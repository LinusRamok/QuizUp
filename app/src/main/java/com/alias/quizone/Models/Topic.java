package com.alias.quizone.Models;



import java.io.Serializable;

public class Topic implements Serializable
{

    public String name;
    public String url;
    public String description;
    private final static long serialVersionUID = -4797064527686704856L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Topic() {
    }

    /**
     *
     * @param description
     * @param name
     * @param url
     */
    public Topic(String name, String url, String description) {
        super();
        this.name = name;
        this.url = url;
        this.description = description;
    }

}