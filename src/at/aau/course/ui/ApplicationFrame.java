package at.aau.course.ui;

import at.aau.course.Task;
import at.aau.course.VectorData;
import at.aau.course.distance.IDistance;
import at.aau.course.distance.LpNorm;
import at.aau.course.distance_space.RankedResult;
import at.aau.course.extractor.ColorHistogramLab;
import at.aau.course.extractor.EdgeExtractor;
import at.aau.course.extractor.GrayScaleHistogram;
import at.aau.course.extractor.HSVExtractor;
import at.aau.course.extractor.IDescriptorWrapper;
import at.aau.course.physics_model.PhysicsModel;
import at.aau.course.ui.particle_physics.PhysicsModelThread;
import at.aau.course.util.environment.EnvironmentPreparationUnit;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ApplicationFrame extends javax.swing.JFrame implements MouseListener {

    EnvironmentPreparationUnit epu;
    DefaultListModel listModel;
    final JFileChooser fc;
    List<IDescriptorWrapper> extractors;
    LpNorm[] allLpNorms;

    HashMap<String, List<VectorData>> descriptsMapped2Name = null;

    public ApplicationFrame() {

        //*********** LOGIC *************
        // environment unit
        epu = new EnvironmentPreparationUnit();
        // query objects
        List<File> queryObjects = initQueryObjects();
        // extractors
        extractors = initDescriptors();
        //LpNorm
        allLpNorms = initLpNorm();

        //*********** UI *************
        initComponents();
        fc = new JFileChooser(epu.getInputDir());
        //initQueryObjectsList(queryObjects);
        initAllDescriptorsLists(extractors);
        initDescriptorsComboBoxes(extractors);
        initLpNormComboBoxes(allLpNorms);

        listModel = (DefaultListModel) this.UI_QueryObjectsList.getModel();

        initDisable();

        //UI_descriptorsList.addListSelectionListener(this);
        UI_descriptorsList.addMouseListener(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        UI_PanelDescriptorsGeneration = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        UI_descriptorsList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        UI_GenerateDescriptorsButton = new javax.swing.JButton();
        UI_LoadDescriptorsFromFileButton = new javax.swing.JButton();
        UI_ShowPhysicsModelButton = new javax.swing.JButton();
        UI_ComputeMapButton = new javax.swing.JButton();
        UI_QueryObjectPanel = new javax.swing.JPanel();
        UI_QueryObjectPath = new javax.swing.JTextField();
        UI_chooseQueryObjectButton = new javax.swing.JButton();
        UI_addQueryObjectButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        UI_QueryObjectsList = new javax.swing.JList();
        UI_ChooseQueryObjectLabel = new javax.swing.JLabel();
        UI_ActualQueryObjectLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        UI_2ndFeatureDescriptorPanel = new javax.swing.JPanel();
        UI_2ndFeatureExtractorComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        UI_2ndLpNormComboBox = new javax.swing.JComboBox();
        UI_2ndFeatureExtractorShowButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        UI_2ndFeatureDescriptorImagePanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        UI_2ndDescriptorPanelRangeTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        UI_2ndDescriptorPanelCountSpinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        UI_2ndDescriptorPanelRangeRadioButton = new javax.swing.JRadioButton();
        UI_2ndDescriptorPanelCountRadioButton = new javax.swing.JRadioButton();
        UI_1stFeatureDescriptorPanel = new javax.swing.JPanel();
        UI_1stFeatureExtractorComboBox = new javax.swing.JComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        UI_1stLpNormComboBox = new javax.swing.JComboBox();
        UI_1stFeatureExtractorShowButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        UI_1stFeatureDescriptorImagePanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        UI_1stDescriptorPanelRangeTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        UI_1stDescriptorPanelRangeRadioButton = new javax.swing.JRadioButton();
        UI_1stDescriptorPanelCountRadioButton = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        UI_1stDescriptorPanelCountSpinner = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Similarity Search Project");

        jScrollPane1.setViewportView(UI_descriptorsList);

        jLabel1.setText("Choose descriptors:");

        UI_GenerateDescriptorsButton.setText("Generate");
        UI_GenerateDescriptorsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_GenerateDescriptorsButtonActionPerformed(evt);
            }
        });

        UI_LoadDescriptorsFromFileButton.setText("Load from files");
        UI_LoadDescriptorsFromFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_LoadDescriptorsFromFileButtonActionPerformed(evt);
            }
        });

        UI_ShowPhysicsModelButton.setText("Show Physics Model");
        UI_ShowPhysicsModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_ShowPhysicsModelButtonActionPerformed(evt);
            }
        });

        UI_ComputeMapButton.setText("Compute AVG precision");
        UI_ComputeMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_ComputeMapButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UI_PanelDescriptorsGenerationLayout = new javax.swing.GroupLayout(UI_PanelDescriptorsGeneration);
        UI_PanelDescriptorsGeneration.setLayout(UI_PanelDescriptorsGenerationLayout);
        UI_PanelDescriptorsGenerationLayout.setHorizontalGroup(
            UI_PanelDescriptorsGenerationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_PanelDescriptorsGenerationLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(UI_GenerateDescriptorsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_LoadDescriptorsFromFileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(UI_ComputeMapButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UI_ShowPhysicsModelButton))
            .addComponent(jScrollPane1)
        );
        UI_PanelDescriptorsGenerationLayout.setVerticalGroup(
            UI_PanelDescriptorsGenerationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_PanelDescriptorsGenerationLayout.createSequentialGroup()
                .addGroup(UI_PanelDescriptorsGenerationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(UI_GenerateDescriptorsButton)
                    .addComponent(UI_LoadDescriptorsFromFileButton)
                    .addComponent(UI_ShowPhysicsModelButton)
                    .addComponent(UI_ComputeMapButton))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        UI_QueryObjectPath.setText("Path to a query object");

        UI_chooseQueryObjectButton.setText("Choose query object");
        UI_chooseQueryObjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_chooseQueryObjectButtonActionPerformed(evt);
            }
        });

        UI_addQueryObjectButton.setText("Add query object");
        UI_addQueryObjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_addQueryObjectButtonActionPerformed(evt);
            }
        });

        UI_QueryObjectsList.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(UI_QueryObjectsList);

        UI_ChooseQueryObjectLabel.setText("Query object:");

        UI_ActualQueryObjectLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UI_ActualQueryObjectLabel.setText("Actual query object");
        UI_ActualQueryObjectLabel.setToolTipText("");
        UI_ActualQueryObjectLabel.setPreferredSize(new java.awt.Dimension(194, 146));

        javax.swing.GroupLayout UI_QueryObjectPanelLayout = new javax.swing.GroupLayout(UI_QueryObjectPanel);
        UI_QueryObjectPanel.setLayout(UI_QueryObjectPanelLayout);
        UI_QueryObjectPanelLayout.setHorizontalGroup(
            UI_QueryObjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_QueryObjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UI_QueryObjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(UI_QueryObjectPanelLayout.createSequentialGroup()
                        .addComponent(UI_ChooseQueryObjectLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UI_chooseQueryObjectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UI_QueryObjectPath)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(UI_QueryObjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UI_addQueryObjectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(UI_QueryObjectPanelLayout.createSequentialGroup()
                        .addComponent(UI_ActualQueryObjectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        UI_QueryObjectPanelLayout.setVerticalGroup(
            UI_QueryObjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_QueryObjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UI_QueryObjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UI_ChooseQueryObjectLabel)
                    .addComponent(UI_chooseQueryObjectButton)
                    .addComponent(UI_QueryObjectPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UI_addQueryObjectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(UI_QueryObjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(UI_ActualQueryObjectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        UI_2ndFeatureExtractorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("LpNorm:");

        UI_2ndLpNormComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        UI_2ndFeatureExtractorShowButton.setText("Show");
        UI_2ndFeatureExtractorShowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_2ndFeatureExtractorShowButtonActionPerformed(evt);
            }
        });

        UI_2ndFeatureDescriptorImagePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        UI_2ndFeatureDescriptorImagePanel.setForeground(new java.awt.Color(240, 240, 240));
        UI_2ndFeatureDescriptorImagePanel.setPreferredSize(new java.awt.Dimension(0, 144));
        UI_2ndFeatureDescriptorImagePanel.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane4.setViewportView(UI_2ndFeatureDescriptorImagePanel);

        jLabel7.setText("Range:");

        UI_2ndDescriptorPanelRangeTextField.setColumns(3);
        UI_2ndDescriptorPanelRangeTextField.setText("3000");

        jLabel8.setText("Count (kNN):");

        UI_2ndDescriptorPanelCountSpinner.setValue((int)8);

        jLabel9.setText("Lookup by:");

        buttonGroup2.add(UI_2ndDescriptorPanelRangeRadioButton);
        UI_2ndDescriptorPanelRangeRadioButton.setText("Range");

        buttonGroup2.add(UI_2ndDescriptorPanelCountRadioButton);
        UI_2ndDescriptorPanelCountRadioButton.setSelected(true);
        UI_2ndDescriptorPanelCountRadioButton.setText("Count");

        javax.swing.GroupLayout UI_2ndFeatureDescriptorPanelLayout = new javax.swing.GroupLayout(UI_2ndFeatureDescriptorPanel);
        UI_2ndFeatureDescriptorPanel.setLayout(UI_2ndFeatureDescriptorPanelLayout);
        UI_2ndFeatureDescriptorPanelLayout.setHorizontalGroup(
            UI_2ndFeatureDescriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_2ndFeatureDescriptorPanelLayout.createSequentialGroup()
                .addComponent(UI_2ndFeatureExtractorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_2ndLpNormComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_2ndDescriptorPanelRangeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_2ndDescriptorPanelCountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_2ndDescriptorPanelRangeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_2ndDescriptorPanelCountRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UI_2ndFeatureExtractorShowButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3))
            .addComponent(jScrollPane4)
        );
        UI_2ndFeatureDescriptorPanelLayout.setVerticalGroup(
            UI_2ndFeatureDescriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_2ndFeatureDescriptorPanelLayout.createSequentialGroup()
                .addGroup(UI_2ndFeatureDescriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UI_2ndFeatureExtractorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(UI_2ndLpNormComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UI_2ndFeatureExtractorShowButton)
                    .addComponent(jLabel7)
                    .addComponent(UI_2ndDescriptorPanelRangeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(UI_2ndDescriptorPanelCountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(UI_2ndDescriptorPanelRangeRadioButton)
                    .addComponent(UI_2ndDescriptorPanelCountRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        UI_1stFeatureExtractorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("LpNorm:");

        UI_1stLpNormComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        UI_1stFeatureExtractorShowButton.setText("Show");
        UI_1stFeatureExtractorShowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UI_1stFeatureExtractorShowButtonActionPerformed(evt);
            }
        });

        UI_1stFeatureDescriptorImagePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        UI_1stFeatureDescriptorImagePanel.setPreferredSize(new java.awt.Dimension(0, 144));
        UI_1stFeatureDescriptorImagePanel.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane3.setViewportView(UI_1stFeatureDescriptorImagePanel);

        jLabel4.setText("Range:");

        UI_1stDescriptorPanelRangeTextField.setColumns(3);
        UI_1stDescriptorPanelRangeTextField.setText("3000");

        jLabel5.setText("Count (kNN):");

        buttonGroup1.add(UI_1stDescriptorPanelRangeRadioButton);
        UI_1stDescriptorPanelRangeRadioButton.setText("Range");

        buttonGroup1.add(UI_1stDescriptorPanelCountRadioButton);
        UI_1stDescriptorPanelCountRadioButton.setSelected(true);
        UI_1stDescriptorPanelCountRadioButton.setText("Count");

        jLabel6.setText("Lookup by:");

        UI_1stDescriptorPanelCountSpinner.setValue((int)8);

        javax.swing.GroupLayout UI_1stFeatureDescriptorPanelLayout = new javax.swing.GroupLayout(UI_1stFeatureDescriptorPanel);
        UI_1stFeatureDescriptorPanel.setLayout(UI_1stFeatureDescriptorPanelLayout);
        UI_1stFeatureDescriptorPanelLayout.setHorizontalGroup(
            UI_1stFeatureDescriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_1stFeatureDescriptorPanelLayout.createSequentialGroup()
                .addComponent(UI_1stFeatureExtractorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_1stLpNormComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_1stDescriptorPanelRangeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_1stDescriptorPanelCountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_1stDescriptorPanelRangeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_1stDescriptorPanelCountRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_1stFeatureExtractorShowButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator3)
            .addComponent(jScrollPane3)
        );
        UI_1stFeatureDescriptorPanelLayout.setVerticalGroup(
            UI_1stFeatureDescriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UI_1stFeatureDescriptorPanelLayout.createSequentialGroup()
                .addGroup(UI_1stFeatureDescriptorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UI_1stFeatureExtractorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(UI_1stLpNormComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UI_1stFeatureExtractorShowButton)
                    .addComponent(jLabel4)
                    .addComponent(UI_1stDescriptorPanelRangeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(UI_1stDescriptorPanelRangeRadioButton)
                    .addComponent(UI_1stDescriptorPanelCountRadioButton)
                    .addComponent(jLabel6)
                    .addComponent(UI_1stDescriptorPanelCountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UI_QueryObjectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(UI_PanelDescriptorsGeneration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(UI_2ndFeatureDescriptorPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(UI_1stFeatureDescriptorPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(UI_PanelDescriptorsGeneration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_QueryObjectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(UI_1stFeatureDescriptorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UI_2ndFeatureDescriptorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UI_GenerateDescriptorsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_GenerateDescriptorsButtonActionPerformed

        List<IDescriptorWrapper> descriptionWrappers = (List<IDescriptorWrapper>) this.UI_descriptorsList.getSelectedValuesList();

        if (descriptionWrappers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feature extractor (descriptor) selected!");
            return;
        }

        try {
            //Task newTask = new Task(descriptionWrappers, fileGenerationRequired, computeDistanceSpace);            
            this.epu.prepareEnvironment(true, descriptionWrappers);
            Task newTask = new Task();
            newTask.setExtractors(descriptionWrappers);
            newTask.setEnvironment(this.epu);
            newTask.generateDescriptors();

            this.descriptsMapped2Name = newTask.getVectorDataMappedToDescriptors();

            enableQuerySection();
            UI_ComputeMapButton.setEnabled(true);
            UI_ShowPhysicsModelButton.setEnabled(true);

            JOptionPane.showMessageDialog(this, "Descriptors generation finished!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_UI_GenerateDescriptorsButtonActionPerformed

    private void UI_LoadDescriptorsFromFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_LoadDescriptorsFromFileButtonActionPerformed
        List<IDescriptorWrapper> descriptionWrappers = (List<IDescriptorWrapper>) this.UI_descriptorsList.getSelectedValuesList();

        if (descriptionWrappers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feature extractor (descriptor) selected!");
            return;
        }

        try {
            //Task newTask = new Task(descriptionWrappers, fileGenerationRequired, computeDistanceSpace);            
            this.epu.prepareEnvironment(false, descriptionWrappers);
            Task newTask = new Task();
            newTask.setExtractors(descriptionWrappers);
            newTask.setEnvironment(this.epu);
            newTask.reloadDescriptors();

            this.descriptsMapped2Name = newTask.getVectorDataMappedToDescriptors();

            System.out.print("Size: ");
            System.out.println(descriptsMapped2Name.size());

            System.out.println("**************************");

            for (Map.Entry<String, List<VectorData>> entry : descriptsMapped2Name
                    .entrySet()) {
                String key = entry.getKey();
                List<VectorData> list = entry.getValue();
                System.out.println("Key: " + key);
                System.out.println("List size: " + list.size());
                System.out.println("**************");
            }

            enableQuerySection();
            UI_ComputeMapButton.setEnabled(true);
            UI_ShowPhysicsModelButton.setEnabled(true);

            JOptionPane.showMessageDialog(this, "Loading descriptors finished!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_UI_LoadDescriptorsFromFileButtonActionPerformed

    private void UI_addQueryObjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_addQueryObjectButtonActionPerformed
        File selectedFile = new File(this.UI_QueryObjectPath.getText());
        try {
            if (!selectedFile.exists()) {
                throw new IOException("File does not exist!");
            }
            this.listModel.addElement(selectedFile);
            this.UI_QueryObjectsList.setSelectedIndex(this.listModel.indexOf(selectedFile));

            // add visualization
            BufferedImage queryObjectPicture;
            queryObjectPicture = ImageIO.read(selectedFile);
            UI_ActualQueryObjectLabel.setIcon(new ImageIcon(queryObjectPicture));

            // enable UI
            UI_1stFeatureDescriptorPanel.setEnabled(true);
            for (Component c : UI_1stFeatureDescriptorPanel.getComponents()) {
                c.setEnabled(true);
            }

            UI_2ndFeatureDescriptorPanel.setEnabled(true);
            for (Component c : UI_2ndFeatureDescriptorPanel.getComponents()) {
                c.setEnabled(true);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "File " + selectedFile.getAbsolutePath() + " does not exist!");
        }
    }//GEN-LAST:event_UI_addQueryObjectButtonActionPerformed

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

    private void UI_1stFeatureExtractorShowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_1stFeatureExtractorShowButtonActionPerformed
        // reset
        this.UI_1stFeatureDescriptorImagePanel.removeAll();

        List<IDescriptorWrapper> descriptionWrappers = (List<IDescriptorWrapper>) this.UI_descriptorsList.getSelectedValuesList();

        if (descriptionWrappers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feature extractor (descriptor) selected!");
            return;
        }

        try {
            Task newRankingTask = new Task();
            newRankingTask.setVectorDataMappedToDescriptors(this.descriptsMapped2Name);

            LpNorm LpNorm = new LpNorm((double) this.UI_1stLpNormComboBox.getSelectedItem());
            String extractorFileName = ((IDescriptorWrapper) this.UI_1stFeatureExtractorComboBox.getSelectedItem()).getFileName();
            File queryObjectFile = (File) this.listModel.elementAt(this.UI_QueryObjectsList.getSelectedIndex());

            List<File> queryObjectsFile = new ArrayList<File>();
            queryObjectsFile.add(queryObjectFile);

            RankedResult[] results = null;
            if (this.UI_1stDescriptorPanelCountRadioButton.isSelected()) {
                //this.UI_1stDescriptorPanelCountSpinner.getValue();
                results = newRankingTask.searchForSimilarQueryObjectsBykNN(queryObjectsFile, extractorFileName, LpNorm, (int) this.UI_1stDescriptorPanelCountSpinner.getValue());
            } else {
                results = newRankingTask.searchForSimilarQueryObjectsByRange(queryObjectsFile, extractorFileName, LpNorm, Double.parseDouble(UI_1stDescriptorPanelRangeTextField.getText()));
            }

            for (int i = 0; i < results.length; i++) {
                RankedResult rankedResult = results[i];

                File rankedResultFile = new File(
                        this.epu.getInputDir()
                        + System.getProperty("file.separator")
                        + String.valueOf(rankedResult.getVectorData().getClassId())
                        + System.getProperty("file.separator") + rankedResult.getVectorData().getFileName()
                );

                if (!rankedResultFile.exists()) {
                    System.out.println("Ranked file " + rankedResultFile.getAbsolutePath() + " does not exist! ");
                    i--;
                    continue;
                }

                System.out.println("Rendering file " + rankedResultFile.getAbsolutePath() + " with a distance " + rankedResult.getDistance());

                BufferedImage myPicture = ImageIO.read(rankedResultFile);
                JLabel picLabel = new JLabel(new ImageIcon(myPicture));

                this.UI_1stFeatureDescriptorImagePanel.add(picLabel, BorderLayout.CENTER);
            }

            this.validate();
            this.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_UI_1stFeatureExtractorShowButtonActionPerformed

    private void UI_2ndFeatureExtractorShowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_2ndFeatureExtractorShowButtonActionPerformed
        // reset
        this.UI_2ndFeatureDescriptorImagePanel.removeAll();

        List<IDescriptorWrapper> descriptionWrappers = (List<IDescriptorWrapper>) this.UI_descriptorsList.getSelectedValuesList();

        if (descriptionWrappers == null || descriptionWrappers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feature extractor (descriptor) selected!");
            return;
        }

        try {
            Task newRankingTask = new Task();
            newRankingTask.setVectorDataMappedToDescriptors(this.descriptsMapped2Name);

            LpNorm LpNorm = new LpNorm((double) this.UI_2ndLpNormComboBox.getSelectedItem());
            String extractorFileName = ((IDescriptorWrapper) this.UI_2ndFeatureExtractorComboBox.getSelectedItem()).getFileName();
            File queryObjectFile = (File) this.listModel.elementAt(this.UI_QueryObjectsList.getSelectedIndex());

            List<File> queryObjectsFile = new ArrayList<File>();
            queryObjectsFile.add(queryObjectFile);

            RankedResult[] results = null;

            if (this.UI_2ndDescriptorPanelCountRadioButton.isSelected()) {
                //this.UI_1stDescriptorPanelCountSpinner.getValue();
                results = newRankingTask.searchForSimilarQueryObjectsBykNN(queryObjectsFile, extractorFileName, LpNorm, (int) this.UI_2ndDescriptorPanelCountSpinner.getValue());
            } else {
                results = newRankingTask.searchForSimilarQueryObjectsByRange(queryObjectsFile, extractorFileName, LpNorm, Double.parseDouble(UI_2ndDescriptorPanelRangeTextField.getText()));
            }

            System.out.println("--->" + results.length);
            //System.out.println(UI_2ndFeatureDescriptorImagePanel.getComponentCount());
            for (int i = 0; i < results.length; i++) {
                RankedResult rankedResult = results[i];

                File rankedResultFile = new File(
                        this.epu.getInputDir()
                        + System.getProperty("file.separator")
                        + String.valueOf(rankedResult.getVectorData().getClassId())
                        + System.getProperty("file.separator") + rankedResult.getVectorData().getFileName()
                );

                if (!rankedResultFile.exists()) {
                    i--;
                    continue;
                }

                System.out.println("Rendering file " + rankedResultFile.getAbsolutePath() + " with a distance " + rankedResult.getDistance());

                BufferedImage myPicture = ImageIO.read(rankedResultFile);
                JLabel picLabel = new JLabel(new ImageIcon(myPicture));

                this.UI_2ndFeatureDescriptorImagePanel.add(picLabel, BorderLayout.CENTER);
            }

            this.validate();
            this.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_UI_2ndFeatureExtractorShowButtonActionPerformed

    private void UI_ShowPhysicsModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_ShowPhysicsModelButtonActionPerformed

        if (this.descriptsMapped2Name == null || this.descriptsMapped2Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feature extractor (descriptor) selected!");
            return;
        }

        PhysicsModel physicsModel = new PhysicsModel();
//        physicsModel.setWidthBound(1280);
//      TODO: automatic full screen:
        physicsModel.setWidthBound(1280);
        physicsModel.setHeightBound(800);

        //IDistance distance = new LpNorm(2);
        VectorData[] objectsPerFeature = null;

        List<VectorData> vectorDataAsList = new ArrayList<VectorData>();

        for (Map.Entry<String, List<VectorData>> entry : this.descriptsMapped2Name.entrySet()) {
            vectorDataAsList.addAll(entry.getValue());
        }

        objectsPerFeature = vectorDataAsList.toArray(new VectorData[vectorDataAsList.size()]);

        //Point[] coordinates = physicsModel.computeCoordinates(distance, objectsPerFeature);
        try {
            //new PhysicsModelUI( coordinates, objectsPerFeature, this.epu.getInputDir() );
            //new Application(coordinates, objectsPerFeature, this.epu.getInputDir());
            PhysicsModelThread physicsModelThread = new PhysicsModelThread(physicsModel, objectsPerFeature, this.epu.getInputDir());
            new Thread(physicsModelThread).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_UI_ShowPhysicsModelButtonActionPerformed

    private void UI_ComputeMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UI_ComputeMapButtonActionPerformed

        if (this.descriptsMapped2Name == null || this.descriptsMapped2Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feature extractor (descriptor) selected!");
            return;
        }

        Object[][] tableData = null;
        String[] columnNames = null;
        int actualIndex;
        List<VectorData> selectedQueryObjects;

        tableData = new Object[descriptsMapped2Name.size()][allLpNorms.length + 1];

        columnNames = new String[this.allLpNorms.length + 1]; // "MAP" + all feature extraction techniques

        // load list of files to put into a query selection
        Task loadCVFileTask = new Task();
        List<File> loadedFiles;
        try {
            loadCVFileTask.setEnvironment(this.epu);
            loadedFiles = loadCVFileTask.loadFilenamesFromCSV();
            if (loadedFiles.isEmpty()) {
                throw new Exception("CSV file contains no entries???");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            return;
        }

        // initialize column names
        columnNames[0] = "MAP";
        for (int cnindex = 1; cnindex < columnNames.length; cnindex++) {
            columnNames[cnindex] = this.allLpNorms[cnindex - 1].getName();
        }

        // initialize whole first column:
        actualIndex = 0;
        for (Map.Entry<String, List<VectorData>> entry : descriptsMapped2Name.entrySet()) {
            String descriptorFileName = entry.getKey();
            tableData[actualIndex][0] = descriptorFileName;
            actualIndex++;
        }
        System.out.println("actualIndex:" + actualIndex);
        for (int i = 0; i < tableData.length; i++) {

            System.out.println("tableData[i][0]:" + tableData[i][0]);
            List<VectorData> l_allVectorsPerDesc = descriptsMapped2Name.get(tableData[i][0]);

            //TODO: conversion of list of files to calculated or loaded VectorData[]
            selectedQueryObjects = new ArrayList<VectorData>(loadedFiles.size());
            for (File queryFile : loadedFiles) {
                for (VectorData singleQuery : l_allVectorsPerDesc) {
                    if (queryFile.getName().equalsIgnoreCase(singleQuery.getFileName())) {
                        selectedQueryObjects.add(singleQuery);
                        break;
                    }
                }
            }

            System.out.println("selectedQueyObjects' size:" + selectedQueryObjects.size());

            for (int j = 1; (j < tableData[i].length); j++) { // skip first column, descriptor names have already been set!

                IDistance iDistance = (IDistance) this.allLpNorms[j - 1]; // starting with index 1 but 1st LpNorm is on index 0
                final double avgPrecision;

                Task avgPrecisionTask = new Task();
                avgPrecisionTask.setEnvironment(this.epu);

                avgPrecision = new Task().computeMapForDistanceSpace(
                        selectedQueryObjects.toArray(new VectorData[selectedQueryObjects.size()]),
                        l_allVectorsPerDesc.toArray(new VectorData[l_allVectorsPerDesc.size()]),
                        iDistance);
                tableData[i][j] = avgPrecision;
//                tableData[i][j] = 1.1;

            }

            System.out.println(Arrays.toString(tableData[i]));

            selectedQueryObjects = null;
        }

//        for (DistanceSpace distanceSpace : allDistanceSpaces) {
//            
//        }
        JTable table = new JTable(tableData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        System.out.println("Done!");

        JFrame frame = new JFrame();
        frame.setTitle("Calculated average precisions");
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(this.getWidth(), this.getHeight() / 2);
        frame.setVisible(true);
    }//GEN-LAST:event_UI_ComputeMapButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton UI_1stDescriptorPanelCountRadioButton;
    private javax.swing.JSpinner UI_1stDescriptorPanelCountSpinner;
    private javax.swing.JRadioButton UI_1stDescriptorPanelRangeRadioButton;
    private javax.swing.JTextField UI_1stDescriptorPanelRangeTextField;
    private javax.swing.JPanel UI_1stFeatureDescriptorImagePanel;
    private javax.swing.JPanel UI_1stFeatureDescriptorPanel;
    private javax.swing.JComboBox UI_1stFeatureExtractorComboBox;
    private javax.swing.JButton UI_1stFeatureExtractorShowButton;
    private javax.swing.JComboBox UI_1stLpNormComboBox;
    private javax.swing.JRadioButton UI_2ndDescriptorPanelCountRadioButton;
    private javax.swing.JSpinner UI_2ndDescriptorPanelCountSpinner;
    private javax.swing.JRadioButton UI_2ndDescriptorPanelRangeRadioButton;
    private javax.swing.JTextField UI_2ndDescriptorPanelRangeTextField;
    private javax.swing.JPanel UI_2ndFeatureDescriptorImagePanel;
    private javax.swing.JPanel UI_2ndFeatureDescriptorPanel;
    private javax.swing.JComboBox UI_2ndFeatureExtractorComboBox;
    private javax.swing.JButton UI_2ndFeatureExtractorShowButton;
    private javax.swing.JComboBox UI_2ndLpNormComboBox;
    private javax.swing.JLabel UI_ActualQueryObjectLabel;
    private javax.swing.JLabel UI_ChooseQueryObjectLabel;
    private javax.swing.JButton UI_ComputeMapButton;
    private javax.swing.JButton UI_GenerateDescriptorsButton;
    private javax.swing.JButton UI_LoadDescriptorsFromFileButton;
    private javax.swing.JPanel UI_PanelDescriptorsGeneration;
    private javax.swing.JPanel UI_QueryObjectPanel;
    private javax.swing.JTextField UI_QueryObjectPath;
    private javax.swing.JList UI_QueryObjectsList;
    private javax.swing.JButton UI_ShowPhysicsModelButton;
    private javax.swing.JButton UI_addQueryObjectButton;
    private javax.swing.JButton UI_chooseQueryObjectButton;
    protected javax.swing.JList UI_descriptorsList;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables

    private void initAllDescriptorsLists(List<IDescriptorWrapper> extractors) {
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

        // adding EdgeExtractor
        extractors.add(new EdgeExtractor());

        for (int i = 0; i < quantinizations.length; i++) {
            for (int j = 0; j < dimensions.length; j++) {

                // adding HSVExtractor
                extractors.add((IDescriptorWrapper) new HSVExtractor(
                        dimensions[j],
                        quantinizations[i]));
            }
        }
        
        // adding Lab color extractor
        extractors.add(new ColorHistogramLab());
        
        return extractors;
    }

    private List<File> initQueryObjects() {
        // no init, return empty array(list)
        return new ArrayList<>();
    }

    private void initDescriptorsComboBoxes(List<IDescriptorWrapper> extractors) {
        UI_1stFeatureExtractorComboBox.setModel(
                new javax.swing.DefaultComboBoxModel(extractors.toArray(new IDescriptorWrapper[extractors.size()]))
        );

        UI_2ndFeatureExtractorComboBox.setModel(
                new javax.swing.DefaultComboBoxModel(extractors.toArray(new IDescriptorWrapper[extractors.size()]))
        );
    }

    private LpNorm[] initLpNorm() {

        return new LpNorm[]{
            new LpNorm(1.0), new LpNorm(2.0), new LpNorm(5.0), new LpNorm(0.5)};
    }

    private void initLpNormComboBoxes(LpNorm[] LpNorms) {

        Double[] LpNormValues = new Double[LpNorms.length];

        for (int i = 0; i < LpNormValues.length; i++) {
            LpNormValues[i] = LpNorms[i].getP();
        }

        UI_1stLpNormComboBox.setModel(
                new javax.swing.DefaultComboBoxModel(LpNormValues)
        );
        UI_2ndLpNormComboBox.setModel(
                new javax.swing.DefaultComboBoxModel(LpNormValues)
        );
    }

    public static void main(String[] args) {
        ApplicationFrame af = new ApplicationFrame();
        Task loadCVFileTask = new Task();
        loadCVFileTask.setEnvironment(af.epu);

        try {
            List<File> loadedFiles = loadCVFileTask.loadFilenamesFromCSV();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            e.printStackTrace();
            return;
        }
    }

    private void initDisable() {
        UI_ComputeMapButton.setEnabled(false);
        UI_ShowPhysicsModelButton.setEnabled(false);
        UI_QueryObjectPanel.setEnabled(false);

        UI_GenerateDescriptorsButton.setEnabled(false);
        UI_LoadDescriptorsFromFileButton.setEnabled(false);

        for (Component c : UI_QueryObjectPanel.getComponents()) {
            c.setEnabled(false);
        }

        UI_1stFeatureDescriptorPanel.setEnabled(false);
        for (Component c : UI_1stFeatureDescriptorPanel.getComponents()) {
            c.setEnabled(false);
        }

        UI_2ndFeatureDescriptorPanel.setEnabled(false);
        for (Component c : UI_2ndFeatureDescriptorPanel.getComponents()) {
            c.setEnabled(false);
        }
    }

    private void enableQuerySection() {

        UI_QueryObjectPanel.setEnabled(true);
        for (Component c : UI_QueryObjectPanel.getComponents()) {
            c.setEnabled(true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (UI_descriptorsList.getSelectedValuesList().isEmpty()) {
            this.initDisable();
        } else {
            UI_GenerateDescriptorsButton.setEnabled(true);
            UI_LoadDescriptorsFromFileButton.setEnabled(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }
}
