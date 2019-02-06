package org.cytoscape.myapp.netSVM.internal;

import java.io.BufferedReader;
//import org.apache.commons.math3.linear.BlockRealMatrix;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class ReadCSVFile {
	public Integer[][] sppi_D;
	public int[][] sppi;
	public int[] gid_2_jump;
	public Integer[] gid_2_jump_D;
	public Integer []g_entree_id_D;
	public int []g_entree_id;
	//public Double[][] Gene_pattern_D;
	//public double[][] Gene_pattern;
	public Integer[] early_idx_D;
	public int[] early_idx;
	public Integer[] late_idx_D;
	public int[] late_idx;
	public LinkedList<LinkedList<Double>> Gene_pattern_list;
	public Integer[] label_D;
	public int[] label;
	
	public int[][] read_sppi(String sppi_path){
		try{
			String str = "";
			BufferedReader br = new BufferedReader(new FileReader(sppi_path));
			List<LinkedList<Integer>> sppi_list = new LinkedList<LinkedList<Integer>>();
			
			while((str = br.readLine())!= null){
				LinkedList<Integer> tempList = new LinkedList<Integer>();
				for(String i :str.split("\t")){
					tempList.add(Integer.parseInt(i));
				}
				sppi_list.add(tempList);
			}
			
			
			sppi_D= new Integer[sppi_list.size()][2];//array with 2 cols since sppi is nx2 matrix, 
			//can be changed if this assumption is not correct
			sppi= new int[sppi_list.size()][2];
			for (int i = 0; i < sppi_list.size(); i++) {
				sppi_list.get(i).toArray(sppi_D[i]);
				sppi[i]=ArrayUtils.toPrimitive(sppi_D[i]);
			}
			
			br.close();
			// null objects
			str=null;
			sppi_list=null;
			sppi_D=null;
		}//end try
		catch(IOException e){
			e.printStackTrace();
		}
		return sppi;
	}// end method
	
	public int [] read_gid(String gid_path){
		try{
			String str = "";
			BufferedReader br = new BufferedReader(new FileReader(gid_path));
			LinkedList<Integer> gid_2_jump_list = new LinkedList<Integer>();
			
			while((str = br.readLine())!= null){
				gid_2_jump_list.add(Integer.parseInt(str));
				}
					
			gid_2_jump_D= new Integer[gid_2_jump_list.size()]; 
			gid_2_jump= new int[gid_2_jump_list.size()];
			gid_2_jump_list.toArray(gid_2_jump_D);
			gid_2_jump=ArrayUtils.toPrimitive(gid_2_jump_D);	
				
							
			// null objects
			str=null;
			br.close();
			gid_2_jump_list=null;
			gid_2_jump_D=null;
		}// end try
		catch(IOException e){
			e.printStackTrace();
		}	
		return gid_2_jump;
	}//end method	
	
	public int[] read_g_entree(String data_path){
		try{
		/*************** this code will be used if gene_entree_id is selected from the gct data file containing all genes **************************************/
			FileReader fr = null;
		    LineNumberReader lnr = null;
		    String str ="";
		    fr = new FileReader(data_path);
			lnr = new LineNumberReader(fr);
			LinkedList<Integer> g_entree_id_list = new LinkedList<Integer>();
			
			while((str=lnr.readLine())!=null ){
				if (lnr.getLineNumber()>=4){
					String strar[] = str.split("\t");
					g_entree_id_list.add(Integer.parseInt(strar[1]));
				}
			}
		
			g_entree_id_D= new Integer[g_entree_id_list.size()]; 
			g_entree_id= new int[g_entree_id_list.size()];
			g_entree_id_list.toArray(g_entree_id_D);
			g_entree_id=ArrayUtils.toPrimitive(g_entree_id_D);	
			
			// null objects
			str=null;
			lnr.close();
			//fr.close();
			//br.close();
			g_entree_id_list=null;
			g_entree_id_D=null;
		}// end try
		catch(IOException e){
			e.printStackTrace();
		}	
		return g_entree_id;
	}// end method
	
	
	public LinkedList<LinkedList<Double>> read_Gene_Pattern (String data_path){
		try{
			String str = "";
			FileReader fr = null;
		    LineNumberReader lnr = null;
		    fr = new FileReader(data_path);
			lnr = new LineNumberReader(fr);
			Gene_pattern_list = new LinkedList<LinkedList<Double>>();
			
			while((str=lnr.readLine())!=null){
				if (lnr.getLineNumber()>=4){
					LinkedList<Double> tempList = new LinkedList<Double>();
					String[] newstring =str.split("\t");
					for(int c=2;c<newstring.length;c++){
						tempList.add(Double.parseDouble(newstring[c]));
					}
					Gene_pattern_list.add(tempList);
				}
			}
			
			// null objects
			str=null;
			lnr.close();
			//br=null;
			//Gene_pattern_list=null;
			fr.close();
		}//end try
		catch(IOException e){
			e.printStackTrace();
		}
		//return Gene_pattern;
		return Gene_pattern_list;
	}// end method	
	
	public int[] read_early_idx(String early_idx_path){
		try{
			String str = "";
			BufferedReader br = new BufferedReader(new FileReader(early_idx_path));
			LinkedList<Integer> early_idx_list = new LinkedList<Integer>();
			
			while((str = br.readLine())!= null){
				for(String i :str.split("\t")){
					early_idx_list.add(Integer.parseInt(i)-1);					
				}
			}				
					
			early_idx_D= new Integer[early_idx_list.size()]; 
			early_idx= new int[early_idx_list.size()];
			early_idx_list.toArray(early_idx_D);
			early_idx=ArrayUtils.toPrimitive(early_idx_D);
			
			
			// null objects
			str=null;
			br.close();
			early_idx_list=null;
			early_idx_D=null;
			
		}// end try
		catch(IOException e){
			e.printStackTrace();
		}	
		return early_idx;
	}// end method
	
	public int[] read_late_idx(String late_idx_path){
		try{
			String str = "";
			BufferedReader br = new BufferedReader(new FileReader(late_idx_path));
			LinkedList<Integer> late_idx_list = new LinkedList<Integer>();
			
			while((str = br.readLine())!= null){
				for(String i :str.split("\t")){
					late_idx_list.add(Integer.parseInt(i)-1);					
				}
			}				
					
			late_idx_D= new Integer[late_idx_list.size()]; 
			late_idx= new int[late_idx_list.size()];
			late_idx_list.toArray(late_idx_D);
			late_idx=ArrayUtils.toPrimitive(late_idx_D);
			
			
			// null objects
			str=null;
			br.close();
			late_idx_list=null;
			late_idx_D=null;
		}// end try
		catch(IOException e){
			e.printStackTrace();
		}	
		
		return late_idx;
	}// end method
	
	
	}//end class


