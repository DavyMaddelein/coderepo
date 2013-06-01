/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.XML;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Davy
 */
/**
 * Book class stores book information, after parsing the xml
 * @author Ganesh Tiwari
 */
public class Book {
    String lang;
    String title;
    String id;
    String isbn;
    Date regDate;
    String publisher;
    int price;
    List<String> authors;
    public Book(){
        authors=new ArrayList<String>();
    }
    //getters and setters

    void setId(String value) {
        id = value;
    }

    void setLang(String value) {
        lang = value;
    }

    void setPublisher(String value) {
        publisher = value;
    }

    void setIsbn(String tmpValue) {
        isbn = tmpValue;
    }

    void setTitle(String tmpValue) {
        title = tmpValue;
    }

    public List<String> getAuthors() {
        return authors;
    }

    void setPrice(int parseInt) {
        price = parseInt;
    }

    void setRegDate(Date parse) {
        regDate = parse;
    }
}
