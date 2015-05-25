/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author Michael Kaufmann
 */
public class IFCModel implements Serializable {
    static final long serialVersionUID = -7069148179905814324L;
    public double[][] nlr;
    //att-index x ... x ... -> ...
    public double[][] values;
   // private IFCNominalAlgorithm stringCross;
    //att-index x ... x ... -> ...
    public double[] value;
    //att-index x ... x ... -> ...
    public String[] featureName;
    public String type = "";
    public Hashtable<Object, Integer> featureCode;

    public IFCModel(double[][] nlr, double[][] values, double[] value, String[] featureName, Hashtable<Object, Integer> featureCode, String type) {
            this.nlr = nlr;
            this.values = values;
            this.value = value;
            this.featureName = featureName;
            this.type = type;
            this.featureCode = featureCode;
    }
    
}
