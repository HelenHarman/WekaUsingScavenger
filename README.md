#Weka and Scavenger

A verison of Weka's weka.experiment.CrossValidationResultProducer which makes use of scavenger.

Each fold will be performed in parallel, and results will be cached on the Master node. When running the same computation multiple times the results from the cache will be returned. 

Scavenger : https://github.com/joergwicker/scavenger/tree/javaDevUnstable

Weka : http://www.cs.waikato.ac.nz/ml/index.html

##Compile and Run

This package must be compile using Java 8. (java.util.function.Function is not avaliable in Java 7)

To compile the package the lib/ directory must contain the scavenger and WEKA JAR files.

 
###Creating the Package and JAR file

If you have not edited the code then this section can be skiped.

Run the following command :

```
ant make_package -Dpackage=2015.08.31
```

###Running From Command Line

For the following commands the classpath should contain *dist/\<version\>/\<version\>.jar* and *lib/\**. For example *dist/2015.08.31/2015.08.31.jar:lib/\**

Run a scavenger seed :

```
java scavenger.app.SeedMain

```

Run a scavenger worker :

```
java scavenger.app.WorkerMain

```

Run the ExperimentDemo :

```
java wekaexamples.experiment.ExperimentDemo -classifier weka.classifiers.trees.J48 -exptype classification -splittyperossvalidation -runs 10 -folds 10 -result results.arff -t vote.arff -t iris.arff
```

###Run Using Weka GUI

To run WekaScavenger from within Weka a package should be created and installed into Weka. 

####Installing the package

This package can be installed using the Weka package manager (https://weka.wikispaces.com/How+do+I+use+the+package+manager%3F). Using the Weka GUI :

1. Tools > Package Manger
2. Click File/URL (located in top right)
3. Select the dist/2015.08.31.zip and click OK. (Don't forget to restart Weka)
4. Copy the scavenger.conf file to your Weka home directory.

####Running WekaScavenger

Start Seed and Worker nodes via the command line, as shown above (in "Running From Command Line").

Running master node in Weka :

1. Open the Experimenter window
2. Select Advanced, then click new
3. From the "Result generator" section, Choose ScavengerCrossValidationResultProducer
4. The rest of the options can be selected as usual (http://weka.sourceforge.net/doc.dev/weka/experiment/CrossValidationSplitResultProducer.html) 
5. Finally "Run" and "Analyse" results as usual.


Note : You may need to run Weka.jar rather than the Weka.app due to a "Usupported major.minor version 52.0" error.
