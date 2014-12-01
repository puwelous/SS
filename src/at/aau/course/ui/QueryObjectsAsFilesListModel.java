/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.aau.course.ui;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavol
 */
public class QueryObjectsAsFilesListModel extends javax.swing.AbstractListModel {

    List<File> queryObjectsAsFiles = new ArrayList<>();

    public QueryObjectsAsFilesListModel(List<File> queryObjects) {
        this.queryObjectsAsFiles = queryObjects;
    }

    @Override
    public int getSize() {
        return queryObjectsAsFiles.size();
    }

    @Override
    public File getElementAt(int index) {
        return queryObjectsAsFiles.get(index);
    }
    
    public boolean addElement(File newQueryObject){
        return queryObjectsAsFiles.add(newQueryObject);
    }

}
