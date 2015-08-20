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

