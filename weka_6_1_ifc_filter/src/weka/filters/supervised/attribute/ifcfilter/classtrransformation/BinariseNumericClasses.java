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
 * BinariseNumericClasses.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.classtrransformation;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Controls if the input array of the IFC_Filter have 0 and 1 as values.
 *
 * @author Cédric Graf
 */
public class BinariseNumericClasses {
    /** Input array. */
    double [] numericClassInstances;

    /** Double array to be returned.*/
    double [] calssDoubleNames;
    
    /** Name of an instance.*/
    String [] classNames;

    /**
     * Generates a sortet array of all possible classvariable.
     *
     * @param numClassInstances contains the dataset with all instances of the class attribute
     */
    public void setClasses(double[] numClassInstances, String arrayClassVariable){
        Hashtable classTable = new Hashtable();
        int numberOfVariable = 0;
        for(int a = 0; a<numClassInstances.length; a++){
            double ab = numClassInstances[a];
            if(!classTable.containsKey(ab)){
                classTable.put(ab, new Integer(1));
                ++numberOfVariable;
            }
        }
        if(numberOfVariable == 2){
            numericClassInstances = numClassInstances;
            classNames = new String[numberOfVariable];
            //classNames[0] = "1";
            //classNames[1] = "0";
            
            classNames[0] = "1";
            classNames[1] = "0";
        }
        else if(numberOfVariable > 2){
            calssDoubleNames = new double[numberOfVariable];

            Enumeration e = classTable.keys();
            int x = 0;
            while(e.hasMoreElements()){
                calssDoubleNames[x] = ((Double)e.nextElement()).doubleValue();
                ++x;
            }
            Arrays.sort(calssDoubleNames);

            classNames = new String[calssDoubleNames.length];
            for(int a = 0; a < calssDoubleNames.length; a++){
                classNames[a] = String.valueOf(calssDoubleNames[a]);
            }

            int arrayClassIndex = 0;
            if(arrayClassVariable.equals("first")){
                arrayClassIndex = 0;
            }
            else if(arrayClassVariable.equals("last")){
                arrayClassIndex = calssDoubleNames.length-1;
            }
            else if(0 < Integer.valueOf( arrayClassVariable) && Integer.valueOf(arrayClassVariable) <= calssDoubleNames.length){
                arrayClassIndex = Integer.valueOf(arrayClassVariable)-1;
            }
            else{
                arrayClassIndex = 0;
            }

            for(int a = 0; a < numClassInstances.length; a++){
                if(numClassInstances[a]== calssDoubleNames[arrayClassIndex]){
                    numClassInstances[a] = 1;
                }
                else{
                    numClassInstances[a] = 0;
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
    public String[] getStringCalssNames(){
        return classNames;
    }

}
