/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.aau.course.ui;

import at.aau.course.extractor.IDescriptorWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavol
 */
public class DescriptorsAbstractListModel extends javax.swing.AbstractListModel {

    List<IDescriptorWrapper> extractors = new ArrayList<>();

    public DescriptorsAbstractListModel(List<IDescriptorWrapper> extractors) {
        this.extractors = extractors;
    }

    @Override
    public int getSize() {
        return extractors.size();
    }

    @Override
    public Object getElementAt(int index) {
        return extractors.get(index);
    }

}
