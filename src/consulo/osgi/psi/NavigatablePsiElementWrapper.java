/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.osgi.psi;

import javax.swing.Icon;

import com.intellij.openapi.util.TextRange;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.impl.RenameableFakePsiElement;
import consulo.osgi.OSGiIcons;

/**
 * @see org.jetbrains.idea.maven.dom.references.MavenPsiElementWrapper
 */
public class NavigatablePsiElementWrapper extends RenameableFakePsiElement
{
	private final PsiElement myWrappee;
	private final Navigatable myNavigatable;

	public NavigatablePsiElementWrapper(PsiElement wrappeeElement, Navigatable navigatable)
	{
		super(wrappeeElement.getParent());
		myWrappee = wrappeeElement;
		myNavigatable = navigatable;
	}

	public PsiElement getWrappee()
	{
		return myWrappee;
	}

	public PsiElement getParent()
	{
		return myWrappee.getParent();
	}

	@Override
	public String getName()
	{
		return ((PsiNamedElement) myWrappee).getName();
	}

	@Override
	public void navigate(boolean requestFocus)
	{
		myNavigatable.navigate(requestFocus);
	}

	public String getTypeName()
	{
		return "Property";
	}

	public Icon getIcon()
	{
		return OSGiIcons.OsgiComponentFile;
	}

	@Override
	public TextRange getTextRange()
	{
		return myWrappee.getTextRange();
	}

	@Override
	public boolean isEquivalentTo(PsiElement other)
	{
		if(other instanceof NavigatablePsiElementWrapper)
		{
			return myWrappee == ((NavigatablePsiElementWrapper) other).myWrappee;
		}
		return myWrappee == other;
	}
}
