package org.cytoscape.myapp.netSVM.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {

	protected static int totalNumberRun = 0;
	protected static NetSVMConfigurePanel NetSVMConfigurePanel;
	
	public CyActivator() {
		super();
	}
	
	@Override
	public void start(BundleContext bc) throws Exception {
		
		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);

		// register visualization related managers to Cytoscape
		// network creation
		CyNetworkManager cyNetworkManagerServiceRef = getService(bc,CyNetworkManager.class);
		CyNetworkNaming cyNetworkNamingServiceRef = getService(bc,CyNetworkNaming.class);
		CyNetworkFactory cyNetworkFactoryServiceRef = getService(bc,CyNetworkFactory.class);
		
		// progress dialog
		DialogTaskManager dialogTaskManager = getService(bc, DialogTaskManager.class);
		
		// view creation
		CyNetworkViewFactory networkViewFactory = getService(bc, CyNetworkViewFactory.class);
		CyNetworkViewManager networkViewManager = getService(bc, CyNetworkViewManager.class);
  
		// visual style manager
		VisualMappingManager vmmServiceRef = getService(bc,VisualMappingManager.class);
		VisualStyleFactory vsfServiceRef = getService(bc,VisualStyleFactory.class);		 

		// visual mapping functions
		VisualMappingFunctionFactory vmfFactoryC = getService(bc,VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
		VisualMappingFunctionFactory vmfFactoryD = getService(bc,VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
		VisualMappingFunctionFactory vmfFactoryP = getService(bc,VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");

		// layout manager
		CyLayoutAlgorithmManager clamRef = getService(bc, CyLayoutAlgorithmManager.class);
		
		// main control panel added to the left of Cytoscape panel
		NetSVMConfigurePanel = new NetSVMConfigurePanel(cyNetworkManagerServiceRef,
				cyNetworkNamingServiceRef,cyNetworkFactoryServiceRef,
				dialogTaskManager, networkViewFactory, networkViewManager,
				vmmServiceRef, vsfServiceRef, vmfFactoryC,
				vmfFactoryD, vmfFactoryP, clamRef);
		registerService(bc,NetSVMConfigurePanel,CytoPanelComponent.class, new Properties());
		
		// menu items
		CyApplicationManager cyApplicationManager = getService(bc, CyApplicationManager.class);
		
		/*NetSVMMenuAction runMenu = new NetSVMMenuAction(cyApplicationManager, "Run analysis", cytoscapeDesktopService, NetSVMConfigurePanel);
		registerAllServices(bc, runMenu, new Properties());*/
	}

}
