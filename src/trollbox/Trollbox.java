/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. aa
 */
package trollbox;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author me@jakubsamek.cz
 */
public class Trollbox {
    
    static String DatabaseServer = "localhost";     // nasleduji udaje k databazi
    static String DatabaseName = "trollbox-v1";
    static int DatabasePort = 3306;
    static String DatabaseUser = "trollbox";
    static String DatabasePassword = "trollbox";
    static String ConnectionUrl = "jdbc:mysql://" + DatabaseServer + ":" + DatabasePort + "/" + DatabaseName + "?useUnicode=yes&characterEncoding=UTF-8";
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        // log4j init
        org.apache.log4j.BasicConfigurator.configure();
        
        // test pritomnosti jdbc driveru
        try {                                           
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("SUCCESS: Mysql JDBC driver found!");
        } catch (ClassNotFoundException ex) {
            System.out.println("ERROR: Mysql JDBC driver not found!");
            System.exit(1);
        }
        
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(ConnectionUrl, DatabaseUser, DatabasePassword); // etevreni databazove pripojeni
        } catch (SQLException ex) {
            Logger.getLogger(Trollbox.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        SQLfactory sqlFactory = new SQLfactory(connection);
        
        
        
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
            
            JSONObject jsonObject = new JSONObject(output);
            //jsonObjet.getString(1);
           
            ChatMessage chatMessage = client.run(jsonObject);
            sqlFactory.insertChatMessage(chatMessage);
        };
        clientSocket.close();
    }
}
