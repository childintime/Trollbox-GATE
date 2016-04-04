/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trollbox;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CreoleRegister;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Node;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author jakub
 */
public class GateClient {
    
    // corpus pipeline
    private static SerialAnalyserController annotationPipeline = null;
    
    // whether the GATE is initialised
    private static boolean isGateInitilised = false;
    
    public void run(String chatString) throws InterruptedException {
        //Thread.sleep(5000);
        //annotationPipeline.
        try {
            Document test = Factory.newDocument(chatString);
            Corpus corpus = Factory.newCorpus("");
            corpus.add(test);
            //corpus.add(eu);
            //corpus.add(brands);
            //corpus.add(olympic);

            // set the corpus to the pipeline
            annotationPipeline.setCorpus(corpus);

            //run the pipeline
            annotationPipeline.execute();
            
            for(int i=0; i< corpus.size(); i++){
                System.out.println("\n\nDocument cislo: " + (i+1));
                Document doc = corpus.get(i);

                // get the default annotation set
                AnnotationSet as_default = doc.getAnnotations();
//as_default.ge

                
                FeatureMap futureMap = null;
                // get all Token annotations
                
                if (as_default.get("Ethereum",futureMap).size() > 0) {
                    
                }
                
                
                System.out.println("Currency words: " + as_default.get("Currency",futureMap).size());
                System.out.println("Ethereum words: " + as_default.get("Ethereum",futureMap).size());
                System.out.println("Bitcoin words: " + as_default.get("Bitcoin",futureMap).size());
                System.out.println("Down words: " + as_default.get("Down",futureMap).size());
                System.out.println("Up words: " + as_default.get("Up",futureMap).size());
            }
        
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    
    }
        
    public void preparePipeline() throws MalformedURLException{
        
        if(!isGateInitilised){
            
            // initialise GATE
            initialiseGate();            
        }        

        try {                
            // create an instance of a Document Reset processing resource
            ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");

            // create an instance of a English Tokeniser processing resource
            ProcessingResource tokenizerPR = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");

            // create an instance of a Sentence Splitter processing resource
            ProcessingResource sentenceSplitterPR = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");

            
            System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
            // locate the JAPE grammar file
            File japeOrigFile = new File("/Users/jakub/Documents/skola/ddw/jape/trollbox.jape");
            java.net.URI japeURI = japeOrigFile.toURI();
            
            // create feature map for the transducer
            FeatureMap transducerFeatureMap = Factory.newFeatureMap();
            try {
                // set the grammar location
                transducerFeatureMap.put("grammarURL", japeURI.toURL());
                // set the grammar encoding
                transducerFeatureMap.put("encoding", "UTF-8");
            } catch (MalformedURLException e) {
                System.out.println("Malformed URL of JAPE grammar");
                System.out.println(e.toString());
            }
            
            // create an instance of a JAPE Transducer processing resource
            ProcessingResource japeTransducerPR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", transducerFeatureMap);

            
            FeatureMap gazetteerFeatureMap = Factory.newFeatureMap();
            try {
                // set the grammar location
                gazetteerFeatureMap.put("caseSensitive", "false");
                // set the grammar encoding
                gazetteerFeatureMap.put("encoding", "UTF-8");
                File listsFile = new File("/Users/jakub/Documents/skola/ddw/lists/trollbox.def");
                java.net.URI listsURI = listsFile.toURI();
                gazetteerFeatureMap.put("listsURL", listsURI.toURL());
            } catch (MalformedURLException e) {
                System.out.println("Malformed URL of JAPE grammar");
                System.out.println(e.toString());
            }
            ProcessingResource gazetteer = (ProcessingResource) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", gazetteerFeatureMap);
            
            
            // create corpus pipeline
            annotationPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");

            // add the processing resources (modules) to the pipeline
            annotationPipeline.add(documentResetPR);
            annotationPipeline.add(tokenizerPR);
            annotationPipeline.add(gazetteer);
            //annotationPipeline.add(sentenceSplitterPR);
            annotationPipeline.add(japeTransducerPR);
            
            // create a document
           // Document document = Factory.newDocument("This is some text person James Newman");
            
            //Document eu = Factory.newDocument(new File("/Users/jakub/Documents/skola/ddw/docs/eu").toURI().toURL());
            //Document brands = Factory.newDocument(new File("/Users/jakub/Documents/skola/ddw/docs/brands").toURI().toURL());
            //Document olympic = Factory.newDocument(new File("/Users/jakub/Documents/skola/ddw/docs/olympic").toURI().toURL());
            
        // create a corpus and add the document
         
            //Document test = Factory.newDocument("dasfsdfds eth Dump ether btc crashing break pump");
            //Corpus corpus = Factory.newCorpus("");
            //corpus.add(test);
            
            // set the corpus to the pipeline
            //annotationPipeline.setCorpus(corpus);

            //run the pipeline
            //annotationPipeline.execute();

            
            // loop through the documents in the corpus
/*            
            for(int i=0; i< corpus.size(); i++){
                System.out.println("\n\nDocument cislo: " + (i+1));
                Document doc = corpus.get(i);

                // get the default annotation set
                AnnotationSet as_default = doc.getAnnotations();

                
                FeatureMap futureMap = null;
                // get all Token annotations
                
                if (as_default.get("Ethereum",futureMap).size() > 0) {
                    
                }
                
                
                System.out.println("Currency words: " + as_default.get("Currency",futureMap).size());
                System.out.println("Ethereum words: " + as_default.get("Ethereum",futureMap).size());
                System.out.println("Bitcoin words: " + as_default.get("Bitcoin",futureMap).size());
                System.out.println("Down words: " + as_default.get("Down",futureMap).size());
                System.out.println("Up words: " + as_default.get("Up",futureMap).size());
                //doc.ge
                /*
                AnnotationSet annSetTokens = as_default.get("Ethereum",futureMap);
                System.out.println("Number of Country annotations: " + annSetTokens.size());
                
                ArrayList tokenAnnotations = new ArrayList(annSetTokens);

                // looop through the Token annotations
                for(int j = 0; j < tokenAnnotations.size(); ++j) {
                    
                    // get a token annotation
                    Annotation token = (Annotation)tokenAnnotations.get(j);
                    
                    System.out.println(token.getFeatures().get("majorType"));
                    // get the underlying string for the Token
                    Node isaStart = token.getStartNode();
                    Node isaEnd = token.getEndNode();
                    String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                    System.out.println("Country token: " + underlyingString);
                }
                
                annSetTokens = as_default.get("Company",futureMap);
                System.out.println("Number of Company annotations: " + annSetTokens.size());

                tokenAnnotations = new ArrayList(annSetTokens);

                // looop through the Token annotations
                for(int j = 0; j < tokenAnnotations.size(); ++j) {
                    
                    // get a token annotation
                    Annotation token = (Annotation)tokenAnnotations.get(j);
                    
                    
                    // get the underlying string for the Token
                    Node isaStart = token.getStartNode();
                    Node isaEnd = token.getEndNode();
                    String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                    System.out.println("Company token: " + underlyingString);
                }
                
            }
        */
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void initialiseGate() {
        
        try {
            // set GATE home folder
            File gateHomeFile = new File("/Applications/GATE_Developer_8.1");
            Gate.setGateHome(gateHomeFile);
            
            // set GATE plugins folder
            File pluginsHome = new File("/Applications/GATE_Developer_8.1/plugins");
            Gate.setPluginsHome(pluginsHome);            
            
            // set user config file (optional)
            Gate.setUserConfigFile(new File("/Applications/GATE_Developer_8.1", "user.xml"));            
            
            // initialise the GATE library
            Gate.init();
            
            // load ANNIE plugin
            CreoleRegister register = Gate.getCreoleRegister();
            URL annieHome = new File(pluginsHome, "ANNIE").toURI().toURL();
            register.registerDirectories(annieHome);
            
            // flag that GATE was successfuly initialised
            isGateInitilised = true;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}


