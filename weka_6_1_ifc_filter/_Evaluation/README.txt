Folder Structure
* aggregator: extracts structured data from Weka output text files
* analysis: files related to analysis of machine learning experiments
* arff: contains data files
* bat: automation of Weka IFC cross-validation tests
* output: where the resulting text files with results from Weka will be located

Running the experiments manually
* run ../startweka.bat or ../startweka.sh
* load a data file from ./arff
* apply > classify > meta.FilteredClassifierNormalized01
* choose IFCFilter and a numeric regression model classifier
* press start

Running the experiments in batch mode
* run ./bat/*.bat (each bat file seperately)
* run ./aggregator/aggrebate.bat
* data is then located at ./aggregator/data.tab