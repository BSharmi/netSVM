package org.cytoscape.myapp.netSVM.internal;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetSVMDataPanel extends JPanel {
	// cross validation
	public File currentDir;
	public JLabel jLabel_file_early_idx;
	public JLabel jLabel_file_late_idx;
	public JLabel jLabel_file_exp_data;
	public JLabel jLabel_file_ppi;
	public JLabel jLabel_file_gid;
	public JLabel jLabel_file_network;	
	public JTextField jTextField_file_early_idx;
	public JTextField jTextField_file_late_idx;
	public JTextField jTextField_file_exp_data;
	public JTextField jTextField_file_ppi;
	public JTextField jTextField_file_gid;
	public JTextField jTextField_file_network;
	public JButton jButton_file_early_idx;
	public JButton	jButton_file_late_idx;
	public JButton jButton_file_exp_data;
	public JButton jButton_file_ppi;
	public JButton jButton_file_gid;
	public JButton jButton_file_network;
	public JPanel boxPanel;
	public JPanel jPanel_Input_cv;
	public JPanel jPanel_Input_indeptest;
	
	//independent test
	public JLabel jLabel_file_early_idx_indeptest;
	public JLabel jLabel_file_late_idx_indeptest;
	public JLabel jLabel_file_exp_data_indeptest;
	public JTextField jTextField_file_early_idx_indeptest;
	public JTextField jTextField_file_late_idx_indeptest;
	public JTextField jTextField_file_exp_data_indeptest;
	public JButton jButton_file_early_idx_indeptest;
	public JButton jButton_file_late_idx_indeptest;
	public JButton jButton_file_exp_data_indeptest;
	
	
	// other panel reference
		private NetSVMConfigurePanel NetSVMConfigurePanel = null;
		private NetSVMParameterPanel parameterPanel = null; 
		
		public NetSVMDataPanel(NetSVMParameterPanel parameterPanel, 
				NetSVMConfigurePanel NetSVMConfigurePanel) {
			this.parameterPanel = parameterPanel;
			this.NetSVMConfigurePanel = NetSVMConfigurePanel;
			
			// fields for cross-validation
			jLabel_file_early_idx = new javax.swing.JLabel();
			jLabel_file_late_idx = new javax.swing.JLabel();
			jLabel_file_exp_data = new javax.swing.JLabel();
			jLabel_file_ppi = new javax.swing.JLabel();
			jLabel_file_gid = new javax.swing.JLabel();
			jLabel_file_network = new javax.swing.JLabel();
			jTextField_file_early_idx = new javax.swing.JTextField();
	    	jTextField_file_late_idx = new javax.swing.JTextField();
	    	jTextField_file_exp_data = new javax.swing.JTextField();
	    	jTextField_file_ppi = new javax.swing.JTextField();
	    	jTextField_file_gid = new javax.swing.JTextField();
	    	jTextField_file_network = new javax.swing.JTextField();
	    	jButton_file_early_idx = new javax.swing.JButton();
	    	jButton_file_late_idx = new javax.swing.JButton();
	    	jButton_file_exp_data = new javax.swing.JButton();
	    	jButton_file_ppi = new javax.swing.JButton();
	    	jButton_file_gid = new javax.swing.JButton();
	    	jButton_file_network = new javax.swing.JButton();
	    	
	    	// fields for independent test
	    	jLabel_file_early_idx_indeptest = new javax.swing.JLabel();
			jLabel_file_late_idx_indeptest = new javax.swing.JLabel();
			jLabel_file_exp_data_indeptest = new javax.swing.JLabel();
			jTextField_file_early_idx_indeptest = new javax.swing.JTextField();
	    	jTextField_file_late_idx_indeptest = new javax.swing.JTextField();
	    	jTextField_file_exp_data_indeptest = new javax.swing.JTextField();
	    	jButton_file_early_idx_indeptest = new javax.swing.JButton();
	    	jButton_file_late_idx_indeptest = new javax.swing.JButton();
	    	jButton_file_exp_data_indeptest = new javax.swing.JButton();
	    	
	    	// combine cv and independent
	    	boxPanel = new javax.swing.JPanel();
	    	
	    	
	    	// set up panel for cv
	    	jPanel_Input_cv = new javax.swing.JPanel();
	    	jPanel_Input_cv.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder
					(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 12), 
					new java.awt.Color(0, 102, 102)), "Input files for cross validation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, 
					new java.awt.Font("Georgia", 1, 12), new java.awt.Color(0, 102, 102)));
	    	
	    	// set up panel for indep test
	    	jPanel_Input_indeptest = new javax.swing.JPanel();
	    	jPanel_Input_indeptest.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder
					(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 12), 
					new java.awt.Color(0, 102, 102)), "Input files for independent test", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, 
					new java.awt.Font("Georgia", 1, 12), new java.awt.Color(0, 102, 102)));
	    	
	    	// set fields for cross validation
	    	jLabel_file_early_idx.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_early_idx.setText("Group1:");        
	        jLabel_file_late_idx.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_late_idx.setText("Group2:");       
	        jLabel_file_exp_data.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_exp_data.setText("Gene expression data:");      
	        jLabel_file_ppi.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_ppi.setText("Protein-protein interaction data:");
	        jLabel_file_gid.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_gid.setText("Gene id:");
	        jLabel_file_network.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_network.setText("Protein Localization:");
	        
	        jTextField_file_early_idx.setMaximumSize(new java.awt.Dimension(6, 20)); 
	        jTextField_file_late_idx.setMaximumSize(new java.awt.Dimension(6, 20));
	    	jTextField_file_exp_data.setMaximumSize(new java.awt.Dimension(6, 20));
	    	jTextField_file_ppi.setMaximumSize(new java.awt.Dimension(6, 20));
	    	jTextField_file_gid.setMaximumSize(new java.awt.Dimension(6, 20));
	    	jTextField_file_network.setMaximumSize(new java.awt.Dimension(6, 20));
	    	
	    	
	    	jButton_file_early_idx.setText("Select");
	    	jButton_file_early_idx.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_EarlyActionPerformed(evt);
	    		}
	    	});    	
	    	jButton_file_late_idx.setText("Select");
	    	jButton_file_late_idx.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_LateActionPerformed(evt);
	    		}
	    	}); 	
	    	jButton_file_exp_data.setText("Select");
	    	jButton_file_exp_data.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_GexpActionPerformed(evt);
	    		}
	    	});   	
	    	jButton_file_ppi.setText("Select");
	    	jButton_file_ppi.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_PPIActionPerformed(evt);
	    		}
	    	});
	    	jButton_file_gid.setText("Select");
	    	jButton_file_gid.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_GIDActionPerformed(evt);
	    		}
	    	});
	    	jButton_file_network.setText("Select");
	    	jButton_file_network.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_id2SymbolActionPerformed(evt);
	    		}
	    	});
	    	
	    	
	    	// setup fields for independent test
	    	jLabel_file_early_idx_indeptest.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_early_idx_indeptest.setText("Group1:");        
	        jLabel_file_late_idx_indeptest.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_late_idx_indeptest.setText("Group2:");       
	        jLabel_file_exp_data_indeptest.setFont(new java.awt.Font("Tahoma", 0, 12));
	        jLabel_file_exp_data_indeptest.setText("Gene expression data:");      
	                
	        jTextField_file_early_idx_indeptest.setMaximumSize(new java.awt.Dimension(6, 20)); 
	        jTextField_file_late_idx_indeptest.setMaximumSize(new java.awt.Dimension(6, 20));
	    	jTextField_file_exp_data_indeptest.setMaximumSize(new java.awt.Dimension(6, 20));
	    	
	    	
	    	jButton_file_early_idx_indeptest.setText("Select");
	    	jButton_file_early_idx_indeptest.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_EarlyActionPerformed_indeptest(evt);
	    		}
	    	});    	
	    	jButton_file_late_idx_indeptest.setText("Select");
	    	jButton_file_late_idx_indeptest.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_LateActionPerformed_indeptest(evt);
	    		}
	    	}); 	
	    	jButton_file_exp_data_indeptest.setText("Select");
	    	jButton_file_exp_data_indeptest.addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) {
	    			jButton_GexpActionPerformed_indeptest(evt);
	    		}
	    	});   	
	    	    	
	    	
	    	// add  for cv panel
	    	javax.swing.GroupLayout InputLayout_cv = new javax.swing.GroupLayout(jPanel_Input_cv);
	    	jPanel_Input_cv.setLayout(InputLayout_cv);
	    	InputLayout_cv.setHorizontalGroup(
	    			InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	    				.addGroup(InputLayout_cv.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_early_idx, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_early_idx, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_early_idx,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGroup(InputLayout_cv.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_late_idx, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_late_idx, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_late_idx,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGroup(InputLayout_cv.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_exp_data, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_exp_data, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_exp_data,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGroup(InputLayout_cv.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_ppi, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_ppi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_ppi,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGroup(InputLayout_cv.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_gid, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_gid, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_gid,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGroup(InputLayout_cv.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_network, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_network, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_network,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGap(0, 21, Short.MAX_VALUE)
	    	);
	    	
	    	
	    	// add  for independent panel
	    	javax.swing.GroupLayout InputLayout_indeptest = new javax.swing.GroupLayout(jPanel_Input_indeptest);
	    	jPanel_Input_indeptest.setLayout(InputLayout_indeptest);
	    	InputLayout_indeptest.setHorizontalGroup(
	    			InputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	    				.addGroup(InputLayout_indeptest.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_early_idx_indeptest, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_early_idx_indeptest, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_early_idx_indeptest,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGroup(InputLayout_indeptest.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_late_idx_indeptest, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_late_idx_indeptest, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_late_idx_indeptest,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
	    				.addGroup(InputLayout_indeptest.createSequentialGroup()
	    						.addContainerGap()
	    						.addComponent(jLabel_file_exp_data_indeptest, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(18, 18, 18)
	    						.addComponent(jTextField_file_exp_data_indeptest, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
	    						.addGap(27, 27, 27)
	    						.addComponent(jButton_file_exp_data_indeptest,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))    				
	    				.addGap(0, 21, Short.MAX_VALUE)
	    	);
	    	
	    	// set group for cv
	    	InputLayout_cv.setVerticalGroup(
	    			InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	    			.addGroup(InputLayout_cv.createSequentialGroup()
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_early_idx)
	    							.addComponent(jTextField_file_early_idx)
	    							.addComponent(jButton_file_early_idx))
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_late_idx)
	    							.addComponent(jTextField_file_late_idx)
	    							.addComponent(jButton_file_late_idx))
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_exp_data)
	    							.addComponent(jTextField_file_exp_data)
	    							.addComponent(jButton_file_exp_data))
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_ppi)
	    							.addComponent(jTextField_file_ppi)
	    							.addComponent(jButton_file_ppi))
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_gid)
	    							.addComponent(jTextField_file_gid)
	    							.addComponent(jButton_file_gid))
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_network)
	    							.addComponent(jTextField_file_network)
	    							.addComponent(jButton_file_network))		
	    			)
	    	);
	    	
			//this.add(jPanel_Input_cv, BorderLayout.NORTH);
			
			
			// set group for independent test
			InputLayout_indeptest.setVerticalGroup(
					InputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	    			.addGroup(InputLayout_indeptest.createSequentialGroup()
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_early_idx_indeptest)
	    							.addComponent(jTextField_file_early_idx_indeptest)
	    							.addComponent(jButton_file_early_idx_indeptest))
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_late_idx_indeptest)
	    							.addComponent(jTextField_file_late_idx_indeptest)
	    							.addComponent(jButton_file_late_idx_indeptest))
	    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	    					.addGroup(InputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	    							.addComponent(jLabel_file_exp_data_indeptest)
	    							.addComponent(jTextField_file_exp_data_indeptest)
	    							.addComponent(jButton_file_exp_data_indeptest))    						
	    			)
	    	);
	    	
			//this.add(jPanel_Input_indeptest, BorderLayout.NORTH);
			
			//BoxLayout boxLayout = new BoxLayout(boxPanel, BoxLayout.Y_AXIS);
			//boxPanel.setLayout(boxLayout);
			//boxPanel.add(jPanel_Input_cv);
			//boxPanel.add(jPanel_Input_indeptest);
			//this.add(boxPanel, BorderLayout.WEST);
			Box right = Box.createVerticalBox();
			right.add(jPanel_Input_cv);
			right.add(jPanel_Input_indeptest);
			this.add(right, BorderLayout.NORTH);
			
		}
		
		// action for cv
		private void jButton_EarlyActionPerformed(java.awt.event.ActionEvent evt) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFileChooser.setCurrentDirectory(currentDir);
			int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = jFileChooser.getSelectedFile();
				currentDir = jFileChooser.getCurrentDirectory();
				jTextField_file_early_idx.setText(selectedFile.getPath());
			}
		}
		
		private void jButton_LateActionPerformed(java.awt.event.ActionEvent evt) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFileChooser.setCurrentDirectory(currentDir);
			int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = jFileChooser.getSelectedFile();
				currentDir = jFileChooser.getCurrentDirectory();
				jTextField_file_late_idx.setText(selectedFile.getPath());
			}
		}
		
		private void jButton_GexpActionPerformed(java.awt.event.ActionEvent evt) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFileChooser.setCurrentDirectory(currentDir);
			int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = jFileChooser.getSelectedFile();
				currentDir = jFileChooser.getCurrentDirectory();
				jTextField_file_exp_data.setText(selectedFile.getPath());
			}
		}
		
		private void jButton_PPIActionPerformed(java.awt.event.ActionEvent evt) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFileChooser.setCurrentDirectory(currentDir);
			int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = jFileChooser.getSelectedFile();
				currentDir = jFileChooser.getCurrentDirectory();
				jTextField_file_ppi.setText(selectedFile.getPath());
			}
		}
		
		private void jButton_GIDActionPerformed(java.awt.event.ActionEvent evt) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFileChooser.setCurrentDirectory(currentDir);
			int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = jFileChooser.getSelectedFile();
				currentDir = jFileChooser.getCurrentDirectory();
				jTextField_file_gid.setText(selectedFile.getPath());
			}
		}
		
		private void jButton_id2SymbolActionPerformed(java.awt.event.ActionEvent evt) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFileChooser.setCurrentDirectory(currentDir);
			int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = jFileChooser.getSelectedFile();
				currentDir = jFileChooser.getCurrentDirectory();
				jTextField_file_network.setText(selectedFile.getPath());
			}
		}
		
		
		// action for independent test
			private void jButton_EarlyActionPerformed_indeptest(java.awt.event.ActionEvent evt) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setCurrentDirectory(currentDir);
				int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File selectedFile = jFileChooser.getSelectedFile();
					currentDir = jFileChooser.getCurrentDirectory();
					jTextField_file_early_idx_indeptest.setText(selectedFile.getPath());
				}
			}
			
			private void jButton_LateActionPerformed_indeptest(java.awt.event.ActionEvent evt) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setCurrentDirectory(currentDir);
				int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File selectedFile = jFileChooser.getSelectedFile();
					currentDir = jFileChooser.getCurrentDirectory();
					jTextField_file_late_idx_indeptest.setText(selectedFile.getPath());
				}
			}
			
			private void jButton_GexpActionPerformed_indeptest(java.awt.event.ActionEvent evt) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setCurrentDirectory(currentDir);
				int returnVal = jFileChooser.showOpenDialog(NetSVMDataPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File selectedFile = jFileChooser.getSelectedFile();
					currentDir = jFileChooser.getCurrentDirectory();
					jTextField_file_exp_data_indeptest.setText(selectedFile.getPath());
				}
			}
		
	}
