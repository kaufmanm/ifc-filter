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
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.filters.supervised.attribute.IFC_Filter;
import weka.filters.supervised.attribute.ifcfilter.nominalalgorithme.IFCNominalAlgorithm;
import weka.filters.supervised.attribute.ifcfilter.numericalgorithme.IFCNumericAlgorithm;

/**
 * Sends instances to the super class and transforms instances from the
 * super class in instances with fuzzy values and categorical target values.
 *
 * @author CÃ©dric Graf
 */
public class NominalFilterOutput extends FilterOutput {
    private final IFC_Filter ifcFilter;

    public NominalFilterOutput(IFC_Filter aThis) {
      this.ifcFilter = aThis;  
    }

    /**
     * Sends instances to the super class and transforms instances from the
     * super class in instances with fuzzy values and categorical target values.
     *
     * @param instances                     instances containing the datas.
     * @param serializedNumericInstances    array list containing numeric attribute values.
     * @param serializedNominalInstances    array list containing categorical attribute values.
     * @param classValue                    string containing the target value.
     * @param percentOfDataSet              percentage of data in an n-tile.
     * @return                              transformed instances containing fuzzy values and categorical target values.
     */
    public Instances set(Instances instances, ArrayList<SerializedNumericInstances> serializedNumericInstances, ArrayList<SerializedNominalInstances> serializedNominalInstances, String classValue, String percentOfDataSet, boolean isFirstBatchDone) {
        super.setClassInstances(instances, serializedNumericInstances, serializedNominalInstances, classValue, percentOfDataSet);
        instances = super.instances;
        if(percentOfDataSet.contains("%")){
            percentOfDataSet = percentOfDataSet.substring(0, percentOfDataSet.length()-1);
        }
        double percent = Double.valueOf(percentOfDataSet);

        double[][] attValue = new double[2][numInst];

        for(int i = 0; i < numAttr-1; i++){
            //FuzzyNtilCrossTable doubleCross;

            if(instances.attribute(0).isNumeric()){
                Attribute attr = instances.attribute(0);
                attValue[0] = numClassInstances.clone();

                attValue[1] = instances.get(0).dataset().attributeToDoubleArray(0);//Erstes Attribute ClassenzughÃ¶rigkeit
                IFCNumericAlgorithm doubleCross = new IFCNumericAlgorithm(this.ifcFilter, i, attr);
                doubleCross.setCrossTable(attValue, percent);
                double[][] nlr = doubleCross.getNLR();
                double [][] value = doubleCross.getCrossTable();
                instances.insertAttributeAt(new Attribute(instances.attribute(0).name()), numAttr);
                for(int x = 0 ; x<numInst; x++){
                    for(int y = 0; y < value[2].length; y++){
                        if(value[0][y] == instances.get(x).value(0)){
                            instances.get(x).setValue(numAttr, value[2][y]);
                            break;
                        }
                    }
                }
                SerializedNumericInstances numericInstances = new SerializedNumericInstances(nlr, attValue[1], value[2], instances.attribute(0).name(), "normalAttribute" );
                serializedNumericInstances.add(numericInstances);
                instances.deleteAttributeAt(0);
            }


            else if(instances.attribute(0).isString()){
                String[][] stringInstances = new String [2][numInst];
                attValue[0] = numClassInstances.clone();
                for(int a = 0; a < numInst; a++){
                    stringInstances[0][a] = String.valueOf(attValue[0][a]);

                }

                for(int a=0; a < numInst; a++ ){
                  stringInstances[1][a] =  instances.get(a).stringValue(0);

                }

                IFCNominalAlgorithm stringCross = new IFCNominalAlgorithm();
                stringCross.setCrossTable(stringInstances, false);
                double [] value = stringCross.getfuzzyTable();
                String [] featureName = stringCross.getfeatureName();

                instances.insertAttributeAt(new Attribute(instances.attribute(0).name()), numAttr);
                double[] newInstanceValue = new double[numInst];
                for (int a = 0; a < numInst; a++) {
                    for(int x = 0; x < featureName.length; x++ ){
                        if(instances.instance(a).stringValue(0).equals(featureName[x])){
                            instances.instance(a).setValue(numAttr, value[x]);
                            newInstanceValue[a]= value[x];
                        }
                    }
                }
                SerializedNominalInstances stringInstances2 = new SerializedNominalInstances(featureName, value, stringInstances[1], newInstanceValue, instances.attribute(0).name(), "normalAttribute" );
                serializedNominalInstances.add(stringInstances2);
                instances.deleteAttributeAt(0);
            }


            else if(instances.attribute(0).isNominal()){
                String[][] stringInstances = new String [2][numInst];
                attValue[0] = numClassInstances.clone();
                for(int a = 0; a < numInst; a++){
                    stringInstances[0][a] = String.valueOf(attValue[0][a]);

                }

                for(int a=0; a < numInst; a++ ){
                  stringInstances[1][a] =  instances.get(a).stringValue(0);
                }

                IFCNominalAlgorithm stringCross = new IFCNominalAlgorithm();
                stringCross.setCrossTable(stringInstances, false);
                double [] value = stringCross.getfuzzyTable();
                String [] featureName = stringCross.getfeatureName();

                instances.insertAttributeAt(new Attribute(instances.attribute(0).name()), numAttr);
                double[] newInstanceValue = new double[numInst];
                for (int a = 0; a < numInst; a++) {
                    for(int x = 0; x < featureName.length; x++ ){
                        if(instances.instance(a).stringValue(0).equals(featureName[x])){
                            instances.instance(a).setValue(numAttr, value[x]);
                            newInstanceValue[a]= value[x];
                        }
                    }
                }
                SerializedNominalInstances stringInstances2 = new SerializedNominalInstances(featureName, value, stringInstances[1], newInstanceValue, instances.attribute(0).name(), "normalAttribute" );
                serializedNominalInstances.add(stringInstances2);
                instances.deleteAttributeAt(0);

            }
        }
        FastVector valuesTemp = new FastVector();
        valuesTemp.addElement("0");
        valuesTemp.addElement("1");
        instances.insertAttributeAt(new Attribute(instances.attribute(0).name(), valuesTemp), numAttr);

        if(instances.attribute(classIndex).isString()| instances.attribute(classIndex).isNominal()){
            for(int a = 0; a < numClassInstances.length; a++){
                instances.get(a).setValue(numAttr, numClassInstances[a]);
            }
        }
        else if(instances.attribute(classIndex).isNumeric()){
            for(int a = 0; a < numClassInstances.length; a++){
                int temp = (int)numClassInstances[a];
                instances.get(a).setValue(numAttr, String.valueOf(temp));
            }
        }
        instances.setClassIndex(-1);
        instances.deleteAttributeAt(0);
        instances.setClassIndex(numAttr-1);
        return instances;
    }
}
