/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import src.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author jjaikumar
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CacheLevelNameDropDown = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        AddressRangeDropDown = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        MemoryViewer = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Helvetica", 1, 13)); // NOI18N
        jLabel1.setText("CS 535 Architecture Simulator");

        CacheLevelNameDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L1 Cache", "L2 Cache", "DRAM" }));
        CacheLevelNameDropDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CacheLevelNameDropDownActionPerformed(evt);
            }
        });

        jLabel2.setText("Cache Level");

        jLabel3.setText("Address Range");

        AddressRangeDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        AddressRangeDropDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddressRangeDropDownActionPerformed(evt);
            }
        });

        jButton1.setText("Run");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                RunButton(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        MemoryViewer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Address", "Memory"
            }
        ));
        jScrollPane1.setViewportView(MemoryViewer);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CacheLevelNameDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AddressRangeDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CacheLevelNameDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(AddressRangeDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(435, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void RunButton(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RunButton
        // TODO add your handling code here:
        Driver driver = new Driver();
        // redraw table based on current values in pull down menus for cache and address range
    }//GEN-LAST:event_RunButton

    private void AddressRangeDropDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddressRangeDropDownActionPerformed
        // TODO add your handling code here:
        Object[][] memoryArraySegment;
        int memoryPage = 0;
        String s = (String) AddressRangeDropDown.getSelectedItem();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        while(m.find()) {
            System.out.println(m.group());
            memoryPage = Integer.parseInt(m.group());
        }
        if ("DRAM".equals((String) CacheLevelNameDropDown.getSelectedItem())) {
            memoryArraySegment = dm.getMemoryPage(cpu.MemorySet.DRAM.getMemArray(), memoryPage);
            MemoryViewer.setModel(new javax.swing.table.DefaultTableModel(
            memoryArraySegment,
            new String [] {"Address", "Instruction"}
            )
            {public boolean isCellEditable(int row, int column){return false;}}
            );
        }
        else if ("L2 Cache".equals((String) CacheLevelNameDropDown.getSelectedItem())) {
            memoryArraySegment = dm.getMemoryPage(cpu.MemorySet.L2Cache.getMemArray(), memoryPage);
            MemoryViewer.setModel(new javax.swing.table.DefaultTableModel(
            memoryArraySegment,
            new String [] {"Address", "Instruction"}
            )
            {public boolean isCellEditable(int row, int column){return false;}}
            ); 
        }
        else if ("L1 Cache".equals((String) CacheLevelNameDropDown.getSelectedItem())) {   
            memoryArraySegment = dm.getMemoryPage(cpu.MemorySet.L1Cache.getMemArray(), memoryPage);
            MemoryViewer.setModel(new javax.swing.table.DefaultTableModel(
            memoryArraySegment,
            new String [] {"Address", "Instruction"}
            )
            {public boolean isCellEditable(int row, int column){return false;}}
            );
        }
        
    }//GEN-LAST:event_AddressRangeDropDownActionPerformed

    private void CacheLevelNameDropDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CacheLevelNameDropDownActionPerformed
        if ("DRAM".equals((String) CacheLevelNameDropDown.getSelectedItem())) {
            AddressRangeDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(dm.getMemoryPageLabelsForMemoryType("DRAM"))); 
        }
        else if ("L2 Cache".equals((String) CacheLevelNameDropDown.getSelectedItem())) {
            AddressRangeDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(dm.getMemoryPageLabelsForMemoryType("L2Cache"))); 
        }
        else if ("L1 Cache".equals((String) CacheLevelNameDropDown.getSelectedItem())) {   
            AddressRangeDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(dm.getMemoryPageLabelsForMemoryType("L1Cache"))); 
        }
    }//GEN-LAST:event_CacheLevelNameDropDownActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    CPU cpu = new CPU();
    DisplayMemory dm = new DisplayMemory();
    // cpu.MemorySet.L1Cache.getMemArray();
    // cpu.MemorySet.L2Cache.getMemArray();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> AddressRangeDropDown;
    private javax.swing.JComboBox<String> CacheLevelNameDropDown;
    private javax.swing.JTable MemoryViewer;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
