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
 * FilterOutput.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.classtrransformation;

import java.io.Serializable;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * Super class of NominalFilterOutput and BinaryFilterOuput.
 * Gets instances from NominalFilterOutput or BinaryFilterOuput. Transforms the order
 * of the attribute which contains the target values and updates the target values.
 *
 * @author Cédric Graf
 */
public abstract class FilterOutput implements Serializable {
    
     /** for serialization */
    static final long serialVersionUID = -7069148179905814324L;

    /** Contains the data.*/
    public Instances instances;

    /** Number of instances. */
    public int numInst;

    /** Number of attributes. */
    public int numAttr;

    /** Array list containing the binarised target values. */
    public double[] numClassInstances;

    /** Index of the attribute containing the target values.*/
    public int classIndex;

    /** Name of the attribute containing the target values.*/
    public String targetVariable;
    
    public String targetAttribute;

    /**
     * Gets instances from NominalFilterOutput or BinaryFilterOuput. Transforms the order
     * of the attribute which contains the target values and updates the target values.
     *
     * @param instances                     instances containing the datas.
     * @param serializedNumericInstances    array list containing numeric attribute values.
     * @param serializedNominalInstances    array list containing categorical attribute values.
     * @param classValue                    string containing the target value.
     * @param percentOfDataSet              percentage of data in an n-tile.
     * @return                              transformed instances containing fuzzy values and categorical target values.
     */
    public void setClassInstances(Instances instances, ArrayList<SerializedNumericInstances> serializedNumericInstances, ArrayList<SerializedNominalInstances> serializedNominalInstances, String classValue, String percentOfDataSet) {
        this.instances = instances;
        numAttr = instances.numAttributes();
        numInst = instances.numInstances();
        numClassInstances = new double[numInst];
        classIndex = instances.classIndex();
        String[] containsClasses;
        instances.insertAttributeAt(new Attribute(instances.attribute(classIndex).name()), instances.numAttributes());
        instances.setClassIndex(-1);
        String[] classData = new String [numInst];
        if(instances.attribute(classIndex).isNumeric()){
            for(int a = 0; a < instances.numInstances(); a++){
                numClassInstances[a] = instances.get(a).value(classIndex);
                if(numClassInstances[a]== Double.NaN)
                    numClassInstances[a] = 0;
            }
            //was muss nur beim ersten batch gemacht werden, was immer?
            instances.deleteAttributeAt(classIndex);
            BinariseNumericClasses numClasses = new BinariseNumericClasses();                        
            numClasses.setClasses(numClassInstances, classValue);  
            
            String[] sa = numClasses.getStringCalssNames();
            if(sa!=null)
                targetVariable = numClasses.getStringCalssNames()[0];
            numClassInstances = numClasses.getNumericCalssInstances();
            for(int a = 0; numClassInstances!=null && a < numClassInstances.length; a++){
                    instances.get(a).setValue(numAttr-1, numClassInstances[a]);
            }
            SerializedNominalInstances classInstances = new SerializedNominalInstances(classData, numClassInstances, classData, numClassInstances, instances.attribute(numAttr-1).name() , "classAttribute");
            serializedNominalInstances.add(classInstances);
        }
        else if(instances.attribute(classIndex).isNominal()){
            for(int a = 0; a < instances.numInstances(); a++){
                classData[a] = instances.get(a).stringValue(classIndex);
            }
            instances.deleteAttributeAt(classIndex);
            BinariseNominalClasses numClassInst = new BinariseNominalClasses();
            numClassInst.setClasses(classData, classValue);
            targetVariable = numClassInst.getStringCalssNames();
            numClassInstances = numClassInst.getNumericCalssInstances();
            for(int a = 0; a < numClassInstances.length; a++){
                instances.get(a).setValue(numAttr-1, numClassInstances[a]);
            }
            SerializedNominalInstances classInstances = new SerializedNominalInstances(classData, numClassInstances,  classData, numClassInstances, instances.attribute(numAttr-1).name() , "classAttribute");
            serializedNominalInstances.add(classInstances);
        }
        else if(instances.attribute(classIndex).isString()){
            for(int a = 0; a < instances.numInstances(); a++){
                classData[a] = instances.get(a).stringValue(classIndex);
            }
            instances.deleteAttributeAt(classIndex);
            BinariseNominalClasses numClassInst = new BinariseNominalClasses();
            numClassInst.setClasses(classData, classValue);
            targetVariable = numClassInst.getStringCalssNames();
            numClassInstances = numClassInst.getNumericCalssInstances();
            for(int a = 0; a < numClassInstances.length; a++){
                instances.get(a).setValue(numAttr-1, numClassInstances[a]);
            }
            SerializedNominalInstances classInstances = new SerializedNominalInstances(classData, numClassInstances,  classData, numClassInstances, instances.attribute(numAttr-1).name() , "classAttribute");
            serializedNominalInstances.add(classInstances);
        }
    }
}
