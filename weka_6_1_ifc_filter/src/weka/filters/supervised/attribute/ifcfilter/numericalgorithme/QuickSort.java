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
 * QuickSort.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

/**
 * Class for quick sort algorithm.
 *
 * @author cedric
 */
public class QuickSort implements IFCNumericAlgorithmInterface{
    
    /** double array containing the target values and numeric values. */
    double[][] doubleData;

    /**
     * Constructor of the class.
     */
    public void quickSort(){}

    /**
     * Computes quick sort.
     *
     * @param doubleDat     double array containing the target values and numeric values.
     * @param leftNtilnr    int left limit of a piece of the array.
     * @param rightClassy   int right limit of a piece of the array.
     * @param arrayIndex    int gives the row to be sorted.
     */
    public void set(double[][] doubleDat, int leftNtilnr, int rightClassy, int arrayIndex){
        int i = leftNtilnr, j = rightClassy;

        double pivot = doubleDat[arrayIndex][(leftNtilnr + rightClassy) / 2];

        while (i <= j) {

            while (doubleDat[arrayIndex][i] < pivot) {
                i++;
            }

            while (doubleDat[arrayIndex][j] > pivot) {
                j--;
            }

            if (i <= j) {
                exchange(doubleDat, i, j);
                i++;
                j--;
            }
        }

        if (leftNtilnr < j)
            set(doubleDat, leftNtilnr, j, arrayIndex);
        if (i < rightClassy)
            set(doubleDat, i, rightClassy,arrayIndex);
        doubleData = doubleDat;
    }

    /**
     * Exchanges pieces of the array.
     *
     * @param doubleData    double array containing the target values and numeric values.
     * @param i             int gives the position to exchage.
     * @param j             int gives the position to exchage.
     * @return              double array containing the target values and numeric values.
     */
    private double[][] exchange(double[][] doubleData, int i, int j) {
        double temp = doubleData[1][i];
        doubleData[1][i] = doubleData[1][j];
        doubleData[1][j] = temp;
        temp = doubleData[0][i];
        doubleData[0][i] = doubleData[0][j];
        doubleData[0][j] = temp;
        return doubleData;
    }
    /**
     * Returns sortet array.
     *
     * @return      double array containing the target values and sorted numeric values.
     */
    public double[][] getCrossTable() {
        return doubleData;
    }

    /**
     * Returns sortet array.
     *
     * @return      double array containing the target values and sorted numeric values.
     */
    public double[][] getNLR() {
        return doubleData;
    }

    /**
     * Returns Corelation.
     *
     * @return  double equal 0.
     */
    public double getCorr() {
        return 0;
    }
}

