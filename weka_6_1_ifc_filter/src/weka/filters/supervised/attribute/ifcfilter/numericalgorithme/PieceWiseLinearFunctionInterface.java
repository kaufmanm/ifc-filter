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
 * PieceWiseLinearFunctionInterface.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

/**
 * Interface for NLR, Ntil and NumericCrosstable to connect to PieceWiseLinearFunction.
 *
 * @author Cédric Graf
 */
interface PieceWiseLinearFunctionInterface {

    /**
     * Sets data in classes to be computed.
     *
     * @param table     double array for computation..
     * @param ntil      int for computation..
     */
    public void set(double[][] table, int ntil);
    
    /**
     * Returns double array for different computations.
     *
     * @return      double array.
     */
    public double[][] get();
}
