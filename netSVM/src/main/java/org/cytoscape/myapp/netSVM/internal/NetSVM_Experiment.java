package org.cytoscape.myapp.netSVM.internal;

import java.awt.Color;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualPropertyDependency;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.jfree.chart.ChartPanel;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.math3.stat.StatUtils;
//import org.apache.commons.math3.stat.StatUtils;
//import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class NetSVM_Experiment extends AbstractTask {
	
	// network creation managers
		private CyNetworkManager cyNetworkManagerServiceRef = null;
		private CyNetworkNaming cyNetworkNamingServiceRef = null;
		private CyNetworkFactory cyNetworkFactoryServiceRef = null;
		private CyNetworkViewFactory networkViewFactory = null;
		private CyNetworkViewManager networkViewManager = null;
		
		// network view managers
		private VisualMappingManager vmmServiceRef = null;
		private VisualStyleFactory vsfServiceRef = null;
		private VisualMappingFunctionFactory vmfFactoryC = null;
		private VisualMappingFunctionFactory vmfFactoryD = null;
		private VisualMappingFunctionFactory vmfFactoryP = null;
		private CyLayoutAlgorithmManager clamRef = null;
		
	    // reference panels
	    private NetSVMDataPanel dataPanel = null;
	    private NetSVMParameterPanel parameterPanel = null;
	    
	    //private netSVM netSVMworker;
	    private String outputfile = null;
	    private String outputdir = null;
	    private JTextField jTextField_file_early_idx = null;
	    private JTextField jTextField_file_late_idx = null;
	    private JTextField jTextField_file_exp_data = null;
	    private JTextField jTextField_file_ppi = null;
	    private JTextField jTextField_file_gid = null;
	    private JTextField jTextField_file_network = null;
	    private JTextField jTextField_file_early_idx_indeptest = null;
	    private JTextField jTextField_file_late_idx_indeptest = null;
	    private JTextField jTextField_file_exp_data_indeptest = null;
	    private JTextField jTextField_output_directory = null;
	    private JTextField jTextField_top_nodes = null;
	    private JTextField jTextField_cross_validation_fold = null;
	    private JTextField jTextField_spe =null;
	    private JTextField jTextField_sen =null;
	    private JTextField jTextField_auc=null;
	    private JTextField jTextField_spe_test =null;
	    private JTextField jTextField_sen_test =null;
	    private Double g_fld_min=0.0, g_fld_max=0.0;
	    
	    public String DEMO_DIR = null;		
    
	    public NetSVM_Experiment(CyNetworkManager cyNetworkManagerServiceRef,
				CyNetworkNaming cyNetworkNamingServiceRef, 
				CyNetworkFactory cyNetworkFactoryServiceRef, 
				CyNetworkViewFactory networkViewFactory, 
				CyNetworkViewManager networkViewManager,
				VisualMappingManager vmmServiceRef, 
				VisualStyleFactory vsfServiceRef, 
				VisualMappingFunctionFactory vmfFactoryC, 
				VisualMappingFunctionFactory vmfFactoryD, 
				VisualMappingFunctionFactory vmfFactoryP,
				CyLayoutAlgorithmManager clamRef, 
				String outputfile, 
				JTextField jTextField_file_early_idx, 
			    JTextField jTextField_file_late_idx, 
			    JTextField jTextField_file_exp_data, 
			    JTextField jTextField_file_ppi, 
			    JTextField jTextField_file_gid,
			    JTextField jTextField_file_early_idx_indeptest, 
			    JTextField jTextField_file_late_idx_indeptest, 
			    JTextField jTextField_file_exp_data_indeptest, 
			    JTextField jTextField_output_directory,
			    JTextField jTextField_top_nodes, 
			    JTextField jTextField_cross_validation_fold,
				NetSVMDataPanel dataPanel, 
				NetSVMParameterPanel parameterPanel, JTextField jTextField_spe, JTextField jTextField_sen, JTextField jTextField_auc, String outputdir, JTextField jTextField_file_network,
				JTextField jTextField_spe_test,JTextField jTextField_sen_test) {
			
			this.cyNetworkFactoryServiceRef = cyNetworkFactoryServiceRef;
			this.cyNetworkManagerServiceRef = cyNetworkManagerServiceRef;
			this.cyNetworkNamingServiceRef = cyNetworkNamingServiceRef;
			this.networkViewFactory = networkViewFactory;
			this.networkViewManager = networkViewManager;
			this.vmmServiceRef = vmmServiceRef;
			this.vsfServiceRef = vsfServiceRef;
			this.vmfFactoryC = vmfFactoryC;
			this.vmfFactoryD = vmfFactoryD;
			this.vmfFactoryP = vmfFactoryP;
			this.clamRef = clamRef;
			this.outputfile = outputfile;
			this.jTextField_file_early_idx = jTextField_file_early_idx;
		    this.jTextField_file_late_idx = jTextField_file_late_idx; 
		    this.jTextField_file_exp_data = jTextField_file_exp_data;
		    this.jTextField_file_ppi = jTextField_file_ppi;
		    this.jTextField_file_gid = jTextField_file_gid;
		    this.jTextField_output_directory = jTextField_output_directory;
		    this.jTextField_spe=jTextField_spe;
		    this.jTextField_sen=jTextField_sen;
		    this.jTextField_auc=jTextField_auc;
		    this.jTextField_top_nodes = jTextField_top_nodes;
		    this.jTextField_cross_validation_fold = jTextField_cross_validation_fold;
			this.dataPanel = dataPanel;
			this.parameterPanel = parameterPanel;	
			this.outputdir=outputdir;
			this.jTextField_file_network=jTextField_file_network;
			this.jTextField_file_early_idx_indeptest = jTextField_file_early_idx_indeptest;
		    this.jTextField_file_late_idx_indeptest = jTextField_file_late_idx_indeptest; 
		    this.jTextField_file_exp_data_indeptest = jTextField_file_exp_data_indeptest;
		    this.jTextField_spe_test=jTextField_spe_test;
		    this.jTextField_sen_test=jTextField_sen_test;	    
		}


		// run task
		@Override
		public void run(TaskMonitor monitor) throws Exception {
			monitor.setTitle("NetSVM experiment");
			// check start time
			//long startTime = System.nanoTime();		

			String file_early_idx = jTextField_file_early_idx.getText();
			String file_late_idx = jTextField_file_late_idx.getText();
			String file_exp_data = jTextField_file_exp_data.getText();
			String file_ppi = jTextField_file_ppi.getText();
			String file_gid = jTextField_file_gid.getText();
			String topnodes = jTextField_top_nodes.getText();	
			String cross_val_fold = jTextField_cross_validation_fold.getText();
			String id2symbol = jTextField_file_network.getText();
			
			String file_early_idx_indeptest = jTextField_file_early_idx_indeptest.getText();
			String file_late_idx_indeptest = jTextField_file_late_idx_indeptest.getText();
			String file_exp_data_indeptest = jTextField_file_exp_data_indeptest.getText();
			

			ReadCSVFile RCV = new ReadCSVFile();
			int[] early_idx=RCV.read_early_idx(file_early_idx);
			int[] late_idx=RCV.read_late_idx(file_late_idx);
			LinkedList<LinkedList<Double>> Gene_exp = RCV.read_Gene_Pattern(file_exp_data);
			int[][] sppi = RCV.read_sppi(file_ppi);
			int[] gid=RCV.read_gid(file_gid);
			int[] g_entree_id=RCV.read_g_entree(file_exp_data);
			int[] early_idx_indeptest=RCV.read_early_idx(file_early_idx_indeptest);
			int[] late_idx_indeptest=RCV.read_late_idx(file_late_idx_indeptest);
			LinkedList<LinkedList<Double>> Gene_exp_indeptest = RCV.read_Gene_Pattern(file_exp_data_indeptest);
			MainClass Demo = new MainClass(gid, g_entree_id,
					Gene_exp, sppi, early_idx, late_idx,topnodes,Integer.parseInt(cross_val_fold),outputdir,
					early_idx_indeptest,late_idx_indeptest,Gene_exp_indeptest);
			Demo.netSVM_main();
			//ChartPanel ROC_curve=Demo.Graph();
			Integer [] gene_net=Demo.get_genenetwork();
			float spe=Demo.get_Spe();
			jTextField_spe.setText(new Float(spe).toString());
			float sen=Demo.get_Sen();
			jTextField_sen.setText(new Float(sen).toString());
			float auc=Demo.get_AUC();
			jTextField_auc.setText(new Float(auc).toString());	
			
			float spe_test=Demo.get_Spe_test();
			jTextField_spe_test.setText(new Float(spe_test).toString());
			float sen_test=Demo.get_Sen_test();
			jTextField_sen_test.setText(new Float(sen_test).toString());
			
			// get weight vector
			double [] weight=Demo.get_WeightVector();
			
			// get fold change
			double [][] data1=Demo.get_data1();
			double [][] data2=Demo.get_data2();
			Double[] G_fld=new Double [data1.length];
			for (int i=0;i<data1.length;i++){
				G_fld[i]=StatUtils.mean(data1[i])-StatUtils.mean(data2[i]);
			}
			
			MultiMap gid2fc = new MultiValueMap();
			//HashMap<Integer, Double> gid2fc = new HashMap<Integer, Double>();
			for(int i=0;i<gene_net.length;i++){
				gid2fc.put(gene_net[i], G_fld[i]);
				gid2fc.put(gene_net[i], weight[i]);
			}
			
			monitor.setProgress(1);
			monitor.setTitle("Creating network");
				
			// increase universal network index    
			CyActivator.totalNumberRun++;          
			// create network                                                       
			CreateNetwork cn = new CreateNetwork(cyNetworkManagerServiceRef,        
					cyNetworkNamingServiceRef,cyNetworkFactoryServiceRef,           
					outputfile, id2symbol,G_fld,gid2fc,spe,sen,auc);	                                        
			cn.create(); 
			
			g_fld_min=cn.getminFC();
			g_fld_max=cn.getmaxFC();
			                                                                        
			// create a new network view                                                                           
			CyNetworkView NetSVMView = networkViewFactory.createNetworkView(cn.network);   
			//VisualStyle style = vmmServiceRef.getVisualStyle(NetSVMView);
			                                                                                                       
			// create two conditions or single condition visual style                                              
			VisualStyle vs = null;                                                                                 
			vs = createVisualStyleSingle(vmmServiceRef, vsfServiceRef,                                             
						vmfFactoryC, vmfFactoryD, vmfFactoryP);		                                               
			vs.apply(NetSVMView);     		
			// change background color
			//NetSVMView.setVisualProperty(BasicVisualLexicon.NETWORK_BACKGROUND_PAINT,Color.LIGHT_GRAY);		
			// update view
			NetSVMView.updateView();
			
			// Add view to Cytoscape                                                                               
			networkViewManager.addNetworkView(NetSVMView);  	
			vmmServiceRef.setVisualStyle(vs, NetSVMView);
			                                                                                                       
			// apply layout                                                                                        
			clamRef.getLayout("force-directed");                                                                   
			CyLayoutAlgorithm layout = clamRef.getLayout("force-directed");                                        
			String layoutAttribute = null;                                                                         
			insertTasksAfterCurrentTask(layout.createTaskIterator(NetSVMView,                                          
					layout.createLayoutContext(),                                                                  
					CyLayoutAlgorithm.ALL_NODE_VIEWS,                                                              
					layoutAttribute));                                                                             
			//jButton_top_nodes.setEnabled(true); 		
			
			monitor.setStatusMessage("Done");
			monitor.setProgress(1);
			
			// check end time
			/*long endTime = System.nanoTime();
			double diff=(endTime - startTime)/1000000000.0;
			jTextField_auc.setText(new Double(diff).toString()+" "+new Float(auc).toString());*/
		} 

		private VisualStyle createVisualStyleSingle(
				VisualMappingManager vmmServiceRef,
				VisualStyleFactory vsfServiceRef,
				VisualMappingFunctionFactory vmfFactoryC,
				VisualMappingFunctionFactory vmfFactoryD,
				VisualMappingFunctionFactory vmfFactoryP) {
			
			// retrieve visual style if already exist
			if(styleExist(vmmServiceRef, "NetSVM visual style - single condition"))
				return getVSstyle(vmmServiceRef, "NetSVM visual style - single condition");
			
			
			// node color setting
			ContinuousMapping<Double, Paint> mapping = (ContinuousMapping<Double, Paint>)
					vmfFactoryC.createVisualMappingFunction("Fold Change", Double.class, BasicVisualLexicon.NODE_FILL_COLOR);
			BoundaryRangeValues<Paint> brv1 = new BoundaryRangeValues<Paint>(Color.GREEN, Color.GREEN, Color.GREEN);	
			BoundaryRangeValues<Paint> brv2 = new BoundaryRangeValues<Paint>(Color.RED, Color.RED, Color.RED);
			mapping.addPoint(g_fld_min, brv1);
			mapping.addPoint(g_fld_max, brv2);
			
			
			//Color NODE_COLOR = new Color(230, 191, 85);
			Color NODE_BORDER_COLOR = Color.WHITE;
			//Color NODE_LABEL_COLOR = new Color(50, 50, 50);
			Color NODE_LABEL_COLOR = Color.BLACK;
			
			
			// To create a new VisualStyle object and set the mapping function
			VisualStyle vs= vsfServiceRef.createVisualStyle("NetSVM visual style - single condition");
			vs.addVisualMappingFunction(mapping);
			
			// unlock node size
			Set<VisualPropertyDependency<?>> deps = vs.getAllVisualPropertyDependencies();
			for(VisualPropertyDependency<?> dep: deps) {
				dep.setDependency(false);
			}
			
			// set node related default		
			vs.setDefaultValue(BasicVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.ELLIPSE);
			vs.setDefaultValue(BasicVisualLexicon.NODE_LABEL_COLOR, NODE_LABEL_COLOR);
			vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_PAINT, NODE_BORDER_COLOR);
			vs.setDefaultValue(BasicVisualLexicon.NODE_TRANSPARENCY, 220);
			vs.setDefaultValue(BasicVisualLexicon.NODE_LABEL_FONT_SIZE, 40);
			
			
			
			// map node names
			String nodeName = "name";
			PassthroughMapping nodeNameMapping = (PassthroughMapping) 
					vmfFactoryP.createVisualMappingFunction(nodeName, String.class, 
							BasicVisualLexicon.NODE_LABEL);
			vs.addVisualMappingFunction(nodeNameMapping);
			
					
			// map edge color
			String edgeType = "interaction";
			DiscreteMapping<String, Paint> edgeTypeMapping = (DiscreteMapping<String, Paint>) 
					vmfFactoryD.createVisualMappingFunction(edgeType, String.class, 
							BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT);
			edgeTypeMapping.putMapValue("static edge", Color.BLUE);
			vs.addVisualMappingFunction(edgeTypeMapping);
			
			// add visual style if not added
			if(!styleExist(vmmServiceRef, "NetSVM visual style - single condition"))
				vmmServiceRef.addVisualStyle(vs);
			
			return vs;
		}

		private VisualStyle getVSstyle(VisualMappingManager vmm,
				String name) {
			Set<VisualStyle> vss = vmm.getAllVisualStyles();
			for(final VisualStyle v : vss) {
				if(v.getTitle() == name)
					return v;
			}
			return null;
		}

		private boolean styleExist(VisualMappingManager vmm,
				String name) {
			
			Set<VisualStyle> vss = vmm.getAllVisualStyles();
			for(final VisualStyle v : vss) {
				if(v.getTitle() == name)
					return true;
			}
			return false;
		}

	}
