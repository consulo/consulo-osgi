package org.jetbrains.osgi.ide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.osgi.OSGiConstants;
import org.jetbrains.osgi.psi.NavigatablePsiElementWrapper;
import org.jetbrains.osgi.serviceComponent.dom.TComponent;
import org.jetbrains.osgi.serviceComponent.dom.TProvide;
import org.jetbrains.osgi.serviceComponent.dom.TService;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.navigation.ClassImplementationsSearch;
import com.intellij.pom.Navigatable;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassObjectAccessExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;

/**
 * @author VISTALL
 * @since 19:38/13.05.13
 */
public class OSGiComponentResolver
{
	public static List<NavigatablePsiElement> resolveImpl(PsiClass psiClass)
	{
		List<NavigatablePsiElement> list = new ArrayList<NavigatablePsiElement>(1);

		PsiAnnotation annotation = AnnotationUtil.findAnnotation(psiClass, OSGiConstants.COMPONENT_ANNOTATION);
		if(annotation != null)
		{
			list.add((NavigatablePsiElement) annotation);
		}

		final List<DomFileElement<TComponent>> fileElements = DomService.getInstance().getFileElements(TComponent.class, psiClass.getProject(),
				GlobalSearchScope.allScope(psiClass.getProject()));
		for(DomFileElement<TComponent> fileElement : fileElements)
		{
			final TComponent rootElement = fileElement.getRootElement();

			final PsiClass value = rootElement.getImplementation().getClazz().getValue();
			if(psiClass.isEquivalentTo(value))
			{
				final XmlElement xmlElement = rootElement.getImplementation().getXmlElement();

				list.add(new NavigatablePsiElementWrapper(xmlElement, (Navigatable) xmlElement));
			}
		}
		return list;
	}

	public static List<NavigatablePsiElement> resolveProvide(final PsiClass psiClass)
	{
		if(CommonClassNames.JAVA_LANG_OBJECT.equals(psiClass.getQualifiedName()))
		{
			return Collections.emptyList();
		}

		final List<NavigatablePsiElement> list = new ArrayList<NavigatablePsiElement>(2);

		ClassImplementationsSearch.processImplementations(psiClass, new Processor<PsiElement>()
		{
			@Override
			public boolean process(PsiElement element)
			{
				if(!(element instanceof PsiClass))
				{
					return true;
				}
				PsiClass iterationClass = (PsiClass) element;
				PsiAnnotation annotation = AnnotationUtil.findAnnotation(iterationClass, OSGiConstants.COMPONENT_ANNOTATION);

				if(annotation == null)
				{
					return true;
				}

				final PsiAnnotationMemberValue provide = annotation.findDeclaredAttributeValue("provide");
				if(provide == null)
				{
					for(PsiClass interfacePsiClass : iterationClass.getInterfaces())
					{
						if(psiClass.isEquivalentTo(interfacePsiClass))
						{
							list.add(iterationClass);
						}
					}
				}
				else if(provide instanceof PsiArrayInitializerMemberValue)
				{
					for(PsiAnnotationMemberValue initializer : ((PsiArrayInitializerMemberValue) provide).getInitializers())
					{
						if(initializer instanceof PsiClassObjectAccessExpression)
						{
							final PsiType type = ((PsiClassObjectAccessExpression) initializer).getOperand().getType();
							final PsiClass psiClassOfType = PsiTypesUtil.getPsiClass(type);
							if(psiClass.isEquivalentTo(psiClassOfType))
							{
								list.add(iterationClass);
							}
						}
					}
				}
				else if(provide instanceof PsiClassObjectAccessExpression)
				{
					final PsiType type = ((PsiClassObjectAccessExpression) provide).getOperand().getType();
					final PsiClass psiClassOfType = PsiTypesUtil.getPsiClass(type);
					if(psiClass.isEquivalentTo(psiClassOfType))
					{
						list.add(iterationClass);
					}
				}
				return true;
			}
		}, psiClass.getUseScope());

		final List<DomFileElement<TComponent>> fileElements = DomService.getInstance().getFileElements(TComponent.class, psiClass.getProject(),
				GlobalSearchScope.allScope(psiClass.getProject()));
		for(DomFileElement<TComponent> fileElement : fileElements)
		{
			final TComponent rootElement = fileElement.getRootElement();

			final TService service = rootElement.getService();
			if(service == null)
			{
				continue;
			}
			final List<TProvide> provides = service.getProvides();
			for(TProvide provide : provides)
			{
				final PsiClass value = provide.getInterface().getValue();
				if(psiClass.isEquivalentTo(value))
				{
					final XmlElement xmlElement = provide.getInterface().getXmlElement();

					list.add(new NavigatablePsiElementWrapper(xmlElement, (Navigatable) xmlElement));
				}
			}
		}
		return list;
	}
}
