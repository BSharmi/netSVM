package org.cytoscape.myapp.netSVM.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class OVA_SVM_indep {
	double[][] testX;
	int[] Y;
	T T_new;
	double[][] outerror;
	int n_classifier;
	//int n_output;
	double[] dec_val;
	double[][] msvm_out;
	String[] output_type;
	MSVM[] msvm;
	MSVM model;
	double[] label;
	float acc,sen,spe;
	
	public OVA_SVM_indep(double[][] testX, int[] Y, MSVM model) {
		this.testX = testX;
		this.Y = Y;
		this.model=model;
	}

	
public void classify() {
		
		//double [][] var_out = new double [testX.length][];
		// find out unique class labels in Y
		Set<Integer> setUniqueNumbers = new LinkedHashSet<Integer>();
		for (int x : Y) {
			setUniqueNumbers.add(x);
		}

		//n_output = T_new.TestOutputType.length;
		n_classifier = setUniqueNumbers.size();
		// for binary class
		if (n_classifier == 2)
			n_classifier = 1;

		// MSVM msvm[][] = new MSVM[n_classifier][1];
		msvm = new MSVM[n_classifier];
		msvm_out = new double[testX[0].length][n_classifier];
		
				
		// normalize test data
		Normalize Norm = new Normalize(testX);
		testX = Norm.normalize();
		double [] mean_test=Norm.getMean();
		double [] std_test=Norm.getSd();
		
		for(int j=1;j<=n_classifier;j++){
			
			
			/********** predict test data with libsvm **********/

			svm_node[][] SVM_node_Test = new svm_node[testX[0].length][testX.length];
			for (int p = 0; p < testX[0].length; p++) {
				for (int q = 0; q < testX.length; q++) {
					SVM_node_Test[p][q] = new svm_node();
					SVM_node_Test[p][q].index = q+1;
					SVM_node_Test[p][q].value = testX[q][p];
				}
			}
			
			svm_model test_model=model.model;
			label = new double[testX[0].length];
			dec_val = new double[testX[0].length];
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


}
