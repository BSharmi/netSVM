package org.cytoscape.myapp.netSVM.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Random;
import java.util.Set;

public class SVM_CV {
	
	double[][] X;
	int[] y;
	int K;
	double[] yPredict,op,outerror;
	Double[] dec_val;
	Integer []testidx;
	MSVM model;
	//Integer [] rp_p;
	//Integer [] rp_n;
	
	public SVM_CV(double[][] X, int[] y, int K) {
		this.X = X;
		this.y = y;
		this.K = K;
	}

public double [] getOuterError(){
		
		//int ngene = X.length;
		int nsample = X[0].length;
		//String KernelType = new String ("linear");
		String[] TestOutputType = new String[] {"label"};
		T T_new = new T(TestOutputType);
		outerror= new double [nsample];
		op= new double [nsample];
		dec_val= new Double [nsample];
		//Arrays.fill(outerror, 2);
		
		//stratified cross validation
		//ArrayList np_arr = new ArrayList();
		int np =0,nn =0;
		ArrayList<Integer> rp_pl = new ArrayList<Integer>();
		ArrayList<Integer> rp_nl = new ArrayList<Integer>();
		for (int i=0;i<y.length;i++) {
			if (y[i] == 1){
				np++;
				rp_pl.add(i);
			}
			else if (y[i] == 2){
				nn++;
				rp_nl.add(i);
			}
		}
		Collections.shuffle(rp_pl);
		Collections.shuffle(rp_nl);
		int kappa_p= (int) Math.floor(np/K);
		int kappa_n= (int) Math.floor(nn/K);
		Integer [] rp_p = new Integer[rp_pl.size()];
		rp_pl.toArray(rp_p);
		Integer [] rp_n = new Integer[rp_nl.size()];
		rp_nl.toArray(rp_n);
		
		Set<Integer> rp = new LinkedHashSet<Integer>(rp_pl);	
		Set<Integer> rp2 = new LinkedHashSet<Integer>(rp_nl);
		//Set<Integer> rp = new LinkedHashSet<Integer>(rp1);
		rp.addAll(rp2);
		
		//System.out.println("rp_p ="+rp_p[1]);
		//System.out.println("kappa"+kappa_p);
		for(int k=0;k<K;k++){
			Integer[]testidx_p = Arrays.copyOfRange(rp_p, k*kappa_p, (k+1)*kappa_p+k*kappa_p);
			Integer[] testidx_n = Arrays.copyOfRange(rp_n, k*kappa_n ,(k+1)*kappa_n+k*kappa_n);  
			if (k==K-1){
				testidx_p = Arrays.copyOfRange(rp_p,k*kappa_p,np+k*kappa_p);
				testidx_n = Arrays.copyOfRange(rp_n, k*kappa_n,nn+k*kappa_n);
				
			}
			Set<Integer> testidx_pset = new LinkedHashSet<Integer>();
			for (int t = 0; t < testidx_p.length; t++) {
				if(testidx_p[t]!= null){
				testidx_pset.add(testidx_p[t]);}
			}
			Set<Integer> testidx_nset = new LinkedHashSet<Integer>();
			for (int t = 0; t < testidx_n.length; t++) {
				if(testidx_n[t]!= null){
				testidx_nset.add(testidx_n[t]);}
			}
			Set<Integer> testidx_set = new LinkedHashSet<Integer>(testidx_pset);
			testidx_set.addAll(testidx_nset);
			Set<Integer> validx_set = new LinkedHashSet<Integer>(rp);
			validx_set.removeAll(testidx_set);
			testidx= new Integer [testidx_set.size()];
			testidx_set.toArray(testidx);
			double [][] Xtest = new double [X.length][testidx.length];
			for(int j=0;j<testidx.length;j++){
				for(int i=0;i<X.length;i++){
					Xtest[i][j]=X[i][testidx[j]];
				}
			}
			double[] ytest= new double [testidx.length];
			for(int i=0;i<testidx.length;i++){
				ytest[i]=y[testidx[i]];
			}
			Integer []validx= new Integer [validx_set.size()];
			validx_set.toArray(validx);
			double [][] Xval = new double [X.length][validx.length];
			for(int j=0;j<validx.length;j++){
				for(int i=0;i<X.length;i++){
					Xval[i][j]=X[i][validx[j]];
				}
			}
			int[] yval= new int [validx.length];
			for(int i=0;i<validx.length;i++){
				yval[i]=y[validx[i]];
			}
			
			OVA_SVM ova_msvm1 = new OVA_SVM(Xval, yval, T_new,Xtest);
			ova_msvm1.classify();
			yPredict =ova_msvm1.getPredicty();// class labels
			model =ova_msvm1.getModel();
			double[] temp = ova_msvm1.getDecVal();
			for(int i=0;i<testidx.length;i++){
				outerror[testidx[i]]=ytest[i] - yPredict[i];
				op[testidx[i]]=yPredict[i];
				dec_val[testidx[i]]=temp[i];
			}
			
		}//end for(int k=0;k<K;k++)
		
		return outerror;		
				
	}// end method
	
	public double [] getop(){
		return op;
	}//end method
	
	public Double[] getDecval(){
		return dec_val;
	}//end method
	
	public MSVM getModel(){
		return model;
	}//

}// end class

