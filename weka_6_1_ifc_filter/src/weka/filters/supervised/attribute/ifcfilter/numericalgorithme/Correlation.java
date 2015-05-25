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
 * Correlation.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

/**
 *  Class computing corralation.
 *
 * @author Cédric Graf
 */
public class Correlation implements IFCNumericAlgorithmInterface{

    /** Double containing the correlation. */
    double corr;
    
    /** Double array containing target values und apporximates fuzzy values (NLR). */
    double[][] table;

    /**
     * Constructor of the class.
     */
    public Correlation(){}

    /**
     * Computes correlation.
     *
     * @param doubleDat     double array containing target values and apporximates fuzzy values (NLR).
     * @param leftNtilnr    int containing 0.
     * @param rightClassy   int containing 0.
     * @param arrayIndex    int containing the lengt of doubleDat.
     */
    public void set(double[][] doubleDat, int leftNtilnr, int rightClassy, int arrayIndex){
        table = doubleDat;
        double averagex = 0;
        double averagey = 0;
        double tempaveragex = 0;
        double tempaveragey = 0;
        double standarddevx = 0;
        double standarddevy = 0;
        double tempstandarddevx = 0;
        double dividend = 0;
        double divisor = 0;
        double number_of_dataset = doubleDat[0].length;

        //Durchschnitt x
        for(int i = 0; i < doubleDat[1].length; i++){
            tempaveragex = tempaveragex + doubleDat[1][i];
        }
        averagex = tempaveragex/number_of_dataset;

        //Durchschnitt y
        for(int i = 0; i < doubleDat[2].length; i++){
            tempaveragey = tempaveragey + doubleDat[2][i];
        }
        averagey = tempaveragey/number_of_dataset;

        //Summ (x-Durchschnitt x * y-Durchnitt y)
        for(int i = 0; i < doubleDat[1].length; i++){
            dividend = dividend + (doubleDat[1][i]-averagex)*(doubleDat[2][i]-averagey);
        }
        dividend = dividend/number_of_dataset;

        //Standardabweichung x
        tempaveragex = 0;
        for(int i = 0; i < doubleDat[1].length; i++){
            tempaveragex = tempaveragex + (doubleDat[1][i]-averagex)*(doubleDat[1][i]-averagex);
        }
        
        tempaveragex = tempaveragex/number_of_dataset;
        standarddevx = Math.sqrt(tempaveragex);

        //Standardabweichung y
        tempaveragey = 0;
        for(int i = 0; i < doubleDat[2].length; i++){
            tempaveragey = tempaveragey + (doubleDat[2][i]-averagey)*(doubleDat[2][i]-averagey);
        }
        tempaveragey = tempaveragey/number_of_dataset;
        standarddevy = Math.sqrt(tempaveragey);

        //Correlation
        divisor = standarddevx*standarddevy;
        if(divisor != 0){
            corr = dividend /divisor;
        }
        else if(divisor == 0){
            corr = 0;
        }
    }

    /**
     * Returns double array containing the target values, numeric values and apporximates fuzzy values (NLR).
     *
     * @return           double array containing the target values, numeric values and apporximates fuzzy values (NLR).
     */
    public double[][] getCrossTable() {
        return table;
    }

    /**
     * Returns double array containing the target values, numeric values and apporximates fuzzy values (NLR).
     *
     * @return           double array containing the target values, numeric values and apporximates fuzzy values (NLR).
     */
    public double[][] getNLR() {
        return table;
    }

    /**
     * Returns the correlation.
     *
     * @return      double containing the correlation.
     */
    public double getCorr() {
        return corr;
    }
}
