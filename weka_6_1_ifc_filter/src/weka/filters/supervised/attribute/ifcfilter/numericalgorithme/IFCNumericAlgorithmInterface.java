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
 * IFCNumericAlgorithmInterface.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

/**
 * Interface for Corralation, QuickSort and PieceWiseLinearFunction to connect to IFCNumericAlgorithm.
 *
 * @author Cédric Graf
 */
public interface IFCNumericAlgorithmInterface {
    /**
     * Sets data in classes to be computed.
     * 
     * @param doubleDat         double array for computation.
     * @param leftNtilnr        int for computation.
     * @param rightClassy       int for computation.
     * @param arrayIndex        int for computation.
     */
    public void set(double[][] doubleDat, int leftNtilnr, int rightClassy, int arrayIndex);

    /**
     * Returns double array for different computations.
     *
     * @return      double array.
     */
    public double[][] getCrossTable();
    
    /**
     * Returns double array for different computations.
     *
     * @return      double array.
     */
    public double[][] getNLR();

    /**
     * Returns value for different computations.
     * 
     * @return      double for computation.
     */
    public double getCorr();
}
