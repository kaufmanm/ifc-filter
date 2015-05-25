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
 * NLR.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

import weka.filters.supervised.attribute.IFC_Filter;
import static weka.filters.supervised.attribute.ifcfilter.numericalgorithme.contingencyCrosstable.ar2s;

/**
 * Class to compute NLR.
 *
 * @author Cédric Graf
 */
public class NLR implements PieceWiseLinearFunctionInterface {

    /**
     * Double containing NLR.
     */
    public double[][] contingencyTableNLR;
    public int ntiles;
    public int numEntires;

    /**
     * Computes NLR.
     *
     * @param table double array containing the sum of the target value 0 at
     * row[0], the sum of the target value 1 at row[1], the average of the
     * n-tile at row[2] and the index of the n-tileat row[4].
     * @param ntiles int containing the number of n-tile.
     */
    public void set(double[][] contingencyTable, int ntiles) {
        this.contingencyTableNLR = contingencyTable;
        this.ntiles = ntiles;
        this.numEntires = ntiles;

        // count nXY
        //contingencyTable[0]: ntl
        //contingencyTable[1]: n10
        //contingencyTable[2]: n11
        //contingencyTable[3]: n1*
        //contingencyTable[4][0]: n*0
        //contingencyTable[5][0]: n*1
        //contingencyTable[6][0]: n**
        //contingencyTable[7]: s1*
        //contingencyTable[8]: a1*
        //contingencyTable[9]: p10 (later)
        //contingencyTable[10]: p11 (later)
        //contingencyTable[11]: nlr (later)
        for (int i = 0; i < ntiles; i++) {
            contingencyTableNLR[9][i] = contingencyTable[1][i] / contingencyTable[4][0];
        }
        for (int i = 0; i < ntiles; i++) {
            contingencyTableNLR[10][i] = contingencyTable[2][i] / contingencyTable[5][0];
        }
        for (int i = 0; i < ntiles; i++) {
            contingencyTableNLR[11][i]
                    = contingencyTableNLR[10][i]
                    / (contingencyTableNLR[10][i] + contingencyTableNLR[9][i]);
        }
        /*
        System.out.println(
         contingencyCrosstable.ar2s(contingencyTable,
         contingencyCrosstable.labels)); */
        int numNan = containsNan();
        if (numNan > 0) {
            this.numEntires = this.numEntires - numNan;
            deleteNan();
            
        }
        System.out.print("");
    }

    /**
     * Returns double array containing the NLR.
     *
     * @return double array in which the NLR are on row [0].
     */
    public double[][] get() {
        return contingencyTableNLR;
    }

    public int containsNan() {
        int c = 0;
        for (int i = 0; i < numEntires; i++) {
            double val = contingencyTableNLR[11][i];
            if (Double.isNaN(val)) {
                c++;
            }
        }
        return c;
    }

    private void deleteNan() {
        int offset = 0;
        double[][] newA = new double[contingencyTableNLR.length][this.numEntires];
        for (int i = 0; i < ntiles; i++) {
            double val = contingencyTableNLR[11][i];
            if (!Double.isNaN(val)) {
                for(int k = 0; k < contingencyTableNLR.length; k++)
                    newA[k][i-offset] = contingencyTableNLR[k][i];
            } else {
                offset ++;
            }
        }
        contingencyTableNLR = newA;
    }

    boolean ntilesTooSmall() {
        boolean b = false;
        
        /*System.out.println(
         contingencyCrosstable.ar2s(contingencyTableNLR,
         contingencyCrosstable.labels)); */
        for (int i= 0; i<this.numEntires; i++){
            int num = (int) this.contingencyTableNLR[3][i];
            b = b || this.contingencyTableNLR[3][i]<IFC_Filter.minNumberOfSamplesGlobal;
        }
        System.out.print("");
        return b;
    }



}
