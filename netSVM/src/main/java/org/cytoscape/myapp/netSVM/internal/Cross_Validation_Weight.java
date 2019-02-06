package org.cytoscape.myapp.netSVM.internal;

import Jama.Matrix;

public class Cross_Validation_Weight {

	double[][] X;
	//Double[][] dec_val;
	int[][] ppi;
	int[] geneid;
	double lambda_cross;
	int[] y;
	int K;
	MSVM model;
	double lambda_weight;
	double [] beta;
	Double [] dec_val;

	public Cross_Validation_Weight(double[][] X, int[] y, int[][] ppi,
			int[] geneid, double lambda, int K) {
		this.X = X;
		this.ppi = ppi;
		this.y = y;
		this.geneid = geneid;
		this.lambda_cross = lambda;
		this.K = K;
	}


	public double[]  get_WeightVector() {

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
		Matrix xPP_Jama = PP_Chol.inverse();
		//new LUDecomposition(PP_Chol).solve(PP_Chol).inverse();
		Matrix X_Jama=new Matrix(X);
		Matrix trainX_Jam=xPP_Jama.times(X_Jama);
		double [][] trainX=trainX_Jam.getArray();
		Matrix betaPP_blk=PP_Chol;

		

		SVM_CV svm_c = new SVM_CV(trainX,y,K);
		double[] outerror =svm_c.getOuterError();//do not comment out. this is the main method call
		dec_val= svm_c.getDecval();
		model =svm_c.getModel();
		double [] beta0 = model.LinearSVMNormVector;
		double[][] betaPP= betaPP_blk.getArray();
		beta = new double [betaPP.length];
		for(int i = 0;i<betaPP.length;i++){
			for(int j=0;j<betaPP[i].length;j++){
				beta[i]+=betaPP[i][j]*beta0[j];
			}
		}			

		return beta;

	}// end method

	public MSVM getModel(){
		return model;
	}
	public Double[] get_Decval() {
		return dec_val;
	}

}// end class

