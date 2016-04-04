/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. aa
 */
package trollbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author jakub
 */
public class Trollbox {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // log4j init
        org.apache.log4j.BasicConfigurator.configure();
        
        // gate embedded client
        GateClient client = new GateClient();

        // gate client inicialization
        client.preparePipeline();
        
        Socket clientSocket = new Socket("localhost", 5000);
        InputStream is = clientSocket.getInputStream();
        
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) != -1) {
            String output = new String(buffer, 0, read);
            client.run(output);
            
            System.out.println(output);
            System.out.flush();
        };
        clientSocket.close();
    }
}
