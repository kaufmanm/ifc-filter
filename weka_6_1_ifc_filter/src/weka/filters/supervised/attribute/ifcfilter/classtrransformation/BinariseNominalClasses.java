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
 * BinariseNominalClasses.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.classtrransformation;

import java.util.Hashtable;

/**
 * Generates a double array with values 0 and 1 out of a string array. Target variables
 * have the value 1.
 *
 * @author Cédric Graf
 */
public class BinariseNominalClasses {
    /** Indicates the length of numericClassInstances.*/
    int arrayClassIndex;
    
    /** Double array to be returned.*/
    double [] numericClassInstances;
    
    /** Name of an instance.*/
    String [] calssNames;
    
    /** Name of the target variable.*/
    String targetVariable;
    
    /**
     * Constructor for the class.
     */
    public BinariseNominalClasses(){
    }

    /**
     * Sets the double array numericClassInstances out of classInstances.
     *
     * @param classInstances        string with the target variable.
     * @param arrayClassVariable    string array with possible target values.
     */
    public void setClasses(String[] classInstances, String arrayClassVariable){

        Hashtable classTable = new Hashtable();
        int numberOfClasses = 0;

        //Counts the number of target values
        for(int a = 0; a<classInstances.length; a++){
            String ab = classInstances[a];
            if(!classTable.containsKey(ab)){
                classTable.put(ab, new Integer(1));
                ++numberOfClasses;
            }
        }
        
        calssNames = new String[numberOfClasses];
        numberOfClasses = 0;
        classTable.clear();
        for(int i = 0; i < classInstances.length; i++){
            String ab = classInstances[i];
            if(!classTable.containsKey(ab)){
                classTable.put(ab, new Integer(1));
                calssNames[numberOfClasses]= classInstances[i];
                ++numberOfClasses;
            }
        }
        arrayClassIndex = 0;
        if(arrayClassVariable.equals("first")){
            arrayClassIndex = 0;
        }
        else if(arrayClassVariable.equals("last")){
            arrayClassIndex = calssNames.length-1;
        }
        else if( 0 < Integer.valueOf( arrayClassVariable) && Integer.valueOf( arrayClassVariable) <= calssNames.length){
            arrayClassIndex = Integer.valueOf(arrayClassVariable)-1;
        }
        else{
            arrayClassIndex = 0;
        }
        numericClassInstances = new double[classInstances.length];
        for(int a = 0; a<classInstances.length; a++){
            for(int i =0; i <calssNames.length; i++){
                if(classInstances[a].equals(calssNames[arrayClassIndex])){
                    numericClassInstances[a] = 0;
                }
                else{
                    numericClassInstances[a] = 1;
                }
            }
        }
    }

    /**
     * Returns binarised the target values.
     *
     * @return      double array with values 0 and 1 out of a string array.
     */
    public double[] getNumericCalssInstances(){
        return numericClassInstances;
    }
    /**
     * Returns the ordered target names.
     *
     * @return string array with tagret names.
     */
    public String getStringCalssNames(){
        targetVariable = calssNames[arrayClassIndex];
        return targetVariable;
    }

}
