

rem linreg ifc
java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.meta.FilteredClassifierNormalized -t ../arff/sonar_class=mine.arff -x 10 -F "weka.filters.supervised.attribute.IFC_Filter " -W weka.classifiers.functions.LinearRegression > ../output/test_sonar_linreg_ifc.txt

rem linreg nonifc
java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.functions.LinearRegression -S 0 -R 1.0E-8 -t ../arff/sonar_class=mine.arff -x 10 > ../output/test_sonar_linreg_nonifc.txt

rem gaussian ifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.meta.FilteredClassifierNormalized -t ../arff/sonar_class=mine.arff -x 10 -F "weka.filters.supervised.attribute.IFC_Filter " -W weka.classifiers.functions.GaussianProcesses > ../output/test_sonar_gaussian_ifc.txt

rem gaussian nonifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.functions.GaussianProcesses -t ../arff/sonar_class=mine.arff -x 10 > ../output/test_sonar_gaussian_nonifc.txt

rem m5p ifc
java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.meta.FilteredClassifierNormalized -t ../arff/sonar_class=mine.arff -x 10 -F "weka.filters.supervised.attribute.IFC_Filter " -W weka.classifiers.trees.M5P > ../output/test_sonar_m5p_ifc.txt

rem m5p nonifc
java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.trees.M5P -t ../arff/sonar_class=mine.arff -x 10 > ../output/test_sonar_m5p_nonifc.txt

rem mlp ifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.meta.FilteredClassifierNormalized -t ../arff/sonar_class=mine.arff -x 10 -F "weka.filters.supervised.attribute.IFC_Filter " -W weka.classifiers.functions.MultilayerPerceptron  > ../output/test_sonar_mlp_ifc.txt

rem mlp nonifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.functions.MultilayerPerceptron -t ../arff/sonar_class=mine.arff -x 10 > ../output/test_sonar_mlp_nonifc.txt

rem smoreg ifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.meta.FilteredClassifierNormalized -t ../arff/sonar_class=mine.arff -x 10 -F "weka.filters.supervised.attribute.IFC_Filter " -W weka.classifiers.functions.SMOreg  > ../output/test_sonar_smoreg_ifc.txt

rem smoreg nonifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.functions.SMOreg -t ../arff/sonar_class=mine.arff -x 10 > ../output/test_sonar_smoreg_nonifc.txt

rem ibk ifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.meta.FilteredClassifierNormalized -t ../arff/sonar_class=mine.arff -x 10 -F "weka.filters.supervised.attribute.IFC_Filter " -W weka.classifiers.lazy.IBk  > ../output/test_sonar_ibk_ifc.txt

rem ibk nonifc
rem java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.lazy.IBk -t ../arff/sonar_class=mine.arff -x 10 > ../output/test_sonar_ibk_nonifc.txt

rem logreg ifc
java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.meta.FilteredClassifierNormalized -t ../arff/sonar_class=mine.arff -x 10 -F "weka.filters.supervised.attribute.IFC_Filter " -W weka.classifiers.functions.LogisticForIFC > ../output/test_sonar_logreg_ifc.txt

rem logreg nonifc
java -Xmx1536m -cp ../.././build/classes;../.././lib/* weka.classifiers.functions.LogisticForIFC -t ../arff/sonar_class=mine.arff -x 10 > ../output/test_sonar_logreg_nonifc.txt


