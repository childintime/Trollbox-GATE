/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trollbox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jakub
 */
public class SQLfactory {
    Connection connection;

    public SQLfactory(Connection con) {
        connection = con;
    }

    public void insertChatMessage(ChatMessage chatMessage) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO message"
                + "(message_id, username, karma, currency, up, down, created, message) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, chatMessage.getMessageid());
        preparedStatement.setString(2, chatMessage.getUsername());
        preparedStatement.setInt(3, chatMessage.getKarma());
        preparedStatement.setInt(4, chatMessage.getCurrencywords());
        preparedStatement.setInt(5, chatMessage.getUpwords());
        preparedStatement.setInt(6, chatMessage.getDownwords());
        preparedStatement.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
        preparedStatement.setString(8, chatMessage.getMessage());
        preparedStatement.executeUpdate();
        
        ResultSet tableKeys = preparedStatement.getGeneratedKeys();
        tableKeys.next();
        int autoGeneratedID = tableKeys.getInt(1);
        preparedStatement.close();
        tableKeys.close();
        
        if (chatMessage.getCurrencyList().size() > 0) {
            for (Currency currency : chatMessage.getCurrencyList()) {
                insertCurrency(currency, autoGeneratedID);
            }
        
        }
    }
    
    public void insertCurrency(Currency currency, int message_id) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO message_has_currency"
                + "(message_id, currency_id) VALUES (?,?)");
        preparedStatement.setInt(1, message_id);
        preparedStatement.setInt(2, currency.getId());
       preparedStatement.executeUpdate();
       preparedStatement.close();
    }
    
    /*
    public void insertZasItem(ZasItem zasItem, int item_id) throws SQLException {
        int subject_id = insertSubject(zasItem.getSbma());

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO itvh "
                + "(item_id, subject_id, quantity, rliv, citpodl, jmenpodl, idma) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, item_id);
        if (subject_id > 0) {
            preparedStatement.setInt(2, subject_id);
        } else {
            System.out.println("davam null");
            preparedStatement.setNull(2, java.sql.Types.NULL);
        }
        preparedStatement.setInt(3, zasItem.getQtet());
        preparedStatement.setString(4, zasItem.getRliv());
        preparedStatement.setInt(5, zasItem.getCitpodl());
        preparedStatement.setInt(6, zasItem.getJmenpodl());
        preparedStatement.setString(7, zasItem.getIdma());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
*/
    
}
