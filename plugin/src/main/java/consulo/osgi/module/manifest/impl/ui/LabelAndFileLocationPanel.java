package consulo.osgi.module.manifest.impl.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;

/**
 * @author VISTALL
 * @since 14:22/30.04.13
 */
public class LabelAndFileLocationPanel extends JPanel
{

	private final TextFieldWithBrowseButton myTextField;

	public LabelAndFileLocationPanel()
	{
		super(new BorderLayout());

		add(new JBLabel("Location: "), BorderLayout.WEST);
		myTextField = new TextFieldWithBrowseButton();
		add(myTextField, BorderLayout.CENTER);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		for(Component component : getComponents())
		{
			component.setEnabled(enabled);
		}
	}

	public TextFieldWithBrowseButton getTextField()
	{
		return myTextField;
	}
}
