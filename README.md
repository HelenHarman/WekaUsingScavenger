Weka and Scavenger
====

A verison of Weka's wekaexamples.experiment.CrossValidationResultProducer which makes use of scavenger.

Scavenger : https://github.com/joergwicker/scavenger/tree/javaDevUnstable

Weka : http://www.cs.waikato.ac.nz/ml/index.html

Known to work with Weka version 3.7.12.

Compile and Run
---------------

lib/ directory must contain the scavenger and WEKA JAR files.

Create JAR file :

```
ant
```

Run a scavenger seed :

```
java -cp dist/wekaexamples-java.jar scavenger.app.SeedMain

```

Run a scavenger worker :

```
java -cp dist/wekaexamples-java.jar scavenger.app.WorkerMain

```

Run the ExperimentDemo :

```
java -cp dist/wekaexamples-java.jar wekaexamples.experiment.ExperimentDemo -classifier weka.classifiers.trees.J48 -exptype classification -splittyperossvalidation -runs 10 -folds 10 -result results.arff -t vote.arff -t iris.arff
```

Using Weka
==========

Creating the package
--------------------

Create the JAR file.
Create a zip file containing the following in the root directory : 
- src/*
- lib/*
- build_package.xml
- Description.props
- wekaScaavenger-java.jar (the JAR file located at dist/wekaScaavenger-java.jar)



Installing and Running
----------------------

This package can be installed using the Weka package manager (https://weka.wikispaces.com/How+do+I+use+the+package+manager%3F). 

Copy the scavenger.conf file to your Weka home directory.
Start Seed and Worker nodes via the command line, as shown above.

Running master in Weka :

1. Open the Experimenter window
2. Select Advanced, then new
3. From the "Result generator" section, Choose ScavengerCrossValidationResultProducer
4. The rest of the options can be selected as usual (http://weka.sourceforge.net/doc.dev/weka/experiment/CrossValidationSplitResultProducer.html) 


Note : I have to run Weka.jar rather than the Weka.app due to a "Usupported major.minor version 52.0" error.