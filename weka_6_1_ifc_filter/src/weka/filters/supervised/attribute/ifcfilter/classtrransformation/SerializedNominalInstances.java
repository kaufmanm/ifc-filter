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
 * SerializedNominalInstances.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.classtrransformation;

import java.io.Serializable;

/**
 * Serializes different variable types for the visualization window.
 *
 * @author Cédric Graf
 */
public class SerializedNominalInstances implements Serializable{
    
    /** for serialization */
    private static final long serialVersionUID = 1L;

    /** String containing the attribute type. */
    public String type;

    /** String containing attribute names. */
    public String attributeName;

    /** Double array containing old attribute names. */
    public String[] oldValue;

    /** Double array containing fuzzy attribute values. */
    public double[] newValue;

    /** String array containing atribute names in order to featureValue. */
    public String[] featureName;
    
    /** Double array containing NLR's. */
    public double[] featureValue;

    /**
     * Serializes different variable types for the visualization window.
     *
     * @param featureName       string array containing atribute names in order to featureValue.
     * @param featureValue      double array containing NLR's.
     * @param oldValue          double array containing old attribute names.
     * @param newValue          double array containing fuzzy attribute values.
     * @param attributeName     string containing attribute names.
     * @param type              string containing the attribute type.
     */
    public SerializedNominalInstances(String[]featureName, double[] featureValue, String[] oldValue, double[] newValue, String attributeName, String type){
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.attributeName = attributeName;
        this.type = type;
        this.featureName = featureName;
        this.featureValue = featureValue;
    }
}
