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
 * Ntil.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

import java.util.Date;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.TiesStrategy;

/**
 * Class to compute index of the n-tile.
 *
 * @author Cédric Graf
 */
public class Ntil implements PieceWiseLinearFunctionInterface {

    double[][] numData;

    public void Ntil() {

    }

    /**
     * Computes the index of the n-tile.
     *
     * @param table double array containing the target values and sorted numeric
     * values.
     * @param ntil int sets number of n-tile.
     */
    public void set(double[][] table, int ntil) {
        numData = new double[3][table[1].length];
        numData[0] = table[0];
        numData[1] = table[1];
        PercentileRank ranking = new PercentileRank(NaNStrategy.REMOVED, TiesStrategy.MINIMUM);
        double[] ranks = ranking.rank(table[1]);

        int numq = 0;
        double qold = 0;
        double qnew = 0;
        int n11=0;
        for (int i = 0; i < table[1].length; i++) {
            qnew = java.lang.Math.floor(ranks[i] * ntil) + 1;
            if (qold != qnew) {
                numq++;
            }
            qold = qnew;
            numData[2][i] = qnew; //
            //numData[2][i] = java.lang.Math.floor(i * ntil / table[1].length) + 1;
            if (table[0][i] == 1 && numData[2][i] == 1) {
                n11++;
            }
        }
        String[] labels = {"Y", "X", "N"};
        //System.out.print(contingencyCrosstable.ar2s(numData, labels));
        new Date();
    }

    /**
     * Returns double array containing the target values, numeric values and
     * index of the n-tile.
     *
     * @return double array containing the target values, sorted numeric values
     * and index of the n-tile.
     */
    public double[][] get() {
        return numData;
    }
}
