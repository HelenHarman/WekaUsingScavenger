package weka.experiment;

import scavenger.*;
import scavenger.app.ScavengerAppJ;

import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.Future;

import akka.dispatch.Futures;
import akka.util.Timeout;
import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;


/**
 * A basic example of how to create a Scavenger Java application.
 */
class ScavengerApp extends ScavengerAppJ
{



    public ScavengerApp()
    {
        super();        
    }  
    
    public void endScavengerApp()
    {
        scavengerShutdown();
    }
    
    public package$ getAlgorithm()
    {
        return scavengerAlgorithm;
    }
    
    public Computation$ getComputation()
    {
        return scavengerComputation;
    }
    
    public Context getScavengerContext()
    {
        return scavengerContext();
    }
}

