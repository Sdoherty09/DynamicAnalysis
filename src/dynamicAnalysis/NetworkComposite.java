package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;

public class NetworkComposite extends Composite
{

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NetworkComposite(Composite parent, int style)
	{
		super(parent, style);
		
		List networkInterfaces = new List(this, SWT.BORDER);
		networkInterfaces.setBounds(0, 0, 115, 300);
		
		List addresses = new List(this, SWT.BORDER);
		addresses.setBounds(121, 0, 115, 300);
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}

//list netstat info
//dropdowns for network interface, address, list packet
//show udp/tcp info