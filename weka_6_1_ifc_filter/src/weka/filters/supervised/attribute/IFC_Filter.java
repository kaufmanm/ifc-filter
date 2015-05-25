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
 * IFC_Filter.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */

package weka.filters.supervised.attribute;

import java.util.ArrayList;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.Capabilities.Capability;
import weka.filters.Filter;
import weka.filters.SupervisedFilter;
import java.util.Enumeration;
import java.util.Vector;
import weka.core.Attribute;
import weka.core.SelectedTag;
import weka.core.SingleIndex;
import weka.core.Tag;
import static weka.filters.supervised.attribute.IFC_Filter.TAGS_TARGET_TYPE;
import static weka.filters.supervised.attribute.IFC_Filter.TARGET_TYPE_BINOMIAL;
import static weka.filters.supervised.attribute.IFC_Filter.TARGET_TYPE_NUMERIC;
import weka.filters.supervised.attribute.ifcfilter.frame.IFCFilterFrame;
import weka.filters.supervised.attribute.ifcfilter.classtrransformation.BinaryFilterOuput;
import weka.filters.supervised.attribute.ifcfilter.classtrransformation.SerializedNumericInstances;
import weka.filters.supervised.attribute.ifcfilter.classtrransformation.SerializedNominalInstances;
import weka.filters.supervised.attribute.ifcfilter.classtrransformation.NominalFilterOutput;

/**
 * <!-- globalinfo-start -->
 * IFC_Filter is a filter for transforming numeric and categorical values into normalized likelihood ratios values.
 * <p/>
 * <!-- globalinfo-end -->
 <!-- options-start -->
 * Valid options are: <p/>
 *
 * <pre> -C
 *  Chooses the class value if categorical class values.</pre>
 *
 * <pre> -W
 *  If true opens the NLR visualization window.</pre>
 *
 * <pre> -P
 *  Is limiting the 0 of data in an n-tile.</pre>
 *
 * <pre> -T
 *  Enables the numeric or categorical output format of the instances.</pre>
 *
 * <pre> -N
 *  Minimum number of samples per quantile </pre>
 *
 <!-- options-end -->
 * @author Michael Kaufmann, Cédric Graf
 */

//INFO to add an option, add Javadoc comment, singleIndex, getter, setter, getOptions and setOptions.
public class IFC_Filter extends Filter implements SupervisedFilter, OptionHandler  {

    /** for serialization */
    static final long serialVersionUID = -7069148179905814324L;

    /** The type of the target: NUMERIC. */
    public static final int TARGET_TYPE_NUMERIC = 1;

    /** The type of target: BINOMIAL. */
    public static final int TARGET_TYPE_BINOMIAL = 2;

    /** Sets the output instances type. */
    public static final Tag[] TAGS_TARGET_TYPE = {
        new Tag(TARGET_TYPE_NUMERIC, "nominal"),
        new Tag(TARGET_TYPE_BINOMIAL, "binary"),
    };

    /** Sets the standard target type */
    protected int targetType = TARGET_TYPE_BINOMIAL;

    /** Sets the percentage of data in a n-tile. */
    private SingleIndex percentOfDataSet = new SingleIndex("4%");

    /** Sets the class value standard. */
    private SingleIndex classVariable = new SingleIndex("first");
    
    /** Sets the minimu number of Samples in quantile */
    private SingleIndex minNumberOfSamples = new SingleIndex("5");

    /** Sets standard value for the NLR visualization window. */
    private boolean openWindow = false;
    private BinaryFilterOuput m_numericFilter;
    private NominalFilterOutput m_binomialFilter;
    public double[][] nlr;
    public double[][] values;
    public double[] value;
    public String[] featureName;
    public static Integer minNumberOfSamplesGlobal;




    /**
     * Returns a string describing this filter.
     *
     * @return a description of the filter suitable for
     * displaying in the explorer/experimenter gui
     */
    public String globalInfo() {
        return
        " IFC_Filter is a filter for transforming numeric and categorical values into normalized likelihood ratios values.\n " +
                "Options: \n" +
                "   -C \n " +
                "        Chooses the class value if categorical class values.\n" +
                "   -W \n " +
                "        If true opens the NLR visualization window.\n"+
                "   -P \n " +
                "        Is limiting the percentage of data in an n-tile.\n"+
                "   -T \n" +
                "        Enables the numeric or categorical output format of the instances.\n"+
                "   -N \n" +
                "        number of samples per quantile.\n";
    }

    /**
     * Returns the Capabilities of this filter.
     *
     * @return            the capabilities of this object
     * @see               Capabilities
     */
    public Capabilities getCapabilities() {
        //gets called when Filter is chosen in preprocess panel
        Capabilities result = super.getCapabilities();
        result.disableAll();

        // attributes
        result.enableAllAttributes();
        result.enable(Capability.MISSING_VALUES);

        // class
        result.enableAllClasses();
        result.enable(Capability.MISSING_CLASS_VALUES);
        
        return result;
    }
    
    /**
     * Sets the format of the input instances.
     *
     * @param instanceInfo an Instances object containing the input instance structure.
     * @return true because outputFormat can be collected immediately.
     * @throws Exception if the input format can't be set successfully.
     */
    public boolean setInputFormat(Instances instanceInfo) throws Exception {
        //gets called when filter is applied in preprocess panel
        super.setInputFormat(instanceInfo);
        setOutputFormat(instanceInfo);
        return true;
    }

    /**
     * Input an instance for filtering. Filter requires all training instances be read before producing output.
     *
     * @param instance the input instance.
     * @return true if the filtered instance may now be collected with output().
     * @throws IllegalStateException if no input structure has been defined.
     */
    public boolean input(Instance instance) {
        // gets called from filter.java: use Filter
        if (getInputFormat() == null) {
            throw new IllegalStateException("No input instance format defined");
        }
        if (m_NewBatch) {
            resetQueue();
            m_NewBatch = false;
        }
        if (isFirstBatchDone()) {
            push(instance);
            return true;
        } else {
            bufferInput(instance);
            return false;
        }
    }


    /**
     * Signify that this batch of input to the filter is finished.
     * Takes instances and reassemble them.
     * Output() may be called to retrieve the filtered instances.
     *
     * @return true if there are instances pending output
     * @throws IllegalStateException if no input structure has been defined
     */
    
    //INFO here the filter gets called
    public boolean batchFinished() throws Exception {

    if (getInputFormat() == null) {
      throw new IllegalStateException("No input instance format defined");
    }

    Instances instances;
    
   // classVariable
   
    
    Instances inst2 = getOutputFormat();

    boolean b = m_OutputQueue.empty();
    Instance i1 = outputPeek();



   /* if (!isFirstBatchDone()) {*/
        String targetVariable = null;
        
        // INFO here the filter gets the data
        instances = getInputFormat();
        
        if (isFirstBatchDone()&&!b) 
        {
            // use the filter
            instances.add(i1);
        }
        
        ArrayList<SerializedNumericInstances> serializedNumericInstances = new ArrayList<SerializedNumericInstances>();//new
        ArrayList<SerializedNominalInstances> serializedNominalInstances = new ArrayList<SerializedNominalInstances>();//new
        if(getTargetType().getSelectedTag().getReadable().equals("binary")){
             if (!isFirstBatchDone()) 
                 m_numericFilter = new BinaryFilterOuput(this);
            // merke dir das modell aus dem ersten Batch
            // ups ist das pro attribut??
             //INFO serializedNumericInstances contains the NLR info Table from PieceWiseLinearFunction
            instances = m_numericFilter.set(instances, serializedNumericInstances, serializedNominalInstances, getClassValue(), getPercentOfDataSet(), isFirstBatchDone());
            targetVariable = m_numericFilter.targetVariable;
            
            

            
        }
        else if(getTargetType().getSelectedTag().getReadable().equals("nominal")){
            m_binomialFilter = new NominalFilterOutput(this);
            instances = m_binomialFilter.set(instances, serializedNumericInstances, serializedNominalInstances, getClassValue(), getPercentOfDataSet(), isFirstBatchDone() );
            targetVariable = m_binomialFilter.targetVariable;
        }
       
        //classVariable.setUpper(instances.numAttributes());
        Attribute classAttribute =  instances.attribute(instances.numAttributes()-1);
        
        if(getIFCWindow()==true && !isFirstBatchDone()){
            
            //INFO transmit NLR table here
            IFCFilterFrame ifcFrame = new IFCFilterFrame(serializedNumericInstances, serializedNominalInstances, classAttribute.name());
        }
        setInputFormat(instances);
        Enumeration instanceEnum = instances.enumerateInstances();
        while(instanceEnum.hasMoreElements()) {
            Instance instance = (Instance) instanceEnum.nextElement();
            push((Instance) instance.copy());
        }
   /* }
    else {
        instances = getInputFormat();
        Enumeration instanceEnum = instances.enumerateInstances();
        while(instanceEnum.hasMoreElements()) {
            Instance instance = (Instance) instanceEnum.nextElement();
            push((Instance) instance.copy());
        }
    } */

    flushInput();
    m_NewBatch = true;
    m_FirstBatchDone = true;
    return (numPendingOutput() != 0);
  }


  /**
   * Main method for testing this class.
   *
   * @param argv should contain arguments to the filter: use -h for help
   */
  public static void main(String [] argv) {
    runFilter(new IFC_Filter(), argv);
  }

  /**
   * Gets an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  public Enumeration listOptions() {
      Vector newVector = new Vector(6);
      String		param;
      SelectedTag		tag;
      newVector.addElement(new Option(
              "\tOpens a window with fuzzy values\n",
	      "W", 0, "-W <num>"));
      newVector.addElement(new Option(
              "\tPercent of data set for ntil",
              "P", 1, "-P <num>"));
      param = "";
      for (int i = 0; i < TAGS_TARGET_TYPE.length; i++) {
          if (i > 0)
              param += "|";
          tag = new SelectedTag(TAGS_TARGET_TYPE[i].getID(), TAGS_TARGET_TYPE);
          param += tag.getSelectedTag().getReadable();
      }
      newVector.addElement(new Option(
	"\tThe algorithm to use.\n"
	+ "\t(default: PLS1)",
	"T", 1, "-T <" + param + ">")); 
      newVector.addElement(new Option(
	"\tNumber of samples per quantile\n"
	+ "\t(default: 5",
	"N", 1, "-N <numberOfSamples>"));
      return newVector.elements();
  }

  /**
   * Parses the options for this object. <p/>
   *
   <!-- options-start -->
   * Valid options are: <p/>
   *
   * <pre> -C
   *  Chooses the class value if categorical class values.</pre>
   *
   * <pre> -W
   *  If true opens the NLR visualization window.</pre>
   *
   * <pre> -P
   *  Is limiting the percentage of data in an n-tile.</pre>
   *
   * <pre> -T
   *  Enables the numeric or categorical output format of the instances.</pre>
   *
   * <pre> -N
   *  Minimum number of samples per quantile </pre>
   *
   <!-- options-end -->
   *
   * @param options	the options to use
   * @throws Exception	if the option setting fails
   */
  public void setOptions(String[] options) throws Exception {
      String	tmpStr;
      String classValue = Utils.getOption('C', options);
      if (classValue.length() != 0) {
          setClassValue(classValue);
      }
      else {
          setClassValue("first");
      }
      String classIndexString = Utils.getOption('C', options);
      if (classIndexString.length() != 0) {
          setClassValue(classIndexString);
      }
      else {
          setClassValue("1");
      }
      String attIndex = Utils.getOption('P', options);
      if (attIndex.length() != 0) {
          setPercentOfDataSet(attIndex);
      } else {
          setPercentOfDataSet("4");
      }
       attIndex = Utils.getOption('W', options);
      if (attIndex.length() != 0) {
          setIFCWindow(Utils.getFlag('W', options));
      } else {
          setIFCWindow(false);
      }
      tmpStr = Utils.getOption("T", options);
      if (tmpStr.length() != 0)
          setTargetType(new SelectedTag(tmpStr, TAGS_TARGET_TYPE));
      else
          setTargetType(new SelectedTag(TARGET_TYPE_BINOMIAL, TAGS_TARGET_TYPE));
      String numSamp = Utils.getOption('N', options);
      if (attIndex.length() != 0) {
          setMinNumberOfSamples(numSamp);
      } else {
          setMinNumberOfSamples("5");
      }
  }
  
  /**
   * Returns the options of the current setup.
   *
   * @return      the current options
   */
  public String[] getOptions() {
      String [] options = new String [10];
      int current = 0;
      options[current++] = "-C";
      options[current++] = "" + (getClassValue());
      options[current++] = "-W";
      options[current++] = "" + (getIFCWindow());
      options[current++] = "-P";
      options[current++] = "" + (getPercentOfDataSet());
      options[current++] = "-T";
      options[current++] = "" + getTargetType().getSelectedTag().getReadable();
      options[current++] = "-N";
      options[current++] = "" + (getMinNumberOfSamples());

      return options;
  }

  /**
   * Sets the type of algorithm to use (binomial or categorical).
   *
   * @param value 	the algorithm type
   */
  public void setTargetType(SelectedTag value) {
    //if (value.getTags() == TAGS_TARGET_TYPE) {
      targetType = TARGET_TYPE_BINOMIAL;//value.getSelectedTag().getID();
    //}
  }
  /**
   * Gets the type of algorithm to use (binomial or categorical).
   *
   * @return 		the current algorithm type.
   */
  public SelectedTag getTargetType() {
      return new SelectedTag(targetType, TAGS_TARGET_TYPE);
  }

  /**
   * Sets the percentage of data in an n-tile.
   *
   * @param type        String containing a percentage.
   */
  public void setPercentOfDataSet(String type){
    percentOfDataSet.setSingleIndex(type);
  }

  /**
   * Gets the percentage of data in an n-tile.
   *
   * @return           String containing a percentage.
   */
  public String getPercentOfDataSet() {
    return percentOfDataSet.getSingleIndex();
  }

  /**
   * Sets the settings for the visualization window.
   *
   * @param inverse     boolean it true the window is opening.
   */
  public void setIFCWindow(boolean inverse) {
      openWindow = inverse;
  }

  /**
   * Gets the settings for the visualization window.
   *
   * @param             boolean it true the window is opening.
   */
  //INFO Parameter turns Window on or off
  public boolean getIFCWindow() {
      return openWindow;
      //DEBUG change back later
      //return true;
  }

  /**
   * Sets the class value if categorical.
   *
   * @return        string containing -fist, -last, or a number.
   */
  public String getClassValue() {
      return classVariable.getSingleIndex();
  }

  /**
   * Gets the class value if categorical.
   *
   * @param attIndex    string containing -fist, -last, or a number.
   */
  public void setClassValue(String attIndex) {
      classVariable.setSingleIndex(attIndex);
  }

  /**
   * Gets the class value if categorical.
   *
   * @param attIndex    string containing -fist, -last, or a number.
   */
    public String getMinNumberOfSamples() {
        return minNumberOfSamples.getSingleIndex();
    }
    
    /**
   * Gets the class value if categorical.
   *
   * @param attIndex    string containing -fist, -last, or a number.
   */
    public void setMinNumberOfSamples(String n) {
        minNumberOfSamplesGlobal = new Integer(n);
        classVariable.setSingleIndex(n);
    }
    
}