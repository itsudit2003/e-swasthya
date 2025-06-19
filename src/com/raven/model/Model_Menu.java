/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.model;

import javax.swing.*;

/**
 *
 * @author Lenovo
 */
public class Model_Menu {


    /**
     * @return the icon
     */
 
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public MenuType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(MenuType type) {
        this.type = type;
    }
    
    public Model_Menu(String name, MenuType type){
//        this.icon = icon;
        this.name = name;
        this.type = type;
        
    }
    private String icon;
    private String name;
    private MenuType type;
    

    public static enum MenuType{
    TITLE, MENU, EMPTY
}

}
