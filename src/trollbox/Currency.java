/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trollbox;

/**
 *
 * @author jakub
 */
public class Currency {
    private String name;
    private int id;
    private int count;

    public Currency(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public Currency(String name, int id) {
        this.name = name;
        this.id = id;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    

}
