/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.aau.course.ui;

import at.aau.course.VectorData;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavol
 */
public class QueryObjectsListModel extends javax.swing.AbstractListModel {

    List<VectorData> queryObjects = new ArrayList<>();

    public QueryObjectsListModel(List<VectorData> queryObjects) {
        this.queryObjects = queryObjects;
    }

    @Override
    public int getSize() {
        return queryObjects.size();
    }

    @Override
    public Object getElementAt(int index) {
        return queryObjects.get(index);
    }
    
    public boolean addElement(VectorData newQueryObject){
        return queryObjects.add(newQueryObject);
    }

}
