/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Socket clientSocket = new Socket("localhost", 5000);
        InputStream is = clientSocket.getInputStream();
        /*
    PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
    pw.println("GET / HTTP/1.0");
    pw.println();
    pw.flush();
         */
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) != -1) {
            String output = new String(buffer, 0, read);
            System.out.println(output);
            System.out.flush();
        };
        clientSocket.close();
    }
}
