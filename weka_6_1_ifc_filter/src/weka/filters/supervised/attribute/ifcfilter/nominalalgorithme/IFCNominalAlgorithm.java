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
 * IFCNominalAlgorithm.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.nominalalgorithme;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Implements the categorical algorithm.
 *
 * @author Michael Kaufmann, Cédric Graf
 */
public class IFCNominalAlgorithm {
    String[][] stringArray;
    double[] fuzzyTable;
    String[] nominalValueToCountNumberOfOccurrencesOfY;

    /**
     * Constructor of the class.
     */
    public IFCNominalAlgorithm(){}

    /**
     * Constructs a cross table and computes fuzzy values (NLR).
     *
     * @param stringData        string array containing nominal attributes.
     */
    public void setCrossTable(String[][] stringData, boolean binary){
//!
        stringArray = stringData;
        Hashtable table = new Hashtable();
        int wordLength = stringArray[1].length;
        int numberOfAttributes = 0;
        for(int a = 0; a<wordLength; a++){
            String ab = stringArray[1][a];
            if(!table.containsKey(ab)){
                table.put(ab, new Integer(1));
                ++numberOfAttributes;
            }
        }
        int[][] countTable = new int[2][numberOfAttributes];
        double[][] crossTable = new double[2][numberOfAttributes];

        //generates string array with the features  
        nominalValueToCountNumberOfOccurrencesOfY = new String[numberOfAttributes];
        Enumeration e = table.keys();
        int x = 0;
        while(e.hasMoreElements()){
            // values to count
            nominalValueToCountNumberOfOccurrencesOfY[x] = (String) e.nextElement(); 
            ++x;
        }
        //gets Similar datas
        
        String class0 = "";
        String class1 = "";
        
        for(int a = 0; a<wordLength; a++){
            for(int b = 0; b<numberOfAttributes;b++){
                
                if(stringArray[1][a].equals(nominalValueToCountNumberOfOccurrencesOfY[b])){
                    for(int c = 0; c<2; c++){
                        if(Double.valueOf(stringArray[0][a])==c){
                            countTable[c][b]=countTable[c][b]+1;
                        }

                    }
                }
            }
        }


        //get divisor
        double divisor = 0;
        for(int a = 0; a<numberOfAttributes; a++){
            for(int b = 0; b < 2; b++){
                divisor = divisor + countTable[b][a];
            }
        }

        //sets crosstable
        if(binary==false){
        for(int a = 0; a<numberOfAttributes; a++){
            for(int b = 0; b < 2; b++){
                crossTable[b][a] = (double)countTable[b][a]/divisor;
            }
        }}
        else
        {
        
        }
        
        //Computes L
        for(int b = 0; b < 2; b++){
            double sumy = 0;
            for(int a = 0; a<numberOfAttributes; a++){
                sumy = sumy + crossTable[b][a];
            }
            for(int a = 0; a<numberOfAttributes; a++){
                crossTable[b][a] = crossTable[b][a]/sumy;
            }
        }       
        //Computes LR
        fuzzyTable = new double[numberOfAttributes];

        //Computes NLR
        for(int a = 0; a<numberOfAttributes; a++){
            /*
            double befor =1;
            double lr = 1;
            double nlr = 1;
            for(int b = 0; b < 2; b++){
                lr = befor /crossTable[b][a];
                befor = crossTable[b][a];
                fuzzyTable[a] = 1/(1+(1/lr));
            } */
            int n_0 = 0;  for(int k= 0; k<numberOfAttributes; k++) n_0 += countTable[0][k];
            int n_1 = 0;  for(int k= 0; k<numberOfAttributes; k++) n_1 += countTable[1][k];
            double n_0a = countTable[0][a];
            double n_1a = countTable[1][a];
            
            fuzzyTable[a] = (n_1a/n_1) / ((n_1a/n_1) + (n_0a/n_0));
        }
    }

    /**
     * Returns double array with fuzzy values (NLR).
     *
     * @return      double array with fuzzy values.
     */
    public double[] getfuzzyTable(){
        return fuzzyTable;
    }

    /**
     * Returns names of the fuzzy values (NLR).
     *
     * @return      string array in order of fuzzyTable.
     */
    public String[] getfeatureName(){
        return nominalValueToCountNumberOfOccurrencesOfY;
    }

    public int[] getFeatureCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}




