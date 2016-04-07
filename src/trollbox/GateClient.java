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
import gate.DocumentExporter;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Node;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ivy.util.Message;
import org.json.JSONArray;
import org.json.JSONObject;



/**
 *
 * @author jakub
 */
public class GateClient {
    
    // corpus pipeline
    private static SerialAnalyserController annotationPipeline = null;
    
    // seznam men, ktere nas zajimaji
    private static List<Currency> currencyToWatch;
    
    // whether the GATE is initialised
    private static boolean isGateInitilised = false;
    
    public ChatMessage run(JSONObject chatJSON) throws InterruptedException, IOException {
        // nova zprava
        ChatMessage chatMessage = new ChatMessage();
        
        chatMessage.setMessage(chatJSON.getString("message"));
        chatMessage.setUsername(chatJSON.getString("username"));
        chatMessage.setKarma(chatJSON.getInt("karma"));
        chatMessage.setMessageid(chatJSON.getInt("message_id"));
        chatMessage.setCurrencyList(new ArrayList<>());
        
        try {
            Document test = Factory.newDocument(chatMessage.getMessage());
            Corpus corpus = Factory.newCorpus("");
            corpus.add(test);
            
            // set the corpus to the pipeline
            annotationPipeline.setCorpus(corpus);

            //run the pipeline
            annotationPipeline.execute();
            
            for(int i=0; i< corpus.size(); i++){
                //System.out.println("\n\nDocument cislo: " + (i+1));
                Document doc = corpus.get(i);

                
                // get the default annotation set
                AnnotationSet as_default = doc.getAnnotations();
                
                FeatureMap futureMap = null;
                
                chatMessage.setCurrencywords(as_default.get("Currency",futureMap).size());
                chatMessage.setUpwords(as_default.get("Up",futureMap).size());
                chatMessage.setDownwords(as_default.get("Down",futureMap).size());
                
                // pokud je ve zprave zminka o nejake mene, ktere sledujeme, tak ji pridat do seznamu
                for (Currency currency : currencyToWatch) {
                    if (as_default.get(currency.getName(),futureMap).size() > 0 ) {
                        chatMessage.getCurrencyList().add(new Currency(currency.getId(), as_default.get(currency.getName(),futureMap).size()));
                    }
                }
            }
        
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return chatMessage;
            
    
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
            //ProcessingResource sentenceSplitterPR = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");

            
            /*System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
            */
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
            
         
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void initialiseGate() {
        // zatim takhle staticky pridane meny vcetne id
        currencyToWatch = new ArrayList<Currency>();
        currencyToWatch.add(new Currency("Ethereum", 1));
        currencyToWatch.add(new Currency("Bitcoin", 2));
        
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


