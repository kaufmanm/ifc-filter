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
 * BinaryFilterOuput.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.classtrransformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.supervised.attribute.IFC_Filter;
import weka.filters.supervised.attribute.ifcfilter.nominalalgorithme.IFCNominalAlgorithm;
import weka.filters.supervised.attribute.ifcfilter.numericalgorithme.IFCModel;
import weka.filters.supervised.attribute.ifcfilter.numericalgorithme.IFCNumericAlgorithm;

/**
 * Sends instances to the super class and transforms instances from the super
 * class in instances with fuzzy values and binary target values.
 *
 * @author Michael Kaufmann, Cédric Graf
 */
public class BinaryFilterOuput extends FilterOutput {

    /**
     * for serialization
     */
    static final long serialVersionUID = -7069148179905814324L;
    //private IFCNumericAlgorithm doubleCross;
    //merke dir das "Modell" pro attribut

    //att-index x ... x ... -> ...
    public double[][] nlr;
    //att-index x ... x ... -> ...
    public double[][] values;
    // private IFCNominalAlgorithm stringCross;
    //att-index x ... x ... -> ...
    public double[] value;
    //att-index x ... x ... -> ...
    public String[] featureName;

    public Hashtable<Object, Integer> featureCode;

    HashMap<Integer, IFCModel> models = new HashMap<Integer, IFCModel>();
    private final IFC_Filter ifcFilter;

    public BinaryFilterOuput(IFC_Filter aThis) {
        this.ifcFilter = aThis;
    }

    /**
     * Sends instances to the super class and transforms instances from the
     * super class in instances with fuzzy values and binary target values.
     *
     * @param instances instances containing the datas.
     * @param serializedNumericInstances array list containing numeric attribute
     * values.
     * @param serializedNominalInstances array list containing categorical
     * attribute values.
     * @param classValue string containing the target value.
     * @param percentOfDataSet percentage of data in an n-tile.
     * @return transformed instances containing fuzzy values and binary target
     * values.
     */
    public Instances set(Instances instances, ArrayList<SerializedNumericInstances> serializedNumericInstances,
            ArrayList<SerializedNominalInstances> serializedNominalInstances, String classValue, String percentOfDataSet, boolean isFirstBatchDone) {
        super.setClassInstances(instances, serializedNumericInstances, serializedNominalInstances, classValue, percentOfDataSet);
        if (percentOfDataSet.contains("%")) {
            percentOfDataSet = percentOfDataSet.substring(0, percentOfDataSet.length() - 1);
        }
        double percent = Double.valueOf(percentOfDataSet);

        instances = super.instances;

        double[][] attValue = new double[2][numInst];

        for (int i = 0; i < numAttr - 1; i++) {

//System.out.println(i);
            if (values == null) {
                System.out.print("");
            }
            if (i == 3) {
                System.out.print("");
            }

            Attribute attr = instances.attribute(0);
            boolean b = attr.isNumeric();

            if (instances
                    .attribute(0)
                    .isNumeric()
                    && (!isFirstBatchDone || models
                    .get(i).values
                    != null)) {

                if (!isFirstBatchDone) {
                    attValue[0] = numClassInstances.clone();
                }

                attValue[1] = instances.get(0).dataset().attributeToDoubleArray(0);//Erstes Attribute ClassenzughÃ¶rigkeit

                //attValue[1] = numClassInstances.clone();
                //attValue[0] = instances.get(0).dataset().attributeToDoubleArray(0);//Erstes Attribute ClassenzughÃ¶rigkeit
                if (!isFirstBatchDone) {
                    IFCNumericAlgorithm doubleCross = new IFCNumericAlgorithm(this.ifcFilter, i, attr);
                    doubleCross.setCrossTable(attValue, percent);
                    nlr = doubleCross.getNLR();
                    values = doubleCross.getCrossTable();
                    models.put(new Integer(i), new IFCModel(nlr, values, null, null, null, "numeric"));
                }
                nlr = models.get(i).nlr;
                values = models.get(i).values;
                instances.insertAttributeAt(new Attribute(instances.attribute(0).name()), numAttr);
                for (int x = 0; x < numInst; x++) {
                    if (values == null) {
                        System.out.print("");
                    }
                    for (int y = 0; y < values[2].length; y++) {
                        if (values[0][y] == instances.get(x).value(0)) {
                            instances.get(x).setValue(numAttr, values[2][y]);
                            break;
                        }
                    }
                }
                double[][] l_values = values;
                double[][] l_nlr = nlr;
                SerializedNumericInstances numericInstances = new SerializedNumericInstances(l_nlr, attValue[1], l_values[2], instances.attribute(0).name(), "normalAttribute");
                serializedNumericInstances.add(numericInstances);
                instances.deleteAttributeAt(0);
            } else if (instances.attribute(0).isString() || (isFirstBatchDone && models.get(i).type.equals("string"))) {

                String[][] stringInstances = new String[2][numInst];
                if (!isFirstBatchDone) {
                    attValue[0] = numClassInstances.clone();
                    for (int a = 0; a < numInst; a++) {
                        stringInstances[0][a] = String.valueOf(attValue[0][a]);
                    }
                } else {
                    for (int a = 0; a < numInst; a++) {
                        stringInstances[0][a] = "?";
                    }
                }

                for (int a = 0; a < numInst; a++) {
                    stringInstances[1][a] = instances.get(a).stringValue(0);

                }

                if (!isFirstBatchDone) {
                    // INFO call nominal algorithm
                    IFCNominalAlgorithm stringCross = new IFCNominalAlgorithm();
                    stringCross.setCrossTable(stringInstances, true);
                    value = stringCross.getfuzzyTable();
                    featureName = stringCross.getfeatureName();
                    featureCode = instances.attribute(0).m_Hashtable;
                    models.put(new Integer(i), new IFCModel(null, null, value, featureName, featureCode, "nominal"));
                }
                value = models.get(i).value;
                featureName = models.get(i).featureName;
                featureCode = models.get(i).featureCode;

                double[] l_value = value;
                String[] l_featureName = featureName;

                instances.insertAttributeAt(new Attribute(instances.attribute(0).name()), numAttr);
                double[] newInstanceValue = new double[numInst];
                for (int a = 0; a < numInst; a++) {
                    for (int x = 0; x < featureName.length; x++) {
                        if (instances.instance(a).value(0) == featureCode.get(featureName[x])) {
                            instances.instance(a).setValue(numAttr, value[x]);
                            newInstanceValue[a] = value[x];
                        }
                    }
                }
                SerializedNominalInstances stringInstances2 = new SerializedNominalInstances(l_featureName, l_value, stringInstances[1], newInstanceValue, instances.attribute(0).name(), "normalAttribute");
                serializedNominalInstances.add(stringInstances2);
                instances.deleteAttributeAt(0);
            } else if (instances.attribute(0).isNominal() || (isFirstBatchDone && models.get(i).type.equals("nominal"))) {
                String[][] stringInstances = new String[2][numInst];

                if (!isFirstBatchDone) {
                    attValue[0] = numClassInstances.clone();
                    for (int a = 0; a < numInst; a++) {
                        stringInstances[0][a] = String.valueOf(attValue[0][a]);
                    }
                    for (int a = 0; a < numInst; a++) {
                        stringInstances[1][a] = instances.get(a).stringValue(0);
                    }
                } else {
                    for (int a = 0; a < numInst; a++) {
                        stringInstances[0][a] = "?";
                    }
                    for (int a = 0; a < numInst; a++) {
                        Instance inst = instances.get(a);
                        if (!instances.attribute(0).isNominal()) {
                            System.out.print("");
                        }
                        stringInstances[1][a] = "?";
                    }

                }

                if (!isFirstBatchDone) {
                    IFCNominalAlgorithm stringCross = new IFCNominalAlgorithm();
                    stringCross.setCrossTable(stringInstances, true);
                    value = stringCross.getfuzzyTable();
                    featureName = stringCross.getfeatureName();
                    featureCode = instances.attribute(0).m_Hashtable;
                    models.put(new Integer(i), new IFCModel(null, null, value, featureName, featureCode, "nominal"));
                }
                value = models.get(i).value;
                featureName = models.get(i).featureName;
                featureCode = models.get(i).featureCode;

                double[] l_value = value;
                String[] l_featureName = featureName;
                instances.insertAttributeAt(new Attribute(instances.attribute(0).name()), numAttr);
                double[] newInstanceValue = new double[numInst];
                for (int a = 0; a < numInst; a++) {
                    for (int x = 0; x < featureName.length; x++) {
                        if (instances.instance(a).value(0) == featureCode.get(featureName[x])) { // hier auf Codes switchen
                            instances.instance(a).setValue(numAttr, value[x]);
                            newInstanceValue[a] = value[x];
                        }
                    }
                }
                SerializedNominalInstances stringInstances2 = new SerializedNominalInstances(l_featureName, l_value, stringInstances[1], newInstanceValue, instances.attribute(0).name(), "normalAttribute");
                serializedNominalInstances.add(stringInstances2);
                instances.deleteAttributeAt(0);

            }
        }

        for (int a = 0; !isFirstBatchDone && a < instances.numInstances(); a++) {
            numClassInstances[a] = instances.get(a).value(0);
        }
        instances.insertAttributeAt(new Attribute(instances.attribute(0).name()), numAttr);
        for (int a = 0; !isFirstBatchDone && a < numClassInstances.length; a++) {
            instances.get(a).setValue(numAttr, numClassInstances[a]);
        }
        instances.setClassIndex(-1);
        instances.deleteAttributeAt(0);
        instances.setClassIndex(numAttr - 1);

        return instances;
    }

}
