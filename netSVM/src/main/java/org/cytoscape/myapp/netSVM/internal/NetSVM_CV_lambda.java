package org.cytoscape.myapp.netSVM.internal;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import Jama.*;
//import jeigen.DenseMatrix;

public class NetSVM_CV_lambda {

	double[][] X,op;
	Double[][] dec_val;
	int[][] ppi;
	int[] geneid;
	double[] R_lambda,beta;
	int[] y;
	int K;
	float [][]nP_avg;
	float [][]nP_std ;
	MSVM model;
	double lambda_weight;


	public NetSVM_CV_lambda(double[][] X, int[] y, int[][] ppi,
			int[] geneid, double[] R_lambda, int K) {
		this.X = X;
		this.ppi = ppi;
		this.y = y;
		this.geneid = geneid;
		this.R_lambda = R_lambda;
		this.K = K;
	}

	public float[][]  get_avg() {

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
		int temp=L.length;
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
		
		
		/*double [] L_diag_arr= new double[L.length];
		Arrays.fill(L_diag_arr, 1);
		DiagonalMatrix L_diag_blk = new DiagonalMatrix(L_diag_arr);
		*/
		double[][] L_diag = new double[L.length][L[0].length];
		for (int i = 0; i < L_diag.length; i++) {
			for (int j = 0; j < L_diag[0].length; j++) {
				L_diag[i][j] = (i == j) ? 1 : 0;
			}
		}
		//Matrix L_dia=new Matrix(L_diag);
		//L_diag=null;
		

		int k = -1;
		//BlockRealMatrix L_blk = new BlockRealMatrix(L);
		//Matrix L_bk = new Matrix(L);
		// null object
		//L = null;

		//BlockRealMatrix LL_blk = new BlockRealMatrix(LL);
		//Matrix LL_bk=new Matrix(LL);
		
		// null object
		//LL = null;

		nP_avg = new float [R_lambda.length][3];
		nP_std = new float [R_lambda.length][3];

		dec_val= new Double[R_lambda.length][y.length];
		op=new double[R_lambda.length][y.length];
		
		
		
		
		for (double lambda :R_lambda) {
			//System.out.println("lambda");
			//System.out.println(lambda);
			
			k = k + 1;
			for(int i=0;i<LL.length;i++){
				for(int j=0;j<LL[i].length;j++){
					LL[i][j]=L[i][j]*lambda+L_diag[i][j];
				}
			}
			
			
			Matrix LL_bk=new Matrix(LL);
			//LL_bk=L_bk.times(lambda).plus(L_dia);
			Matrix PP_Chol = LL_bk.chol().getL();
			
			/*for (int i = 0; i < PP_Chol.getRowDimension(); i++)
			{
				for (int ii = 0; ii < PP_Chol.getColumnDimension(); ii++)
				{
					System.out.printf("%f ",PP_Chol.get(i, ii));
				}
				System.out.printf("\n");
			}*/
			
			Matrix xPP_Jama = PP_Chol.inverse();
			//new LUDecomposition(PP_Chol).solve(PP_Chol).inverse();
			Matrix X_Jama=new Matrix(X);
			
			Matrix trainX_Jam=xPP_Jama.times(X_Jama);
			double [][] trainX=trainX_Jam.getArray();
			
			/*for (int i = 0; i < trainX.length; i++)
			{
				for (int ii = 0; ii < trainX[i].length; ii++)
				{
					System.out.printf("%f ",trainX[i][ii]);
				}
				System.out.printf("\n");
			}*/
			
			float[] acc = new float[10];
			float[] sen = new float[10];
			float[] spe = new float[10];


			for(int iter =0;iter<10;iter++){
				SVM_CV svm_c = new SVM_CV(trainX,y,K);
				double[] outerror =svm_c.getOuterError();
				dec_val[k]=svm_c.getDecval(); // OUTSIDE THE LOOP< CALL JUST ONCE
				op[k]=svm_c.getop();
				model =svm_c.getModel();
				/*	double [] beta0 = model.LinearSVMNormVector;
					double[][] betaPP= betaPP_blk.getData();
					beta = new double [betaPP.length];
					for(int i = 0;i<betaPP.length;i++){
						for(int j=0;j<betaPP[i].length;j++){
							beta[i]+=betaPP[i][j]*beta0[j];
						}
					}*/
				int sum=0;
				for(double r: outerror){
					if(r==0)
						sum++;
				}
				int sum1=0;
				for(double r: y){
					if(r==1)
						sum1++;
				}
				int sum2=0;
				for(double r: outerror){
					if(r==-1)
						sum2++;
				}
				int sum3=0;
				for(double r: outerror){
					if(r==1)
						sum3++;
				}
				int sum4=0;
				for(double r: y){
					if(r==2)
						sum4++;
				}

				acc[iter]=(float)sum/outerror.length;				
				sen[iter]=(float)(sum1-sum2)/sum1;					
				spe[iter]=(float)(sum4-sum3)/sum4;

			}//end for(int iter =0;iter<10;iter++)


			float mean_acc =0, sum_acc=0;
			for (int i = 0; i < acc.length; i++) {
				sum_acc += acc[i];
			}
			mean_acc = (float)sum_acc / acc.length;				
			nP_avg[k][0]=mean_acc;

			float mean_sen =0, sum_sen=0;
			for (int i = 0; i < sen.length; i++) {
				sum_sen += sen[i];
			}
			mean_sen =(float) sum_sen / sen.length;				
			nP_avg[k][1]=mean_sen;

			float mean_spe =0, sum_spe=0;
			for (int i = 0; i < spe.length; i++) {
				sum_spe += spe[i];
			}
			mean_spe = (float)sum_spe / spe.length;				
			nP_avg[k][2]=mean_spe;

			double temp_acc=0;
			for(double p:acc){
				temp_acc += (p-mean_acc)*(p-mean_acc);
			}				
			nP_std[k][0]=(float) Math.sqrt(temp_acc/(acc.length-1));

			double temp_sen=0;
			for(double p:sen){
				temp_sen += (p-mean_sen)*(p-mean_sen);
			}
			nP_std[k][1]=(float) Math.sqrt(temp_sen/(sen.length-1));

			double temp_spe=0;
			for(double p:spe){
				temp_spe += (p-mean_spe)*(p-mean_spe);
			}
			nP_std[k][2]=(float) Math.sqrt(temp_spe/(spe.length-1));


		}//end for (double lambda :R_lambda) 

		return nP_avg;

	}// end method

	public float[][] get_std() {
		return nP_std;
	}
	public Double[][] get_Decval() {
		return dec_val;
	}
	public double[][] get_op() {
		return op; //class labels for all lambda
	}
	public double[] get_WeightVector() {
		return beta;
	}

}// end class
