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
 * CompressDoubleFeatureValue.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */

package weka.filters.supervised.attribute.ifcfilter.frame;

/**
 * Takes values which occur twice in an array out of the array. 
 * Gives the cleansed array back.
 *
 * @author Cédric Graf
 */
class CompressDoubleFeatureValue {

    /** cleansed array with the original value of the attribute. */
    double[] newSortedFeatureName;

    /** cleansed array with the fuzzy value of the attribute. */
    double[] newSortedFeatureValue;

    /**
     * Constructor of the class.
     */
    public void SortsCompressDoubleFeatureValue(){}

    /**
     * Takes values which occur twice in an array out of the array.
     *
     * @param sortedFeatureName     array with the original value of the attribute.
     * @param sortedFeatureValue    array with the fuzzy value of the attribute.
     */
    public void set(double[] sortedFeatureName, double[] sortedFeatureValue ){
        
        //Densify the Dataset
        int length = 0;
        for(int i = 0 ; i < sortedFeatureName.length; i++){
            if(i < sortedFeatureName.length-1 && sortedFeatureName[i]==sortedFeatureName[i+1]){


            }
            else {
            length = length +1;
            }
        }
        int count = 0;
        newSortedFeatureName  = new double [length];
        newSortedFeatureValue = new double[length];

        double add = 0;
        for(int i = 0 ; i < sortedFeatureName.length; i++){
            if(i<sortedFeatureName.length-1 && sortedFeatureName[i]==sortedFeatureName[i+1]){
                add = add + sortedFeatureName[i];
            }
            else {
                
                newSortedFeatureName[count] = sortedFeatureName[i];
                newSortedFeatureValue[count] = sortedFeatureValue[i];
                ++count;
                add=0;
            }
        }
    }

    /**
     * Gives the cleansed array back.
     *
     * @return      cleansed array with the original value of the attribute.
     */
    public double[] getCompressedFeatureName(){
        return newSortedFeatureName;
    }

    /**
     * Gives the cleansed array back.
     *
     * @return      cleansed array with the fuzzy value of the attribute.
     */
    public double[] getCompressedFeatureValue(){
        return newSortedFeatureValue;
    }
}
