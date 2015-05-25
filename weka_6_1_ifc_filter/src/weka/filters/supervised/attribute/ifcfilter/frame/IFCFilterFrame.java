/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 * IFCFilterFrame.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.frame;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import weka.filters.supervised.attribute.ifcfilter.classtrransformation.SerializedNumericInstances;
import weka.filters.supervised.attribute.ifcfilter.classtrransformation.SerializedNominalInstances;

/**
 * Frame for the NLR visualization window.
 *
 * @author Cédric Graf
 */
public class IFCFilterFrame extends JFrame implements WindowListener {

    /**
     * Generates the frame for the NLR visualization window.
     *
     * @param serializedNumericInstances contains information to build a
     * visualization for numeric attributes.
     * @param serializedNominalInstances contains information to build a
     * visualization for nominal attributes.
     * @param targetVariable contains the name of the target attribute.
     */
    public IFCFilterFrame(ArrayList<SerializedNumericInstances> serializedNumericInstances, ArrayList<SerializedNominalInstances> serializedNominalInstances, String targetVariable) {

        addWindowListener(this);
        setVisible(true);
        JTabbedPane tabbedPane = new JTabbedPane();

        for (int i = 1; i < serializedNominalInstances.size(); i++) {
            String[] featureName = serializedNominalInstances.get(i).featureName;
            double[] featureValue = serializedNominalInstances.get(i).featureValue;

            //sorts by name
            String[] sortedFeatureNameByName = featureName.clone();
            double[] sortedFeatureValueByName = featureValue.clone();
            String[] sortedFeatureNameByValue = featureName.clone();
            double[] sortedFeatureValueByValue = featureValue.clone();

            Arrays.sort(sortedFeatureValueByValue);
            Arrays.sort(sortedFeatureNameByName);
            double[] sortedFeatureValueByString = featureValue.clone();

            sortedFeatureValueByString = sortDoubleByString(sortedFeatureNameByName, featureName, sortedFeatureValueByString);
            sortedFeatureNameByValue = sortStringByValue(sortedFeatureValueByValue, featureValue, sortedFeatureNameByValue);

            String[] columnNames = {"Value", "NLR"};
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Value", featureName);
            model.addColumn("NLR", convertDoubleToString(featureValue));
            JTable table = new JTable(model);
            JScrollPane scrollingTable = new JScrollPane(table);
            JTextArea text = new JTextArea();
            String textContent = "";

            String attributeName = serializedNominalInstances.get(i).attributeName;
            textContent = textContent + "case \n";
            for (int z = 0; z < featureName.length; z++) {
                textContent = textContent + "when " + attributeName + " = " + "' " + sortedFeatureNameByName[z] + "' then " + String.valueOf(sortedFeatureValueByString[z]) + "\n";
            }
            textContent = textContent + "end \n";
            text.setText(textContent);
            JScrollPane scrollingText = new JScrollPane(text);

            JComponent stringDraw = new NominalDrawingArea(sortedFeatureNameByValue, sortedFeatureValueByValue);
            JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, stringDraw, scrollingText);
            JSplitPane splitPaneHrozontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollingTable, splitPaneVertical);
            splitPaneHrozontal.setDividerLocation(250);
            splitPaneHrozontal.setBackground(Color.white);
            splitPaneVertical.setDividerLocation(300);
            splitPaneVertical.setBackground(Color.white);

            PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    JSplitPane sourceSplitPane = (JSplitPane) evt.getSource();
                    sourceSplitPane.setBackground(Color.white);
                    String propertyName = evt.getPropertyName();
                    if (propertyName.equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
                        int current = sourceSplitPane.getDividerLocation();
                        Integer last = (Integer) evt.getNewValue();
                        Integer priorLast = (Integer) evt.getOldValue();
                    }
                }
            };
            splitPaneHrozontal.addPropertyChangeListener(propertyChangeListener);
            tabbedPane.addTab(serializedNominalInstances.get(i).attributeName, splitPaneHrozontal);
        }

        /**
         * ****************************************************************************************
         */
        for (int i = 0; i < serializedNumericInstances.size(); i++) {
            double[] featureName = serializedNumericInstances.get(i).oldValue;
            double[] featureValue = serializedNumericInstances.get(i).newValue;
            //sorts by name
            //INFO uses distinct values instead of quantile averages
            CompressDoubleFeatureValue compressedFeatureValue = new CompressDoubleFeatureValue();
            compressedFeatureValue.set(featureName, featureValue);
            double[] a = compressedFeatureValue.getCompressedFeatureName();
            double[] b = compressedFeatureValue.getCompressedFeatureValue();

            double[][] tableNLR = serializedNumericInstances.get(i).nlr;
            //INFO this is the Array containing Piecewise Linear function approximation
            double[][] newTableNLR = getNumericNtilNLR(tableNLR);

            // add the following points: min, f(min); max, f(max); x1, 1/0; x2, 0/0
            double[][] tableNLR2 = addDrawingPoints(tableNLR, a, b);
            double[] compFeatureName = tableNLR2[0];
            double[] compFeatureValue = tableNLR2[1];
            double[] compFeatureType = tableNLR2[2];

            String[] columnNames = {"N-Til ", "NLR", "new Instances"};
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("quantile", convertDoubleToString(newTableNLR[0]));
            model.addColumn("avg quantile val", convertDoubleToString(newTableNLR[1]));
            model.addColumn("nlr", convertDoubleToString(newTableNLR[2]));
            JTable table = new JTable(model);
            JScrollPane scrollingTable = new JScrollPane(table);
            JTextArea text = new JTextArea();
            String textContent = "";

            String attributeName = serializedNumericInstances.get(i).attributeName;
            String numAttributeName = serializedNumericInstances.get(i).attributeName;
            textContent = textContent + "case \n";
            for (int z = 0; z < tableNLR[0].length; z++) {
                if (z == 0) {
                    if (tableNLR[0][0] != tableNLR[0][1]) {
                        textContent = textContent + "when " + numAttributeName + " < " + tableNLR[0][0] + " then " + numAttributeName + " * " + tableNLR[2][1] + " + " + tableNLR[3][1] + "\n";
                    }
                } else if (tableNLR[0][z] != tableNLR[0][z - 1]) {
                    if (tableNLR[0][z] != tableNLR[0][z - 1]) {
                        textContent = textContent + "when " + tableNLR[0][z - 1] + " <= " + numAttributeName + " < " + tableNLR[0][z] + " then " + numAttributeName + " * " + tableNLR[2][z] + " + " + tableNLR[3][z] + "\n";
                    }
                }
                if (z == tableNLR[0].length - 1) {
                    textContent = textContent + "when " + numAttributeName + " >= " + tableNLR[0][tableNLR[0].length - 1] + " then " + numAttributeName + " * " + tableNLR[2][tableNLR[0].length - 1] + " + " + tableNLR[3][tableNLR[0].length - 1] + "\n";
                }
            }
            textContent = textContent + "end \n";
            text.setText(textContent);
            JScrollPane scrollingText = new JScrollPane(text);
            JComponent stringDraw = new NumericDrawingArea(newTableNLR, compFeatureName, compFeatureValue, compFeatureType);
            stringDraw.setSize(300, 700);

            JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, stringDraw, scrollingText);
            JSplitPane splitPaneHrozontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollingTable, splitPaneVertical);
            splitPaneHrozontal.setDividerLocation(250);
            splitPaneHrozontal.setBackground(Color.white);
            splitPaneVertical.setDividerLocation(300);
            splitPaneVertical.setBackground(Color.white);
            PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
                /**
                 * Division of the panel in the different areas.
                 */
                public void propertyChange(PropertyChangeEvent evt) {
                    JSplitPane sourceSplitPane = (JSplitPane) evt.getSource();
                    sourceSplitPane.setBackground(Color.white);
                    String propertyName = evt.getPropertyName();
                    if (propertyName.equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
                        int current = sourceSplitPane.getDividerLocation();
                        Integer last = (Integer) evt.getNewValue();
                        Integer priorLast = (Integer) evt.getOldValue();
                    }
                }
            };

            splitPaneHrozontal.addPropertyChangeListener(propertyChangeListener);

            tabbedPane.addTab(attributeName, splitPaneHrozontal);
        }
        this.add(tabbedPane);
        this.setTitle("IFC-filter/target:" + targetVariable);
        setSize(1000, 550);
    }

    /**
     * Converts double array in string array
     *
     * @param doubleInstances input double array
     * @return output string array
     */
    private String[] convertDoubleToString(double[] doubleInstances) {
        String[] tmp = new String[doubleInstances.length];
        for (int i = 0; i < doubleInstances.length; i++) {
            tmp[i] = String.valueOf(doubleInstances[i]);
        }
        return tmp;
    }

    /**
     * Sorts string array with value by alphabetical order.
     *
     * @param newStringArray alphabetical sorted string array.
     * @param oldStringArray original string array.
     * @param toSort double array which original values.
     * @return double array which are sorted with the alphabetical ones.
     */
    private double[] sortDoubleByString(String[] newStringArray, String[] oldStringArray, double[] toSort) {
        double[] temp = new double[toSort.length];
        for (int i = 0; i < newStringArray.length; i++) {
            for (int a = 0; a < oldStringArray.length; a++) {
                if (newStringArray[i].equals(oldStringArray[a])) {
                    temp[i] = toSort[a];
                }
            }
        }
        return temp;
    }

    /**
     * Sorts double array in numeric order.
     *
     * @param newDoubleArray numerical sorted array.
     * @param oldDoubleArray orignial double array.
     * @param toSort string array which original values.
     * @return string array which are sorted with the numerical ones.
     */
    private String[] sortStringByValue(double[] newDoubleArray, double[] oldDoubleArray, String[] toSort) {
        String[] temp = new String[toSort.length];
        for (int i = 0; i < newDoubleArray.length; i++) {
            for (int a = 0; a < oldDoubleArray.length; a++) {
                if (newDoubleArray[i] == oldDoubleArray[a]) {
                    temp[i] = toSort[a];
                }
            }
        }
        return temp;
    }

    /**
     * Gets numeric NLR in order to print it as SQL.
     *
     * @param tableNLR double array conained in the serialized array list.
     * @return double array containing sorted NLR.
     */
    private double[][] getNumericNtilNLR(double[][] tableNLR) {
        int counter = 0;
        for (int z = 0; z < tableNLR[0].length; z++) {
            if (z == 0) {
                if (tableNLR[0][0] != tableNLR[0][1]) {
                    //counter++;
                }
            } else if (tableNLR[0][z] != tableNLR[0][z - 1]) {
                if (tableNLR[0][z] != tableNLR[0][z - 1]) {
                    counter++;
                }
            }
            if (z == tableNLR[0].length - 1) {
                counter++;
            }
        }
        double[][] numericNtilNLR = new double[3][counter];
        counter = 0;
        for (int z = 0; z < tableNLR[0].length; z++) {
            if (z == 0) {
                if (tableNLR[0][0] != tableNLR[0][1]) {
                }
            } else if (tableNLR[0][z] != tableNLR[0][z - 1]) {
                if (tableNLR[0][z] != tableNLR[0][z - 1]) {
                    numericNtilNLR[0][counter] = tableNLR[5][z - 1];
                    numericNtilNLR[1][counter] = tableNLR[0][z - 1];
                    numericNtilNLR[2][counter] = tableNLR[1][z - 1];

                    counter++;
                }
            }
            if (z == tableNLR[0].length - 1) {
                numericNtilNLR[0][counter] = tableNLR[5][tableNLR[0].length - 1];
                numericNtilNLR[1][counter] = tableNLR[0][tableNLR[0].length - 1];
                numericNtilNLR[2][counter] = tableNLR[1][tableNLR[1].length - 1];
                counter++;
            }
        }
        return numericNtilNLR;
    }

    /**
     * Reacts to window events.
     *
     * @param event WindowEvent
     */
    public void windowClosing(WindowEvent event) {
        this.dispose();
    }

    public void windowClosed(WindowEvent event) {
    }

    public void windowDeiconified(WindowEvent event) {
    }

    public void windowIconified(WindowEvent event) {
    }

    public void windowActivated(WindowEvent event) {
    }

    public void windowDeactivated(WindowEvent event) {
    }

    public void windowOpened(WindowEvent event) {
    }

    private double[][] addDrawingPoints(double[][] tableNLR, double[] compressedFeatureName, double[] compressedFeatureValue) {
        new Date();
        double min = compressedFeatureName[0];
        double fmin = compressedFeatureValue[0];
        double min01;
        double fmin01;
        double min01A = tableNLR[2][1];
        double min01B = tableNLR[3][1];
        if (min01A >= 0) {
            min01 = (-1 * min01B / min01A);
            fmin01 = 0;
        } else {
            min01 = (1 - min01B) / min01A;
            fmin01 = 1;
        }
        double max01;
        double fmax01;
        double max01A = tableNLR[2][tableNLR[2].length - 1];
        double max01B = tableNLR[3][tableNLR[3].length - 1];
        if (max01A < 0) {
            max01 = (-1 * max01B / max01A);
            fmax01 = 0;
        } else {
            max01 = (1 - max01B) / max01A;
            fmax01 = 1;
        }
        int add = 2;
        double max = compressedFeatureName[compressedFeatureName.length - 1];
        double fmax = compressedFeatureValue[compressedFeatureName.length - 1];
        if (min < min01) {
            add++;
        }
        if (max > max01) {
            add++;
        }
        double[][] r = new double[3][tableNLR[0].length + add];
        int offset = 0;
        if (min < min01) {
            r[0][0] = min;
            r[1][0] = fmin;
            r[0][1] = min01;
            r[1][1] = fmin01;
            offset = 2;
        } else {
            r[0][0] = min;
            r[1][0] = fmin;
            offset = 1;
        }
        for(int i=0;i<tableNLR[0].length; i++) {
            r[0][i+offset] =tableNLR[0][i];
            r[1][i+offset] =tableNLR[1][i];
            r[2][i+offset] = 1;
        }
        if (max > max01) {
            r[0][offset+tableNLR[0].length] = max01;
            r[1][offset+tableNLR[0].length] = fmax01;
            r[0][offset+tableNLR[0].length+1] = max;
            r[1][offset+tableNLR[0].length+1] = fmax01;
        } else {
            r[0][offset+tableNLR[0].length] = max;
            r[1][offset+tableNLR[0].length] = fmax;
        }

        return r;
    }
}
