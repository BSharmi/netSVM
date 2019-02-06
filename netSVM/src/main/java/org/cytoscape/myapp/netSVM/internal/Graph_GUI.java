package org.cytoscape.myapp.netSVM.internal;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Graph_GUI extends ApplicationFrame{
	private String title;
	private static final long serialVersionUID = 1L;

	public Graph_GUI(String title) {
		super(title);
		this.title = title;
	}

	public ChartPanel display(float[] spe_ROC, float[] sen_ROC) {
		// Create a simple XY chart
		XYSeries series = new XYSeries("netSVM");
		for(int i = 0; i < spe_ROC.length; i++){
			series.add(spe_ROC[i],sen_ROC[i]);
		}

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		// Generate the graph
		JFreeChart chart = ChartFactory.createXYLineChart(title, // Title
				"1-spe", // x-axis Label
				"sen", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				false, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(270, 270));
		setContentPane(chartPanel);
		return chartPanel;
		/*pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);*/
		/*try {
				ChartUtilities.saveChartAsJPEG(new File("E:\\chart.jpg"), chart,
						500, 300);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Problem occurred creating chart.");
			}*/

	}
	
}// end class

