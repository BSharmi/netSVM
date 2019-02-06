package org.cytoscape.myapp.netSVM.internal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.List;
//import java.util.List;
//import java.util.Random;
//import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartPanel;

public class MainClass {
	
	int[] gid,g_entree_id,gene_net;
	double[][] sig_gid;
	double[] topgene_weight;
	double[][] X_train,X_test;
	int[][] sppi;
	float[][] nP_avg;
	float[][] nP_std;
	double lambda;
	int[] geneid;
	LinkedList<LinkedList<Double>> Gene_pattern,Gene_pattern_indeptest;
	int[] early_idx, late_idx, label, early_idx_indeptest, late_idx_indeptest;
	float[] spe_ROC,sen_ROC,stats_cv,stats_test;
	float auc_ROC;
	String top_nodes=null;
	String outputdir=null;
	int K;

	public MainClass(int[] gid,int[] g_entree_id, LinkedList<LinkedList<Double>> Gene_pattern, int[][] sppi,int[] early_idx,int[] late_idx, String topnodes, 
			int K, String outputdir,int[] early_idx_indeptest,int[] late_idx_indeptest,LinkedList<LinkedList<Double>> Gene_pattern_indeptest)
	{
		this.gid = gid;
		this.g_entree_id = g_entree_id;
		this.Gene_pattern = Gene_pattern;
		this.sppi = sppi;
		this.early_idx = early_idx;
		this.late_idx = late_idx;
		this.top_nodes=topnodes;
		this.K=K;
		this.outputdir=outputdir;
		this.early_idx_indeptest = early_idx_indeptest;
		this.late_idx_indeptest = late_idx_indeptest;
		this.Gene_pattern_indeptest = Gene_pattern_indeptest;
		//this.label=label;
	}

		public void netSVM_main() {
		
/*		
/**************************************************** code to select the gene expression corresponding to a gene list and ppi start **********************************/
		// g_entry_id matched to Gene_pattern and gid is the smaller gene id provided by user

		// SELECT GENE EXPRESSION FOR THE  GENE SUBSET
		
		Set<Integer> g_entry_id = new LinkedHashSet<Integer>();
		for (int t = 0; t < g_entree_id.length; t++) {
			g_entry_id.add( g_entree_id[t]);
		}
		Set<Integer> gid_2_jump = new LinkedHashSet<Integer>();
		for (int t = 0; t < gid.length; t++) {
			gid_2_jump.add(gid[t]);
		}
		Set<Integer> geneid_set = new LinkedHashSet<Integer>(g_entry_id);
		geneid_set.retainAll(gid_2_jump);// intersection of two gene lists
		
		// null
		gid=null;
		g_entree_id=null;
		gid_2_jump=null;
		
		// remove genes which are not in the ppi network
		Set<Integer> ppi1 = new LinkedHashSet<Integer>();
		Set<Integer> ppi2 = new LinkedHashSet<Integer>();
		for(int i=0;i<sppi.length;i++){
			ppi1.add(sppi[i][0]);
			ppi2.add(sppi[i][1]);
		}
		
		ppi1.addAll(ppi2);
		
		
		// final geneid
		Set<Integer> geneid_temp=new LinkedHashSet<Integer>(geneid_set);
		geneid_temp.removeAll(ppi1);
		if (geneid_temp.size()>0){
			geneid_set.removeAll(geneid_temp);
		}
		
		Integer [] temp = new Integer [geneid_set.size()];
		geneid_set.toArray(temp);
		geneid=ArrayUtils.toPrimitive(temp);
		
		//null
		temp=null;
		geneid_temp=null;
		ppi1=null;
		ppi2=null;
		
		// index of common genes in the gene entry id to select the corresponding rows from gene expression
		// both for cross validation and independent tests
		int[] idx = new int[geneid_set.size()];
		java.util.List<Integer> buff = new LinkedList<Integer>(g_entry_id);
		int x = 0;
		for (Integer j : geneid_set) {
			idx[x] = buff.indexOf(j);
			x++;
		}
		LinkedList<LinkedList<Double>> X_list = new LinkedList<LinkedList<Double>>();
		for (int i = 0; i < idx.length; i++) {
			X_list.add(i, Gene_pattern.get(idx[i]));
		}				

		Double[][] X_D = new Double[X_list.size()][X_list.get(0).size()];
		double[][] X_temp = new double[X_list.size()][];
		for (int i = 0; i < X_list.size(); i++) {
			X_list.get(i).toArray(X_D[i]);
			X_temp[i] = ArrayUtils.toPrimitive(X_D[i]);
		}
		
		// GET THE EARLY AND LATE INDEX VALUES
		X_train = new double[X_D.length][early_idx.length+late_idx.length];
		for (int i = 0; i < X_temp.length; i++) {
			for (int j=0;j<early_idx.length;j++){
				X_train[i][j]=X_temp[i][early_idx[j]];
			}
			int k=early_idx.length-1;
			for (int j=0;j<late_idx.length;j++){
				k=k+1;
				X_train[i][k]=X_temp[i][late_idx[j]];
			}
		}		
		
/**************************************************** code to select the train gene expression corresponding to a gene list and ppi end **********************************/			
		
/**************************************************** code to select the test gene expression corresponding to a gene list and ppi start **********************************/	
		LinkedList<LinkedList<Double>> X_list_indeptest = new LinkedList<LinkedList<Double>>();
		for (int i = 0; i < idx.length; i++) {
			X_list_indeptest.add(i, Gene_pattern_indeptest.get(idx[i]));
		}				

		Double[][] X_D_indeptest = new Double[X_list_indeptest.size()][X_list_indeptest.get(0).size()];
		double[][] X_temp_indeptest = new double[X_list_indeptest.size()][];
		for (int i = 0; i < X_list_indeptest.size(); i++) {
			X_list_indeptest.get(i).toArray(X_D_indeptest[i]);
			X_temp_indeptest[i] = ArrayUtils.toPrimitive(X_D_indeptest[i]);
		}
		
		// GET THE EARLY AND LATE INDEX VALUES
		Set<Integer> early_idx_test_set = new LinkedHashSet<Integer>();
		for (int t = 0; t < early_idx_indeptest.length; t++) {
			if(early_idx_indeptest[t]<X_temp_indeptest[0].length)
			early_idx_test_set.add(early_idx_indeptest[t]);
		}
		Integer[] early_idx_test= new Integer[early_idx_test_set.size()];
		early_idx_test_set.toArray(early_idx_test);
		
		Set<Integer> late_idx_test_set = new LinkedHashSet<Integer>();
		for (int t = 0; t < late_idx_indeptest.length; t++) {
			if(late_idx_indeptest[t]<X_temp_indeptest[0].length)
			late_idx_test_set.add(late_idx_indeptest[t]);
		}
		Integer[] late_idx_test= new Integer[late_idx_test_set.size()];
		late_idx_test_set.toArray(late_idx_test);
		
		
		X_test = new double[X_D_indeptest.length][early_idx_test.length+late_idx_test.length];
		for (int i = 0; i < X_temp_indeptest.length; i++) {
			for (int j=0;j<early_idx_test.length;j++){
				X_test[i][j]=X_temp_indeptest[i][early_idx_test[j]];
			}
			int k=early_idx_test.length-1;
			for (int j=0;j<late_idx_test.length;j++){
				k=k+1;
				X_test[i][k]=X_temp_indeptest[i][late_idx_test[j]];

			}
		}	
		
		
/**************************************************** code to select the test gene expression corresponding to a gene list and ppi end **********************************/			
		
		// cv normalize
		double [][]trainX=X_train;
		Normalize Norm = new Normalize(trainX);
		trainX = Norm.normalize();
		
		int[] tlabel=new int[early_idx.length+late_idx.length];
		Arrays.fill(tlabel, 2);
		for (int i = 0; i < early_idx.length; i++) {
			tlabel[i]=1;
		}
		
		//String[] TestOutputType = new String[] { "label" };
		//T T_new = new T(TestOutputType);
		
		//independent test normalize
		double [][]testX=X_test;
		Normalize Norm1 = new Normalize(testX);
		testX = Norm1.normalize();
		
		int[] plabel=new int[early_idx_test.length+late_idx_test.length];
		Arrays.fill(plabel, 2);
		for (int i = 0; i < early_idx_test.length; i++) {
			plabel[i]=1;
		}

/************************************************************* netSVM ****************************************************************/
		
		//double[] R_lambda = { 0.01,0.02,0.03,0.04,0.05,0.1,0.11,0.12,0.13,0.14,0.15}; // different values of lamda
		double[] R_lambda = { 0.5,1,5,10,30,50,100 }; // different values of lambda
		//double[] R_lambda = { 31,32,33,34 };
		NetSVM_CV_lambda NSVM_CV_lam = new NetSVM_CV_lambda(trainX, tlabel,sppi, geneid, R_lambda, K);
		System.getProperty("java.class.path");
		nP_avg = NSVM_CV_lam.get_avg();
		// NSVM_CV_lam.get_nP_avg();
		nP_std = NSVM_CV_lam.get_std();
		Double [][]dec_val=NSVM_CV_lam.get_Decval();
		double [][]op_all=NSVM_CV_lam.get_op();// labels for all lambda
		double a1 = nP_avg[0][0];
		int b = 0;
		for (int i = 0; i < nP_avg.length; i++) {
			if (nP_avg[i][0] > a1) {
				a1 = nP_avg[i][0];
				b = i;
			}
		}
		lambda = R_lambda[b];
		double [] op=op_all[b]; // labels for best lambda
		
		// get the model with the best lambda selected above
		Cross_Validation_Weight CVW1 = new Cross_Validation_Weight(trainX, tlabel,sppi, geneid, lambda, 5);
		double [] weight_vector =CVW1.get_WeightVector();
		MSVM model =CVW1.getModel();
		Double [] CV_decval=CVW1.get_Decval();
		
		// apply the model on independent test
		System.out.printf("independent data size: %d \n", testX.length );
		System.out.printf("independent label size: %d \n", plabel.length );
		NetSVM_Indep_lambda NetSVMIl1 = new NetSVM_Indep_lambda(trainX, testX, tlabel, plabel,sppi, geneid, lambda, 5);
		double [] y_predict =NetSVMIl1.get_testlabel();
		double [] weight_vector1 =NetSVMIl1.get_Beta();
		
		
		// get top  genes weight
		double [] a_net=new double [weight_vector.length];
		for(int r=0;r< weight_vector.length;r++){
			a_net[r]=Math.abs(weight_vector[r]);
		}
		Arrays.sort(a_net);//sort ascending then reverse the order
		for (int i=0;i<Math.floor(a_net.length)/2;i++){
			 double tmp = a_net[i];
			 a_net[i]=a_net[a_net.length - 1 - i];
			 a_net[a_net.length - 1 - i] = tmp;
		}		
		// get index of sorted weight vector
		ArrayList<Integer> iw_net_list=new ArrayList<Integer>();
		for(int r1=0;r1< a_net.length;r1++){
			for(int q=0;q< weight_vector.length;q++){
				if (a_net[r1]==Math.abs(weight_vector[q])){
					if(!(iw_net_list.contains(q))){
						iw_net_list.add(q);
						break;
						}
					}
				}
			}
		Integer[] iw_net= new Integer[a_net.length];
		iw_net_list.toArray(iw_net);
		
		// get the top genes 
		int top_gene = Integer.parseInt(top_nodes);
		gene_net=new int[top_gene];
		for(int i=0;i< top_gene;i++){
			gene_net[i]=geneid[iw_net[i]];
		} 
		
		// get the top gene's weights
		topgene_weight=Arrays.copyOfRange(a_net, 0, top_gene);
		
		System.out.printf("independent data size: %d \n", testX.length );
		System.out.printf("independent label size: %d \n", plabel.length );
		//System.out.printf("Selected lambda: %f\n",lambda);
		// write top genes to a file
		try {
			//PrintWriter pw = new PrintWriter(new FileWriter(System.getProperty("user.home") + "/Documents/OutputFiles/topgene.txt"));
			PrintWriter pw = new PrintWriter(new FileWriter(outputdir + "/topgene.txt"));
			for (int i=0;i<gene_net.length;i++){
				String sc ="";
				sc+=gene_net[i];
				pw.println(String.valueOf(sc));
				pw.flush();
			}
			pw.close();	
			
			PrintWriter pw1 = new PrintWriter(new FileWriter(outputdir + "/gene_weight.txt"));
			for (int i=0;i<geneid.length;i++){
				String sc ="";
				sc+=weight_vector[i];
				pw1.println(String.valueOf(sc));
				pw1.flush();
			}			
			pw1.close();	
			
			PrintWriter pw2 = new PrintWriter(new FileWriter(outputdir + "/raw_values.txt"));
			for (int i=0;i<tlabel.length;i++){
				String sc ="";
				sc+=CV_decval[i];
				pw2.println(String.valueOf(sc));
				pw2.flush();
			}
			
			pw2.close();
			
			PrintWriter pw3 = new PrintWriter(new FileWriter(outputdir + "/gene_weight_inde.txt"));
			for (int i=0;i<weight_vector1.length;i++){
				String sc ="";
				sc+=weight_vector1[i];
				pw3.println(String.valueOf(sc));
				pw3.flush();
			}			
			pw3.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// calculate accuracy, sensitivity and specificity for cross validation
		stats_cv= getStats(op,tlabel);
		
		// calculate accuracy, sensitivity and specificity for independent test
		stats_test= getStats(y_predict,plabel);
		
		
		float[] auc_temp=new float [dec_val.length];
		float[][] sen_temp=new float [dec_val.length][dec_val[0].length];
		float[][] spe_temp=new float [dec_val.length][dec_val[0].length];

		// sort in decision values descending order 
		for (int i = 0; i < dec_val.length; i++) {
			Double[] sorted_decval = new Double [dec_val[i].length];
			for (int i1 = 0; i1 < dec_val[i].length; i1++) {
				sorted_decval[i1] = dec_val[i][i1];
			}
			Arrays.sort(sorted_decval);
			List<Double> decval_list = Arrays.asList(sorted_decval);
			Collections.reverse(decval_list);
			double [] rank_idx = new double [dec_val[0].length];
			for (int r1 = 0; r1 < dec_val[0].length; r1++) {
				rank_idx[r1] = Arrays.asList(dec_val[i]).indexOf(decval_list.get(r1));
			}
			// add ROC Curve here
			ROC_Curve RC = new ROC_Curve(rank_idx,tlabel);
			RC.get_value();
			sen_temp[i] = RC.sen;
			spe_temp[i]=RC.spe_roc;
			auc_temp[i]=RC.auc;
		}	
		// calculate mean sen and spe for roc curve
		sen_ROC= new float[sen_temp[0].length];
		spe_ROC= new float[sen_temp[0].length];
		for (int i = 0; i < sen_temp[0].length; i++){
			float sum11=(float) 0.0;
			for(int i1 = 0; i1 < sen_temp.length; i1++){
				sum11+=sen_temp[i1][i];
			}
			sen_ROC[i]=sum11/sen_temp.length;
		}
		for (int i = 0; i < spe_temp[0].length; i++){
			float sum11=(float) 0.0;
			for(int i1 = 0; i1 < spe_temp.length; i1++){
				sum11+=spe_temp[i1][i];
			}
			spe_ROC[i]=sum11/spe_temp.length;
		}
		
		float sum11=(float) 0.0;
		for (int i = 0; i < auc_temp.length; i++){
			sum11+=auc_temp[i];
		}
		auc_ROC=sum11/auc_temp.length;
		//System.out.println("auc =" +Math.abs(auc_ROC));		
		
		
	}// end method

	public ChartPanel Graph(){
		Graph_GUI G1= new Graph_GUI("ROC Curve");
		ChartPanel ROC_curve=G1.display(spe_ROC,sen_ROC);	
		return ROC_curve;
	}	
	
	public Integer [] get_genenetwork(){
		LinkedList<Integer> index1 = new LinkedList<Integer>();
		LinkedList<Integer> index2 = new LinkedList<Integer>();
		for (int i = 0; i < gene_net.length; i++) {
			for(int j=0;j<sppi.length;j++){
				if(sppi[j][0]==gene_net[i])
					index1.add(j);
				if(sppi[j][1]==gene_net[i])
					index2.add(j);					
			}
		}		

		Set<Integer> index1_set = new LinkedHashSet<Integer>();
		for (int t = 0; t < index1.size(); t++) {
			index1_set.add( index1.get(t));
		}
		Set<Integer> index2_set = new LinkedHashSet<Integer>();
		for (int t = 0; t < index2.size(); t++) {
			index2_set.add( index2.get(t));
		}
		Set<Integer> index_set = new LinkedHashSet<Integer>(index1_set);
		index_set.retainAll(index2_set);
		Integer[] index =  new Integer [index_set.size()];
		index_set.toArray(index);

		int[][] network_ppi = new int[index.length][2];
		for (int i = 0; i < index.length; i++) {
			network_ppi[i][0]=sppi[index[i]][0];
			network_ppi[i][1]=sppi[index[i]][1];
		}		
		// write network to file
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(outputdir + "/network.txt"));
			for (int i=0;i<network_ppi.length;i++){
				String sc ="";
				for (int j=0;j<network_ppi[i].length;j++){
                    sc+=network_ppi[i][j]+" ";                    
            }
				sc=sc.substring(0, sc.length()-1);
				pw.println(String.valueOf(sc));
				pw.flush();
			}
			pw.close();			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		Integer[] gene_network =new Integer[gene_net.length];
		for (int i=0;i<gene_net.length;i++){
			gene_network[i]=(Integer)gene_net[i];
		}		
		return gene_network;
	}// end method
	
	public float get_Spe(){
		return stats_cv[2];	
	}
	
	public float get_Sen(){
		return stats_cv[1];	
	}
	public float get_AUC(){
		return Math.abs(auc_ROC);	
	}
	
	public double[] get_WeightVector(){
		return topgene_weight;	
	}
	
	public double [][] get_data1(){
		LinkedList<Integer> data1_list = new LinkedList<Integer>();
		for (int i=0;i<geneid.length;i++){
			data1_list.add(geneid[i]);
		}
		double[][] data1 = new double[gene_net.length][early_idx.length];		
		for (int i=0;i<gene_net.length;i++){
			int idx=data1_list.indexOf(gene_net[i]);
			data1[i]=Arrays.copyOfRange(X_train[idx], 0, early_idx.length);
			/*for (int j=0;j<early_idx.length;j++){
				data1[i][j]=X[idx][j];
			}*/
		}		
		
		return data1;
	}
	public double [][] get_data2(){
		LinkedList<Integer> data2_list = new LinkedList<Integer>();
		for (int i=0;i<geneid.length;i++){
			data2_list.add(geneid[i]);
		}
		double[][] data2 = new double[gene_net.length][late_idx.length];				
		for (int i=0;i<gene_net.length;i++){
			int idx=data2_list.indexOf(gene_net[i]);
			data2[i]=Arrays.copyOfRange(X_train[idx], early_idx.length, early_idx.length+late_idx.length);
			/*int k=-1;
			for (int j=early_idx.length;j<early_idx.length+late_idx.length;j++){
				k+=1;
				data2[i][k]=X[idx][j];
			}	*/		
		}
		return data2;
	}	
	
	public float [] getStats (double [] predict_label, int [] label){
		float acc,spe,sen;
		float [] res=new float[3];
		double []cross_error = new double[predict_label.length];
		for (int i = 0; i < predict_label.length; i++) {
			cross_error[i]=label[i]-predict_label[i];
		}
		int sum=0;
		for(double r: cross_error){
			if(r==0)
				sum++;
		}
		int sum1=0;
		for(double r: label){
			if(r==1)
				sum1++;
		}
		int sum2=0;
		for(double r: cross_error){
			if(r==-1)
				sum2++;
		}
		int sum3=0;
		for(double r: cross_error){
			if(r==1)
				sum3++;
		}
		int sum4=0;
		for(double r: label){
			if(r==2)
				sum4++;
		}

		acc=(float)sum/cross_error.length;				
		sen=(float)(sum1-sum2)/sum1;					
		spe=(float)(sum4-sum3)/sum4;
		//System.out.println("acc =" +acc);
		//System.out.println("sen =" +sen);
		//System.out.println("spe =" +spe);
		res[0]=acc;
		res[1]=sen;
		res[2]=spe;
		return(res);
	}
	
	public float get_Spe_test(){
		return stats_test[2];	
	}
	
	public float get_Sen_test(){
		return stats_test[1];	
	}

	/***************************************************** main method *********************************************************************/
	public static void main(){}
}// end class
