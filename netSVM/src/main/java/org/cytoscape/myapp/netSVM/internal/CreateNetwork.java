package org.cytoscape.myapp.netSVM.internal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.session.CyNetworkNaming;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;

public class CreateNetwork {

	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil; 
	private String outputfile;
	public CyNetwork network = null;
	private String id2symbolfile;
	private Double [] g_fld;
	private MultiMap gid2fc;
	private float spe,sen,auc;

	public CreateNetwork(final CyNetworkManager netMgr, 
			final CyNetworkNaming namingUtil, 
			final CyNetworkFactory cnf, String outputfile, String id2symbolfile, Double[] g_fld, MultiMap gid2fc2, float spe, float sen, float auc){
		this.netMgr = netMgr;
		this.cnf = cnf;
		this.namingUtil = namingUtil;
		this.outputfile = outputfile;
		this.id2symbolfile = id2symbolfile;
		this.g_fld=g_fld;
		this.gid2fc=gid2fc2;
		this.spe=spe;
		this.auc=auc;
		this.sen=sen;
	}

	@SuppressWarnings("unchecked")
	public void create() {
		// keep track of already added nodes
		HashMap<String, CyNode> nodeInNetwork = new HashMap<String, CyNode>();
		
		// Create an empty network with unique title
		CyNetwork NetSVMNet = cnf.createNetwork();
		NetSVMNet.getRow(NetSVMNet).set(CyNetwork.NAME,
				      namingUtil.getSuggestedNetworkTitle("NetSVM network " + CyActivator.totalNumberRun));
		
		NetSVMNet.getDefaultNodeTable().createColumn("GeneName", String.class, false);
		NetSVMNet.getDefaultNodeTable().createColumn("Geneid", String.class, false);
		NetSVMNet.getDefaultNodeTable().createColumn("Fold Change", Double.class, false);
		NetSVMNet.getDefaultNodeTable().createColumn("Gene Weight", Double.class, false);
		NetSVMNet.getDefaultNodeTable().createColumn("Location", String.class, false);
		NetSVMNet.getDefaultNodeTable().createColumn("KEGG pathway", String.class, false);
		NetSVMNet.getDefaultNetworkTable().createColumn("Specificity", Double.class, false);
		NetSVMNet.getDefaultNetworkTable().createColumn("Sensitivity", Double.class, false);
		NetSVMNet.getDefaultNetworkTable().createColumn("AUC", Double.class, false);
		
		NetSVMNet.getDefaultNetworkTable().getRow(NetSVMNet.getSUID()).set("Specificity", Double.parseDouble(Float.toString(spe)));
		NetSVMNet.getDefaultNetworkTable().getRow(NetSVMNet.getSUID()).set("Sensitivity", Double.parseDouble(Float.toString(sen)));
		NetSVMNet.getDefaultNetworkTable().getRow(NetSVMNet.getSUID()).set("AUC", Double.parseDouble(Float.toString(auc)));
		
		//Map id2symbol = new HashMap();	
		MultiMap id2symbol = new MultiValueMap();
		
		try {
			BufferedReader reader;
			//String mappingfile = DEMO_DIR + "entrezidmap.txt";
			String mappingfile = id2symbolfile ;
			reader = new BufferedReader(new FileReader(mappingfile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				//String[] parts = line.split("\\s");
				String[] parts = line.split("\t");
				int index = 0;
				String id = null;
				String symbol = null;
				String location = null;
				String GOpath = null;
				for (String part:parts) {
					if (index == 0) {
						id = part;
						index++;
					}
					else if (index == 1) {
						symbol = part;
						index++;
					}
					else if (index == 2) {
						location = part;
						index++;
					}
					else {
						GOpath = part;						
					}
				}
				id2symbol.put(id, symbol);
				id2symbol.put(id, location);
				id2symbol.put(id, GOpath);
			}
			reader.close();
			reader = new BufferedReader(new FileReader(outputfile));
			while ((line = reader.readLine()) != null) {	
				String[] parts = line.split("\\s");
				int index = 0;
				// pair of nodes
				String nd1 = null;
				String nd2 = null;
				String geneid1 = null;
				String geneid2 = null;
				ArrayList<Double> temp1 =new ArrayList<Double>();
				ArrayList<Double> temp2=new ArrayList<Double>();
				String loc_temp1=null, loc_temp2=null;
				String GOpath_temp1=null, GOpath_temp2=null;
				for (String part:parts) {
					if (index == 0) {
						geneid1=part;
						@SuppressWarnings("unchecked")
						ArrayList<String> templist=(ArrayList<String>) id2symbol.get(part);
						if(templist==null)
							nd1 = part;
						else{
							nd1=templist.get(0);
							loc_temp1=templist.get(1);
							GOpath_temp1=templist.get(2);
						}
						temp1=(ArrayList<Double>) gid2fc.get(Integer.parseInt(part));						
						index++;
					}
					else if (index == 1) {
						geneid2=part;
						@SuppressWarnings("unchecked")
						ArrayList<String> templist=(ArrayList<String>) id2symbol.get(part);
						if(templist==null)
							nd2 = part;
						else{
							nd2=templist.get(0);
							loc_temp2=templist.get(1);
							GOpath_temp2=templist.get(2);
						}
						temp2=(ArrayList<Double>) gid2fc.get(Integer.parseInt(part));
						CyNode node1 = null;
						CyNode node2 = null;
						// add node to network if not in network
						
						if(!nodeInNetwork.containsKey(nd1)) {
							node1 = NetSVMNet.addNode();
							NetSVMNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", nd1);
							NetSVMNet.getDefaultNodeTable().getRow(node1.getSUID()).set("Fold Change", temp1.get(0));
							NetSVMNet.getDefaultNodeTable().getRow(node1.getSUID()).set("Gene Weight", temp1.get(1));
							NetSVMNet.getDefaultNodeTable().getRow(node1.getSUID()).set("Location", loc_temp1);
							NetSVMNet.getDefaultNodeTable().getRow(node1.getSUID()).set("Geneid", geneid1);	
							NetSVMNet.getDefaultNodeTable().getRow(node1.getSUID()).set("GeneName", nd1);
							NetSVMNet.getDefaultNodeTable().getRow(node1.getSUID()).set("KEGG pathway", GOpath_temp1);
							
							nodeInNetwork.put(nd1, node1);
						} else {
							node1 = nodeInNetwork.get(nd1);
						}
						if(!nodeInNetwork.containsKey(nd2)) {
							node2 = NetSVMNet.addNode();
							NetSVMNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", nd2);
							NetSVMNet.getDefaultNodeTable().getRow(node2.getSUID()).set("Fold Change", temp2.get(0));
							NetSVMNet.getDefaultNodeTable().getRow(node2.getSUID()).set("Gene Weight", temp2.get(1));
							NetSVMNet.getDefaultNodeTable().getRow(node2.getSUID()).set("Location", loc_temp2);
							NetSVMNet.getDefaultNodeTable().getRow(node2.getSUID()).set("Geneid", geneid2);
							NetSVMNet.getDefaultNodeTable().getRow(node2.getSUID()).set("GeneName", nd2);
							NetSVMNet.getDefaultNodeTable().getRow(node2.getSUID()).set("KEGG pathway", GOpath_temp2);
														
							nodeInNetwork.put(nd2, node2);
						} else {
							node2 = nodeInNetwork.get(nd2);
						}
												
						// add common edge
						String edgeType = "static edge";
						
						// assign edge table attributes
						CyEdge edge = NetSVMNet.addEdge(node1, node2, false);
						NetSVMNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", nd1+"<->"+nd2);
						NetSVMNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("interaction", edgeType);
						
						break;
					}
				}
			} 
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// add network to cytoscape
		netMgr.addNetwork(NetSVMNet);
		
		network = NetSVMNet;
	}// end method
	
	public Double getminFC(){		
		return(Collections.min(Arrays.asList(g_fld)));
	}
	
	public Double getmaxFC(){		
		return(Collections.max(Arrays.asList(g_fld)));
	}

}
