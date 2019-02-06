package org.cytoscape.myapp.netSVM.internal;

import java.util.Arrays;
import libsvm.*;
//import libsvm.defpackage.svm_train;

//import java.util.ArrayList;
import java.util.LinkedHashSet;
//import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

public class OVA_SVM {
	double[][] trainX;
	int[] Y;
	T T_new;
	double[][] S;
	double[][] var_out,outerror;
	int n_classifier;
	int n_output;
	double[] dec_val;
	double[][] msvm_out;
	String[] output_type;
	MSVM[] msvm;
	double[] label;
	float acc,sen,spe;
	
	public OVA_SVM(double[][] trainX, int[] Y, T T_new, double[][] S) {
		this.trainX = trainX;
		this.Y = Y;
		this.T_new = T_new;
		this.S = S;
	}

	
public void classify() {
		
		//double [][] var_out = new double [S.length][];
		// find out unique class labels in Y
		Set<Integer> setUniqueNumbers = new LinkedHashSet<Integer>();
		for (int x : Y) {
			setUniqueNumbers.add(x);
		}

		n_output = T_new.TestOutputType.length;
		n_classifier = setUniqueNumbers.size();
		// for binary class
		if (n_classifier == 2)
			n_classifier = 1;

		// MSVM msvm[][] = new MSVM[n_classifier][1];
		msvm = new MSVM[n_classifier];
		msvm_out = new double[S[0].length][n_classifier];
		
		// normalize train data
		Normalize Norm1 = new Normalize(trainX);
		trainX = Norm1.normalize();
		double [] mean_train=Norm1.getMean();
		double [] std_train=Norm1.getSd();
		
		// normalize test data
		Normalize Norm2 = new Normalize(S);
		S = Norm2.normalize();
		double [] mean_test=Norm2.getMean();
		double [] std_test=Norm2.getSd();
		
		
		
		for(int j=1;j<=n_classifier;j++){
			
			/************************************************************ libsvm *****************************************************/
			
			//prepare the svm node
			svm_node[][] SVM_node_Train = new svm_node[trainX[0].length][trainX.length];

			for (int p = 0; p < trainX[0].length; p++) {
				for (int q = 0; q < trainX.length; q++) {
					SVM_node_Train[p][q] = new svm_node();
					SVM_node_Train[p][q].index = q+1;
					SVM_node_Train[p][q].value = trainX[q][p];
				}
			}
			
			//prepare svm problem
			double[] y_SVM = new double[Y.length];// for svm compatible
			for (int p = 0; p < Y.length; p++) {
				y_SVM[p] = Y[p];
			}
			svm_problem SVM_Prob = new svm_problem();
			SVM_Prob.l = trainX[0].length;
			SVM_Prob.x = SVM_node_Train;
			//SVM_Prob.y = biny_SVM;	
			SVM_Prob.y = y_SVM;
			
			
			//prepare svm parameter
			svm_parameter SVM_Param = new svm_parameter();
			SVM_Param.svm_type = 0;
			SVM_Param.kernel_type = 0;
			SVM_Param.cache_size = 100;
			SVM_Param.eps = 0.001;
			SVM_Param.C = 1;
			//SVM_Param.gamma = 0.5;
			//SVM_Param.probability=1;
			
			// check parameters
			String check =svm.svm_check_parameter(SVM_Prob, SVM_Param); 
			System.out.println("check ="+check);

			// train classifier
			svm_model test_model= svm.svm_train(SVM_Prob, SVM_Param);			
			
			
			/************************ get the training results of libsvm ******************************************************************************/

			msvm[0] = new MSVM();
			
			msvm[0].NumberOfSupportVectors = svm.svm_get_nr_sv(test_model);
			msvm[0].SupportVectorIDs = new int[msvm[0].NumberOfSupportVectors];
			svm.svm_get_sv_indices(test_model, msvm[0].SupportVectorIDs);
			svm_node[][] SV= test_model.SV;
			msvm[0].SupportVectors=new double [SV[0].length][SV.length];
			for(int ii=0;ii<SV.length;ii++){
				for(int jj=0;jj<SV[0].length;jj++){
					msvm[0].SupportVectors[jj][ii]=SV[ii][jj].value;
					msvm[0].SupportVectors[jj][ii]=(msvm[0].SupportVectors[jj][ii] * std_train[jj]) + mean_train[jj];
				}
			}
			msvm[0].SupportVectorWeights=test_model.sv_coef[0];
			msvm[0].LinearSVMNormVector = new double [msvm[0].SupportVectors.length];
			for (int ii=0;ii<msvm[0].SupportVectors.length;ii++){
				for (int jj=0;jj<msvm[0].SupportVectors[0].length;jj++){
					msvm[0].LinearSVMNormVector[ii] += (msvm[0].SupportVectors[ii][jj] * msvm[0].SupportVectorWeights[jj]);
				}
				
			}
			
			double sum = 0.0; 			 
            for (int i = 0; i < msvm[0].LinearSVMNormVector.length; i++) { 
                sum += msvm[0].LinearSVMNormVector[i] * msvm[0].LinearSVMNormVector[i]; 
            } 
            double norm_weight =Math.pow(sum, 0.5); 
            for (int i = 0; i < msvm[0].LinearSVMNormVector.length; i++) { 
            	msvm[0].LinearSVMNormVector[i]= msvm[0].LinearSVMNormVector[i]/norm_weight; 
            } 
			
			
			msvm[0].model=test_model;
			
			/********** predict test data with libsvm **********/

			svm_node[][] SVM_node_Test = new svm_node[S[0].length][S.length];
			for (int p = 0; p < S[0].length; p++) {
				for (int q = 0; q < S.length; q++) {
					SVM_node_Test[p][q] = new svm_node();
					SVM_node_Test[p][q].index = q+1;
					SVM_node_Test[p][q].value = S[q][p];
				}
			}

			label = new double[S[0].length];
			dec_val = new double[S[0].length];
			for (int c = 0; c < SVM_node_Test.length; c++) {
			//for (int c = 0; c < SVM_node_Train.length; c++) {
				//label[c] = svm.svm_predict(test_model,SVM_node_Train[c]);
				label[c] = svm.svm_predict(test_model,SVM_node_Test[c]);// class labels			
				svm.svm_predict_values(test_model, SVM_node_Test[c],msvm_out[c]);// considering only the first column of msvm_out array for binary classification
				dec_val[c]=msvm_out[c][0];// raw decision values
			}//end for (int c = 0; c < SVM_node_Test.length; c++) { 
			//j += 1;//for the while loop

		}// end for(int j=1;j<=n_classifier;j++)
		
	// end for (int k = 0; k < n_output; k++) {		
		//return var_out;
		
		
		
	}// end method
	
	public MSVM getModel(){
		// null var_out
		//var_out = null;
		return msvm[0];
	}
	
	public double[] getPredicty(){
		return label;
		//return outerror;
	}
	
	public double[] getDecVal(){
		return dec_val;
		
	}	

	private static void error(String string) {
		// TODO Auto-generated method stub

	}

}// end class OVA_SVM

class MSVM {
	public String KernelType;
	int PolynomialOrder;
	int GaussianWidth;
	double GaussianScale;
	int NumberOfSupportVectors;
	int[] SupportVectorIDs;
	int[] SupportVectorLabels;
	double[] SupportVectorWeights;
	double[][] SupportVectors;
	double Bias;
	double[] LinearSVMNormVector;
	svm_model model;
} // end class MSVM

class T {
	public String KernelType;
	String[] TestOutputType;

	// String TestOutputTyp;

	public T(String KernelType, String[] TestOutputType) {
		this.KernelType = KernelType;
		this.TestOutputType = TestOutputType;
	}

	public T(String[] TestOutputType) {
		this.TestOutputType = TestOutputType;
	}
}// end class T

