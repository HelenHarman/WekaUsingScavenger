/**
 * Based on weka.experiment.CrossValidationResultProducer
 */

package weka.experiment;

import scavenger.*;
import scavenger.app.*;

import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.Arrays;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.Future;

import akka.dispatch.Futures;
import akka.util.Timeout;
import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;


import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Range;
import weka.core.Utils;
import weka.experiment.ClassifierSplitEvaluator;
import weka.experiment.CrossValidationResultProducer;
import weka.experiment.Experiment;
import weka.experiment.InstancesResultListener;
import weka.experiment.PairedCorrectedTTester;
import weka.experiment.PairedTTester;
import weka.experiment.PropertyNode;
import weka.experiment.RandomSplitResultProducer;
import weka.experiment.RegressionSplitEvaluator;
import weka.experiment.ResultMatrix;
import weka.experiment.ResultMatrixPlainText;
import weka.experiment.SplitEvaluator;
import weka.experiment.OutputZipper;


import java.util.Random;


public class ScavengerCrossValidationResultProducer extends CrossValidationResultProducer
{
    @Override
    public void doRun(int run) throws Exception 
    {        
        System.out.println("ScavengerCrossValidationResultProducer : doRun");
        ScavengerSingleApp scavengerApp = ScavengerSingleApp.getInstance();
        
        if (getRawOutput()) {
            if (m_ZipDest == null) {
                m_ZipDest = new OutputZipper(m_OutputFile);
            }
        }
        
        if (m_Instances == null) {
            throw new Exception("No Instances set");
        }
        // Randomize on a copy of the original dataset
        Instances runInstances = new Instances(m_Instances);
        Random random = new Random(run);
        runInstances.randomize(random);
        if (runInstances.classAttribute().isNominal()) {
            runInstances.stratify(m_NumFolds);
        }
        
        List<Future<List<Object>>> futures = new ArrayList<Future<List<Object>>>();
        List<Object[]> keyList = new ArrayList<Object[]>();
        for (int fold = 0; fold < m_NumFolds; fold++) 
        {
            // Add in some fields to the key like run and fold number, dataset name
            Object[] seKey = m_SplitEvaluator.getKey();
            Object[] key = new Object[seKey.length + 3];
            key[0] = Utils.backQuoteChars(m_Instances.relationName());
            key[1] = "" + run;
            key[2] = "" + (fold + 1);
            System.arraycopy(seKey, 0, key, 3, seKey.length);
            if (m_ResultListener.isResultRequired(this, key)) 
            {
                keyList.add(key);
                Instances train = runInstances.trainCV(m_NumFolds, fold, random);
                Instances test = runInstances.testCV(m_NumFolds, fold);                
                
                ScavengerFunction<List<Object>> runScavenger = new GetResults(train, test);
                List<Object> resultList = new ArrayList<Object>();
                Computation<List<Object>> computationData = scavengerApp.getComputation().apply("resultList", resultList).cacheGlobally();
                Algorithm<List<Object>, List<Object>> algorithm = scavengerApp.getAlgorithm().expensive("GetResults", runScavenger).cacheGlobally();
                Computation<List<Object>> computation1 = algorithm.apply(computationData);
                Future<List<Object>> future = scavengerApp.getScavengerContext().submit(computation1);
                futures.add(future);
            }
        }
        Future<Iterable<List<Object>>> allTogether = Futures.sequence(futures, scavengerApp.scavengerContext().executionContext());
        
        
        List<List<Object>> allResults = new ArrayList<List<Object>>();
        try
        {
            allResults = (List<List<Object>>)Await.result(allTogether, (new Timeout(Duration.create(120, "seconds")).duration()));
        }
        catch(Exception e) 
        { 
            e.printStackTrace(); 
        }
        
        for(int i = 0; i < keyList.size(); i++)
        {            
            m_ResultListener.acceptResult(this, keyList.get(i), allResults.get(i).toArray(new Object[allResults.get(i).size()]));
        }
    }
    
    
    class GetResults extends ScavengerFunction<List<Object>> 
    {
        Instances train;
        Instances test;
        public GetResults(Instances train, Instances test)
        {
            this.train = train;
            this.test = test;
        }
        public List<Object> call()
        {
            try {
                Object[] seResults = m_SplitEvaluator.getResult(train, test);
                Object[] results = new Object[seResults.length + 1];
                results[0] = getTimestamp();
                System.arraycopy(seResults, 0, results, 1, seResults.length);
                return Arrays.asList(results);
            } catch (Exception ex) {
                return new ArrayList<Object>();
            }
            
        }
    }
    
    
}
