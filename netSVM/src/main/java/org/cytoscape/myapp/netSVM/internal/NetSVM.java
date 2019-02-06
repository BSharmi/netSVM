package org.cytoscape.myapp.netSVM.internal;

import java.util.Arrays;
//import org.apache.commons.math3.linear.AnyMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
//import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
//import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.DiagonalMatrix;

public class NetSVM {
	
	double[][] trainX;
	int[] trainy;
	T T_new;
	double[][] testX;
	int[][] ppi;
	int[] geneid;
	double lambda;
	double[] output;
	
	
	public NetSVM(double[][] trainX, int[] trainy, T T_new,double[][] testX,int[][] ppi, int[] gid, double lambda) {
		this.trainX = trainX;
		this.trainy = trainy;
		this.T_new = T_new;
		this.testX=testX;
		this.ppi = ppi;
		this.geneid = gid;
		this.lambda = lambda;
	}
	
public double[]  get_beta(){
		
		int a = 0, b = 0;
		boolean flg_a = false,flg_b=false;
		double[][] network = new double[geneid.length][geneid.length];
		for (int i = 0; i < ppi.length; i++) {
			for (int j = 0; j < geneid.length; j++) {
				if(flg_a==false || flg_b==false){
					if (geneid[j] == ppi[i][0]){
						a = j;
						flg_a = true;
					}
					if (geneid[j] == ppi[i][1]){
						b = j;
						flg_b=true;
					}
				}
				else
					break;
			}	
			network[a][b] = 1;
			network[b][a] = 1;
			flg_a = false;
			flg_b=false;			
		}
		
		BlockRealMatrix B = new BlockRealMatrix(network);
		BlockRealMatrix network21 = B.add(B.transpose());
		double[][] network2 = network21.getData();
		for (int i = 0; i < network2.length; i++) {
			for (int j = 0; j < network2[i].length; j++) {
				if (network2[i][j]>0)
					network2[i][j]=1;
				}
			}
		int n_node = network2.length;
		
		// for cholesky decomposition
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
		
		// for eigen decomposition
		/*double[][] L = new double[n_node][n_node];
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

		double[][] LL = L;
		for (int i = 0; i < n_node; i++) {
			double div = Math.sqrt(LL[i][i]);
			for (int j = 0; j < n_node; j++) {
				L[i][j] = L[i][j] / div;
			}
		}*/
		
		
		/*************** netSVM classifier*****************/
		
		double [] L_diag_arr= new double[L.length];
		Arrays.fill(L_diag_arr, 1);
		DiagonalMatrix L_diag_blk = new DiagonalMatrix(L_diag_arr);
		//if this does not work use below code to create diagonal matrix
		
		/*double[][] L_diag = new double[L.length][L[0].length];
		for (int i = 0; i < L_diag.length; i++) {
			for (int j = 0; j < L_diag[0].length; j++) {
				L_diag[i][j] = (i == j) ? 1 : 0;
			}
		}*/
		RealMatrix LL_blk = new BlockRealMatrix(LL);
		BlockRealMatrix L_blk = new BlockRealMatrix(L);
		//BlockRealMatrix L_diag_blk = new BlockRealMatrix(L_diag);
		LL_blk=L_blk.scalarMultiply(lambda).add(L_diag_blk);
		
		// eigen value decomposition
		/*EigenDecomposition E =new EigenDecomposition(LL_blk);
		double[] TT_Real =E.getRealEigenvalues();
		DiagonalMatrix TT = new DiagonalMatrix(TT_Real);
		RealMatrix UU=E.getV();
		
		double [][]TT_sqt= TT.getData();
		for(int i = 0; i < TT_sqt.length; i++){
			for(int j = 0; j < TT_sqt[i].length;j++){
				TT_sqt[i][j]=Math.sqrt(TT_sqt[i][j]);
			}
		}
		BlockRealMatrix TT_sqtBl = new BlockRealMatrix(TT_sqt);
		RealMatrix PP= UU.multiply(TT_sqtBl);
		RealMatrix xPP= new LUDecomposition(PP).getSolver().getInverse();
		RealMatrix betaPP_blk=PP;*/
		
		//cholesky decomposition
		CholeskyDecomposition PP_chol= new CholeskyDecomposition(LL_blk);
		RealMatrix PP= PP_chol.getL();
		RealMatrix xPP= new LUDecomposition(PP).getSolver().getInverse();
		RealMatrix betaPP_blk=PP;
		
		BlockRealMatrix trainX_blk = new BlockRealMatrix(trainX);
		RealMatrix nettrainX_blk= xPP.multiply(trainX_blk);
		BlockRealMatrix testX_blk = new BlockRealMatrix(testX);
		RealMatrix nettestX_blk= xPP.multiply(testX_blk);
		
		String[] TestOutputType = new String[] {"label"};
		T T_new = new T(TestOutputType);
		
		double [][] nettrainX = nettrainX_blk.getData();
		double [][] nettestX = nettestX_blk.getData();
		OVA_SVM ova_msvm2 = new OVA_SVM(nettrainX, trainy, T_new,nettestX);
		ova_msvm2.classify();
		MSVM model =ova_msvm2.getModel();
		double [] beta0 = model.LinearSVMNormVector;
		double[][] betaPP= betaPP_blk.getData();
		double [] beta = new double [betaPP.length];
		for(int i = 0;i<betaPP.length;i++){
			for(int j=0;j<betaPP[i].length;j++){
				beta[i]+=betaPP[i][j]*beta0[j];
			}
		}
		
		return beta;			
		
	}//end method
	
	public double[]  get_output(){
		return output;
	}

}// end class

