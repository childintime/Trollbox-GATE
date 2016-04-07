/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trollbox;

import java.util.List;

/**
 *
 * @author jakub
 */
public class ChatMessage {
    private String message;
    private String username;
    private int messageid;
    private int karma;
    
    private int currencywords;
    private int upwords;
    private int downwords;
    
    private List<Currency> currencyList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMessageid() {
        return messageid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public int getCurrencywords() {
        return currencywords;
    }

    public void setCurrencywords(int currencywords) {
        this.currencywords = currencywords;
    }

    public int getUpwords() {
        return upwords;
    }

    public void setUpwords(int upwords) {
        this.upwords = upwords;
    }

    public int getDownwords() {
        return downwords;
    }

    public void setDownwords(int downwords) {
        this.downwords = downwords;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }
    
    
    
}
