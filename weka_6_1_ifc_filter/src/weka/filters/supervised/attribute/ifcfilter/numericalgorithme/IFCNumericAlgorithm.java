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
 * IFCNumericAlgorithm.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

import weka.core.Attribute;
import weka.filters.supervised.attribute.IFC_Filter;

/**
 * Implements the numeric algorithm.
 *
 * @author Michael Kaufmann, Cédric Graf
 */
public class IFCNumericAlgorithm {

    double[][] doubleData;
    double[][] fuzzyTable;
    double correl_new;
    double table[][];
    public PieceWiseLinearFunction piecewiseAffineFunctionApproximation;
    public IFC_Filter ifcFilter;
    public int attNo;
    private Attribute attr;

    /**
     * Constructor of the class.
     */
    public IFCNumericAlgorithm() {
    }

    public IFCNumericAlgorithm(IFC_Filter ifcFilter, int attNo, Attribute attr) {
        this.ifcFilter = ifcFilter;
        this.attNo = attNo;
        this.attr = attr;
    }

    /**
     * Iterates and calculates the correlation until it is 20 times maximal or
     * smaller as percentOfDataSet.
     *
     * @param doubleDat double array containing the target values and numeric
     * values.
     * @param percentOfDataSet percentage of data in an n-tile.
     */
    public void setCrossTable(double[][] doubleDat, double percentOfDataSet) {
        doubleData = doubleDat;
        int trials = 0;
        int ntil = 2;
        int last_ntil = 2;
        correl_new = 0;
        double correl_max = -1;

        /*for(int i = 0; i < doubleDat[0].length;i++){
         if(doubleDat[0][i] == 1){
         doubleDat[0][i] = 0;
         }
         else if(doubleDat[0][i] == 0){
         doubleDat[0][i] = 1;
         }
         } */
        QuickSort quick = new QuickSort();
        Correlation corr = new Correlation();
        piecewiseAffineFunctionApproximation = new PieceWiseLinearFunction();

        int l = 0;
        int r = doubleData[1].length - 1;

        quick.set(doubleData, l, r, 1);

        //Groups and sorts two elements
        int upper = 0;
        int under = 0;
        for (int i = 0; i < doubleData[0].length - 1; i++) {
            if (i < doubleData[0].length - 1 && doubleData[1][i] == doubleData[1][i + 1]) {
            } else {
                upper = i;
                double[][] temp = new double[2][upper - under + 1];
                for (int j = under; j <= upper; j++) {
                    if (doubleData[0][j] == 0) {
                        doubleData[0][j] = 1;
                    } else {
                        doubleData[0][j] = 0;
                    }
                    temp[0][j - under] = doubleData[0][j];
                    temp[1][j - under] = doubleData[1][j];
                }
                quick.set(temp, 0, temp[0].length - 1, 0);
                for (int j = under; j <= upper; j++) {
                    if (temp[0][j - under] == 0) {
                        doubleData[0][j] = 1;
                    } else {
                        doubleData[0][j] = 0;
                    }
                }
                under = upper + 1;
            }
            if (i == doubleData[0].length - 2) {
                int tempb = doubleData[0].length - 1;

                double[][] temp = new double[2][tempb - under + 1];
                for (int j = under; j <= tempb; j++) {
                    if (doubleData[0][j] == 0) {
                        doubleData[0][j] = 1;
                    } else {
                        doubleData[0][j] = 0;
                    }
                    temp[0][j - under] = doubleData[0][j];
                    temp[1][j - under] = doubleData[1][j];
                }
                quick.set(temp, 0, temp[0].length - 1, 0);
                for (int j = under; j <= tempb; j++) {
                    if (temp[0][j - under] == 0) {
                        doubleData[0][j] = 1;
                    } else {
                        doubleData[0][j] = 0;
                    }
                }

            }
        }

        //INFO this loop optimizes Correlation
        //INFO doubleData[0] = Y, doubleData[1] = X
        //INFO fuzzVal = instance of piecewise affine function
        //INFO fuzzyTable[0] = Y, fuzzyTable[1] = X, fuzzyTable[2] = Y' (Prediction)
        for (ntil = 2; ntil < doubleData[1].length; ntil++) {

            //INFO here the magic happens: NLR is calculated, as well as slopes and intercepts for pwlfa
            piecewiseAffineFunctionApproximation.set(doubleData, ntil, 0, 0);
            fuzzyTable = piecewiseAffineFunctionApproximation.getCrossTable();
            corr.set(fuzzyTable, 0, 0, 0);
            correl_new = corr.getCorr();
            
            if (piecewiseAffineFunctionApproximation.nlr.ntiles 
                    > piecewiseAffineFunctionApproximation.nlr.numEntires) {
                last_ntil = ntil - 1;
                break;
            } /*
            if (piecewiseAffineFunctionApproximation.nlr.ntilesTooSmall()) {
                last_ntil = ntil - 1;
                break;
            } */
            if (correl_max <= correl_new) {
                correl_max = correl_new;
                last_ntil = ntil;
                trials = 0;
            } else if (correl_max > correl_new && trials <= 1) {
                ++trials;
            } else if (correl_max > correl_new && trials > 1) {
                break;
            }
            //limits overfit
            if (percentOfDataSet * doubleData[0].length / 100 > doubleData[0].length / ntil) {
                break;
            }
        }
        piecewiseAffineFunctionApproximation.set(doubleData, last_ntil, 0, 0);
        fuzzyTable = piecewiseAffineFunctionApproximation.getCrossTable();
        table = piecewiseAffineFunctionApproximation.getNLR();
        corr.set(fuzzyTable, 0, 0, 0);
        correl_new = corr.getCorr();
    }

    /**
     * Returns double array containing the target values, numeric values und
     * apporximates fuzzy values (NLR).
     *
     * @return double array containing the target values, numeric values und
     * apporximates fuzzy values (NLR).
     */
    public double[][] getCrossTable() {
        return fuzzyTable;
    }

    /**
     * Returns maximal correlation.
     *
     * @return double maximal correlation.
     */
    public double getCorrelation() {
        return correl_new;
    }

    /**
     * Returns double array containing the target values, numeric values und
     * apporximates fuzzy values (NLR).
     *
     * @return double array containing average of an n-tile at row[0], the NLR
     * at row[1], the slope of an n-tile at row[2], the axis intercepts for each
     * n-tile at row[3], interval for each n-tile at row[4] and the n-tile index
     * at row[5].
     */
    public double[][] getNLR() {
        return table;
    }

    private boolean containsNan(NLR nlr) {
        boolean b = false;
        for (int i = 0; i < nlr.ntiles; i++) {
            double val = nlr.contingencyTableNLR[11][i];
            b = b || Double.isNaN(val);
            System.out.print("");
        }

        return b;
    }
}
