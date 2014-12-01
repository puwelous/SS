package at.aau.course.ui;

import at.aau.course.Task;
import at.aau.course.distance_space.RankedResult;
import at.aau.course.extractor.GrayScaleHistogram;
import at.aau.course.extractor.IDescriptorWrapper;
import at.aau.course.util.environment.EnvironmentPreparationUnit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class ApplicationFrame extends javax.swing.JFrame {

    EnvironmentPreparationUnit epu;
    DefaultListModel listModel;
    final JFileChooser fc;

    public ApplicationFrame() {

        //*********** LOGIC *************
        // environment unit
        epu = new EnvironmentPreparationUnit();
        // query objects
        List<File> queryObjects = initQueryObjects();
        // extractors
        List<IDescriptorWrapper> extractors = initDescriptors();

        //*********** UI *************
        initComponents();
        fc = new JFileChooser(epu.getInputDir());
        //initQueryObjectsList(queryObjects);
        initDescriptorsList(extractors);

        listModel = (DefaultListModel) this.UI_QueryObjectsList.getModel();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        UI_ChooseQueryObjectLabel = new javax.swing.JLabel();
        UI_chooseQueryObjectButton = new javax.swing.JButton();
        UI_QueryObjectPath = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        UI_descriptorsList = new javax.swing.JList();
        UI_GenerateDescriptorFilesCHB = new javax.swing.JCheckBox();
        UI_ComputeDistanceCHB = new javax.swing.JCheckBox();
        UI_RunBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        UI_QueryObjectsList = new javax.swing.JList();
        UI_addQueryObjectButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Similarity Search Project");

        jLabel1.setText("Choose descriptors:");

        UI_ChooseQueryObjectLabel.setText("Query object:");

        UI_chooseQueryObjectButton.setText("Choose query object");
        UI_chooseQueryObjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_chooseQueryObjectButtonActionPerformed(evt);
            }
        });

        UI_QueryObjectPath.setText("Path to a query object");

        jScrollPane1.setViewportView(UI_descriptorsList);

        UI_GenerateDescriptorFilesCHB.setText("Generate descriptor files");

        UI_ComputeDistanceCHB.setText("Compute distance");

        UI_RunBtn.setText("RUN");
        UI_RunBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_RunBtnActionPerformed(evt);
            }
        });

        UI_QueryObjectsList.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(UI_QueryObjectsList);

        UI_addQueryObjectButton.setText("Add query object");
        UI_addQueryObjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_addQueryObjectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(UI_GenerateDescriptorFilesCHB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UI_ComputeDistanceCHB))
                    .addComponent(jScrollPane2)
                    .addComponent(UI_RunBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(UI_ChooseQueryObjectLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UI_chooseQueryObjectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UI_QueryObjectPath, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(UI_addQueryObjectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UI_ChooseQueryObjectLabel)
                    .addComponent(UI_chooseQueryObjectButton)
                    .addComponent(UI_QueryObjectPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UI_addQueryObjectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(UI_GenerateDescriptorFilesCHB)
                        .addComponent(UI_ComputeDistanceCHB)))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
                .addComponent(UI_RunBtn)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UI_chooseQueryObjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_chooseQueryObjectButtonActionPerformed

        if (evt.getSource() == UI_chooseQueryObjectButton) {
            int returnVal = fc.showOpenDialog(ApplicationFrame.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                //System.out.println("Seelcted query object:" + file.getName());
                this.UI_QueryObjectPath.setText(file.getAbsolutePath());
            }

        }
    }//GEN-LAST:event_UI_chooseQueryObjectButtonActionPerformed

    private void UI_RunBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_RunBtnActionPerformed

        boolean fileGenerationRequired = UI_GenerateDescriptorFilesCHB.isSelected();
        boolean computeDistanceSpace = this.UI_ComputeDistanceCHB.isSelected();

        List<IDescriptorWrapper> descriptionWrappers = (List<IDescriptorWrapper>) this.UI_descriptorsList.getSelectedValuesList();

        List<File> queryObjects = this.UI_QueryObjectsList.getSelectedValuesList();

        try {
            Task newTask = new Task(descriptionWrappers, fileGenerationRequired, computeDistanceSpace);

            this.epu.prepareEnvironment(fileGenerationRequired, descriptionWrappers);

            newTask.setEnvironment(this.epu); // has to be called before compute()

            RankedResult[] rankedResults = newTask.compute(queryObjects);
            File[] rankedResultsAsFiles = new File[rankedResults.length];
            int index = 0;

            for (RankedResult rankedResult : rankedResults) {
                String classIdAsString = String.valueOf(rankedResult.getVectorData().getClassId());

                rankedResultsAsFiles[index] = new File(
                        this.epu.getInputDir() + System.getProperty("file.separator") + classIdAsString + System.getProperty("file.separator") + rankedResult.getVectorData().getFileName());
                index++;
            }

            new RankedResultsUI(queryObjects.get(0),rankedResultsAsFiles).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_UI_RunBtnActionPerformed

    private void UI_addQueryObjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_addQueryObjectButtonActionPerformed
        File selectedFile = new File(this.UI_QueryObjectPath.getText());
        if (!selectedFile.exists()) {
            JOptionPane.showMessageDialog(this, "File " + selectedFile.getAbsolutePath() + " does not exist!");
            return;
        }

        this.listModel.addElement(selectedFile);
        this.UI_QueryObjectsList.setSelectedIndex(this.listModel.indexOf(selectedFile));
    }//GEN-LAST:event_UI_addQueryObjectButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel UI_ChooseQueryObjectLabel;
    protected javax.swing.JCheckBox UI_ComputeDistanceCHB;
    protected javax.swing.JCheckBox UI_GenerateDescriptorFilesCHB;
    private javax.swing.JTextField UI_QueryObjectPath;
    private javax.swing.JList UI_QueryObjectsList;
    private javax.swing.JButton UI_RunBtn;
    private javax.swing.JButton UI_addQueryObjectButton;
    private javax.swing.JButton UI_chooseQueryObjectButton;
    protected javax.swing.JList UI_descriptorsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    private void initDescriptorsList(List<IDescriptorWrapper> extractors) {
        this.UI_descriptorsList.setModel(new DescriptorsAbstractListModel(extractors));
    }

    private List<IDescriptorWrapper> initDescriptors() {

        List<IDescriptorWrapper> extractors;

        int[] quantinizations = new int[]{4, 8 /*, 16, 32, 64, 128 /*
         * ,
         * 256
         */}; // bins

        int[] dimensions = new int[]{1, 2, /*3, 4 */}; // blocks expressed
        // by
        // elements per
        // a row
        // (or per a
        // column)

        int numberOfDescriptors = dimensions.length
                * quantinizations.length;

        extractors = new ArrayList<>(
                numberOfDescriptors);

        for (int i = 0; i < quantinizations.length; i++) {
            for (int j = 0; j < dimensions.length; j++) {

                // gray scale descriptors
                extractors.add((IDescriptorWrapper) new GrayScaleHistogram(
                        dimensions[j],
                        quantinizations[i]));
            }
        }

        return extractors;
    }

//    private void initQueryObjectsList(List<File> queryObjects) {
//        //this.UI_QueryObjectsList.setModel(new QueryObjectsAsFilesListModel(queryObjects));
//
//        listModel = new DefaultListModel();
//        listModel.addElement("");
//        UI_QueryObjectsList = new JList(listModel);
//    }
    private List<File> initQueryObjects() {
        // no init, return empty array(list)
        return new ArrayList<>();
    }
}
