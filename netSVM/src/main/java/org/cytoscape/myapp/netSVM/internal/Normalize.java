package org.cytoscape.myapp.netSVM.internal;

public class Normalize {
	double[][] data;
	double [] mean;
	double [] std;
	
	public Normalize(double[][] data) {
		this.data = data;
	}

	public double[][] normalize() {
		int rows = data.length;
		int cols = data[0].length;
		
		mean = new double[rows];
		for (int i = 0; i < rows; i++) {
			double sum=0.0;
			for (int j = 0; j < data[i].length; j++) {
				sum += data[i][j];
			}
			mean[i] = sum/data[i].length;
		}
		
		std = new double[rows];
		for (int i = 0; i < rows; i++) {
			double temp=0.0;
			for (int j = 0; j < data[i].length; j++) {
				temp += (data[i][j] - mean[i]) * (data[i][j] - mean[i]);
			}
			std[i] = Math.sqrt(temp/ (data[i].length - 1));
		}
		
		double[][] trainX = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < data[i].length; j++) {
				// sub_mean[i][j]=data[i][j]-mean[i];
				trainX[i][j] = (data[i][j] - mean[i]) / std[i];				
			}
		}
		return trainX;
	}// end normalize
	
	double [] getMean(){
		return mean;
	}
	
	double [] getSd(){
		return std;
	}

}

