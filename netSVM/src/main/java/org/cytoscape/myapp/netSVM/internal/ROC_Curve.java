package org.cytoscape.myapp.netSVM.internal;

import java.util.LinkedList;

public class ROC_Curve {
	/****************************************** ROC CURVE FOR THE SAMPLES******************************/
	double [] rank_idx;
	int[] tlabel;
	float[] sen; float[] spe; float auc; float [] spe_roc;
	public ROC_Curve(double[] rank_idx, int[] tlabel) {
		// TODO Auto-generated constructor stub
		this.rank_idx=rank_idx;
		this.tlabel=tlabel;
	}
	
	public void get_value (){
		LinkedList<Double> t_earlyidx_list = new LinkedList<Double>();
		LinkedList<Double> t_lateidx = new LinkedList<Double>();
		for (int i=0;i<tlabel.length;i++){
			if(tlabel[i]==1)
				t_earlyidx_list.add((double) i);
			else
				t_lateidx.add((double) i);
		}
		int size_early=t_earlyidx_list.size();
		int size_late=t_lateidx.size();	
		sen = new float[rank_idx.length];
		spe = new float[rank_idx.length];
		spe_roc = new float[rank_idx.length];
		
		for(int i=0;i<rank_idx.length;i++){
			int counttp=0; int countfp=0; 
			for(int j=0;j<=i;j++){
				if(t_earlyidx_list.indexOf(rank_idx[j])!=-1)
					counttp++;
				else
					countfp++;
			}//end for
			sen[i] = (float) counttp / size_early;
	        spe[i] = (float)(1.0 - (float)countfp / size_late);
	        spe_roc[i]=1-spe[i];
	     }// end for
		//TrapezoidIntegrator trapezoid = new TrapezoidIntegrator();
		for (int k=1;k<rank_idx.length;k++){
			double increment = 0.5 * (1-spe_roc[k]-(1-spe_roc[k-1])) * (sen[k]+sen[k-1]);
			auc += increment;
		}
		
	}// end method

} // end class


