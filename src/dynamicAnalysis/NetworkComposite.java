package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

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
		
		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 10, 138, 280);
		
		ToolItem tltmNetworkinterface = new ToolItem(toolBar, SWT.DROP_DOWN);
		tltmNetworkinterface.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		tltmNetworkinterface.setText("NetworkInterface1");
		
		ToolBar toolBar_1 = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar_1.setBounds(154, 10, 138, 280);

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