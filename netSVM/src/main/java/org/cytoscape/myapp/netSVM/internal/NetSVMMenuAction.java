package org.cytoscape.myapp.netSVM.internal;

import java.awt.event.ActionEvent;

import javax.help.CSH.DisplayHelpFromSource;
import javax.help.HelpBroker;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;

public class NetSVMMenuAction extends AbstractCyAction {

	// Cytoscape swing service
	private final CySwingApplication desktopApp;
		
	// Cytoscape panels
	private final CytoPanel cyPanelWest;
	private final NetSVMConfigurePanel controlPanel;
		
	public NetSVMMenuAction(CyApplicationManager cyApplicationManager, final String menuTitle,
			CySwingApplication desktopApp,
			NetSVMConfigurePanel myControlPanel) {
		
		super(menuTitle);
		setPreferredMenu("Apps.NetSVM");
		
		this.name = name;
		this.desktopApp = desktopApp;
		this.cyPanelWest = this.desktopApp.getCytoPanel(CytoPanelName.WEST);
		this.controlPanel = myControlPanel;
	}

	public void actionPerformed(ActionEvent e) {

		if (name == "Run analysis") {
			selectInputPanel();
		}
		
	}
	
	/**
	 * Select Cytoscape control panel
	 */
	public void selectInputPanel() {
		// If the state of the cytoPanelWest is HIDE, show it
		if (cyPanelWest.getState() == CytoPanelState.HIDE) {
			cyPanelWest.setState(CytoPanelState.DOCK);
		}
		// Select my panel
		int index = cyPanelWest.indexOfComponent(controlPanel);
		if (index == -1) {
			return;
		}
		cyPanelWest.setSelectedIndex(index);
	}
}

