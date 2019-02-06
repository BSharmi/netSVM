package org.cytoscape.myapp.netSVM.internal;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import Jama.Matrix;

public class NetSVM_Indep_lambda {
	double[][] trainX_d;
	double[][] X;
	//Double[][] dec_val;
	int[][] ppi;
	int[] geneid;
	double lambda_cross;
	int[] trainy;
	int[] y;
	int K;
	double lambda_weight;
	double [] beta;
	double[] yPredict,op,outerror,dec_val;
	//Double[] dec_val;


	public NetSVM_Indep_lambda(double[][] trainX_d, double[][] X, int[] trainy, int[] y, int[][] ppi,
			int[] geneid, double lambda, int K) {
		this.trainX_d = trainX_d;
		this.X = X;
		this.ppi = ppi;
		this.trainy = trainy;
		this.y = y;
		this.geneid = geneid;
		this.lambda_cross = lambda;
		this.K = K;
	}


	public double[]  get_testlabel() {
		int a = 0, b = 0;
		boolean flg_a = false, flg_b = false;
		double[][] network = new double[geneid.length][geneid.length];
		for (int i = 0; i < ppi.length; i++) {
			for (int j = 0; j < geneid.length; j++) {
				if (flg_a == false || flg_b == false) {
					if (geneid[j] == ppi[i][0]) {
						a = j;
						flg_a = true;
					}
					if (geneid[j] == ppi[i][1]) {
						b = j;
						flg_b = true;
					}
				} 
				else
					break;
			}
			network[a][b] = 1;
			network[b][a] = 1;
			flg_a = false;
			flg_b = false;
		}

		double [][] network2=new double[network.length][network[0].length];
		for(int i=0;i<network.length; i++){
			for (int j=0;j<network[i].length;j++){
				network2[i][j]=network[i][j]+network[j][i];
				if (network2[i][j]>0)
					network2[i][j]=1;
			}
		}
		
		// null object
		network=null;		


		int n_node = network2.length;

		double[][] L = new double[n_node][n_node];
		for (int i = 0; i < n_node; i++) {
			for (int j = 0; j <= i; j++) {
				if (j == i) {
					double sum = 0;
					for (double pp : network2[j]) {
						sum = sum + pp;
					}
					L[j][j] = sum;
				} else if (network2[i][j] == 1) {
					L[i][j] = -1;
					L[j][i] = -1;
				}
			}
		}

		// null object
		network2 = null;

		
		double[][] LL = new double[L.length][];
		for (int i = 0; i < L.length; i++)
			LL[i] = L[i].clone();
		
		for (int i = 0; i < n_node; i++) {
			for (int j = 0; j < n_node; j++) {
				double div = Math.sqrt(LL[i][i]);
				double div_1 = Math.sqrt(LL[j][j]);
				if (LL[i][i]>0 && LL[j][j]>0){
					L[i][j] = L[i][j] / div;
					L[i][j] = L[i][j] / div_1;
				}
			}
		}

		double[][] L_diag = new double[L.length][L[0].length];
		for (int i = 0; i < L_diag.length; i++) {
			for (int j = 0; j < L_diag[0].length; j++) {
				L_diag[i][j] = (i == j) ? 1 : 0;
			}
		}
		
		//Matrix L_dia=new Matrix(L_diag);
		//L_diag=null;
		
		//Matrix L_bk = new Matrix(L);
		// null object
		//L = null;
		
		//Matrix LL_bk=new Matrix(LL);		
		// null object
		//LL = null;
		
		for(int i=0;i<LL.length;i++){
			for(int j=0;j<LL[i].length;j++){
				LL[i][j]=L[i][j]*lambda_cross+L_diag[i][j];
			}
		}
		Matrix LL_bk=new Matrix(LL);
		//LL_bk=L_bk.times(lambda_cross).plus(L_dia);
		Matrix PP_Chol = LL_bk.chol().getL();
		Matrix xPP_Jama1 = PP_Chol.transpose().inverse(); // mH
		Matrix xPP_Jama = xPP_Jama1.transpose();
		
		//new LUDecomposition(PP_Chol).solve(PP_Chol).inverse();
		Matrix X_blk = new Matrix(X);
		Matrix testX_blk =xPP_Jama.times(X_blk); 				
		double [][] testX=testX_blk.getArray();
		
		Matrix X_blk1 = new Matrix(trainX_d);
		Matrix trainX_blk = xPP_Jama.times(X_blk1);
		double [][] trainX = trainX_blk.getArray();
		
		
		
		//Modified by Henry
		//Matrix betaPP_blk=PP_Chol;
		Matrix betaPP_blk = xPP_Jama1;
		
		String[] TestOutputType = new String[] {"label"};
		T T_new = new T(TestOutputType);
		OVA_SVM ova_msvm1 = new OVA_SVM(trainX, trainy, T_new, testX);
		ova_msvm1.classify();

		//OVA_SVM_indep ova_msvm1 = new OVA_SVM_indep(testX, y, model);
		//ova_msvm1.classify();
		yPredict =ova_msvm1.getPredicty();// class labels	
		MSVM model = ova_msvm1.getModel();
		double [] beta0 = model.LinearSVMNormVector;
		
		double[][] betaPP= betaPP_blk.getArray();
		beta = new double [betaPP.length];
		for(int i = 0;i<betaPP.length;i++){
			for(int j=0;j<betaPP[i].length;j++){
				beta[i]+=betaPP[i][j]*beta0[j];
			}
		}
		
		
		return yPredict;

	}// end method

	
	public double[] get_Beta() {
		return beta;
	}

}
