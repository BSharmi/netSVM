package org.cytoscape.myapp.netSVM.internal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.swing.DialogTaskManager;

public class NetSVMConfigurePanel extends JPanel implements CytoPanelComponent {
	// separate panels for data input, parameter setting, and run setting
	private NetSVMParameterPanel parameterPanel = null;
	private NetSVMDataPanel dataPanel = null;
	protected static NetSVMRunPanel runPanel;
	
	// network creation and visualization managers
	private CyNetworkManager cyNetworkManagerServiceRef = null;
	private CyNetworkNaming cyNetworkNamingServiceRef = null;
	private CyNetworkFactory cyNetworkFactoryServiceRef = null;
	private CyNetworkViewFactory networkViewFactory = null;
	private CyNetworkViewManager networkViewManager = null;
	private VisualMappingManager vmmServiceRef = null;
	private VisualStyleFactory vsfServiceRef = null;
	private VisualMappingFunctionFactory vmfFactoryC = null;
	private VisualMappingFunctionFactory vmfFactoryD = null;
	private VisualMappingFunctionFactory vmfFactoryP = null;
	
	// progress dialog
	private DialogTaskManager dialogTaskManager = null;
	
	// layout manager
	private CyLayoutAlgorithmManager clamRef = null;
	
	public NetSVMConfigurePanel(CyNetworkManager cyNetworkManagerServiceRef, 
			CyNetworkNaming cyNetworkNamingServiceRef, 
			CyNetworkFactory cyNetworkFactoryServiceRef, 
			DialogTaskManager dialogTaskManager, 
			CyNetworkViewFactory networkViewFactory, 
			CyNetworkViewManager networkViewManager, 
			VisualMappingManager vmmServiceRef, 
			VisualStyleFactory vsfServiceRef, 
			VisualMappingFunctionFactory vmfFactoryC, 
			VisualMappingFunctionFactory vmfFactoryD, 
			VisualMappingFunctionFactory vmfFactoryP, 
			CyLayoutAlgorithmManager clamRef) {

		this.cyNetworkFactoryServiceRef = cyNetworkFactoryServiceRef;
		this.cyNetworkManagerServiceRef = cyNetworkManagerServiceRef;
		this.cyNetworkNamingServiceRef = cyNetworkNamingServiceRef;
		this.dialogTaskManager = dialogTaskManager;
		this.networkViewFactory = networkViewFactory;
		this.networkViewManager = networkViewManager;
		this.vmmServiceRef = vmmServiceRef;
		this.vsfServiceRef = vsfServiceRef;
		this.vmfFactoryC = vmfFactoryC;
		this.vmfFactoryD = vmfFactoryD;
		this.vmfFactoryP = vmfFactoryP;
		this.clamRef = clamRef;

		// extra space at the bottom
		JPanel bottomPanel = new JPanel();
		
		// dimensions
		int width = (int)(0.25 * Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		this.setPreferredSize(new Dimension(width, 0));
		
		// step 2 parameter choice
		parameterPanel = new NetSVMParameterPanel();
		
		// step 1 data input
		dataPanel = new NetSVMDataPanel(parameterPanel, this);
		
		// step 3 run MS
		runPanel = new NetSVMRunPanel(this.cyNetworkManagerServiceRef,
				this.cyNetworkNamingServiceRef,
				this.cyNetworkFactoryServiceRef,
				this.dialogTaskManager, 
				this.networkViewFactory,
				this.networkViewManager,
				this.vmmServiceRef, this.vsfServiceRef, this.vmfFactoryC,
				this.vmfFactoryD, this.vmfFactoryP, this.clamRef,
				parameterPanel, dataPanel, this);
		
		// layout the panels
		JPanel agentPanel = new JPanel(new BorderLayout(2,0));
		
		this.setLayout(new BorderLayout(2,0));
		agentPanel.add(mergePanel(mergePanel(dataPanel, parameterPanel), runPanel), BorderLayout.CENTER);
		agentPanel.add(bottomPanel, BorderLayout.SOUTH);

		JScrollPane scrp = new JScrollPane(agentPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(scrp, BorderLayout.CENTER);
	}

	private JPanel mergePanel(JPanel p1,
			JPanel p2) {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(2,0));
		panel.add(p1, BorderLayout.NORTH);
		panel.add(p2, BorderLayout.CENTER);
		
		return panel;
	}


	public Component getComponent() {
		return this;
	}


	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}


	public String getTitle() {
		return "NetSVM";
	}


	public Icon getIcon() {
		return null;
	}
}
