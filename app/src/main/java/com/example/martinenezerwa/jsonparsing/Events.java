package com.example.martinenezerwa.jsonparsing;

/**
 * Created by martine.nezerwa on 9/29/16.
 * Model class for the events title and images. The data will be fed to the ListView.
 */
public class Events
{
    private String title;
    private String image;

    public Events()
    {
    }

    //Getters and setters
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getImage()
    {
        return image;
    }
    public void setImage(String image)
    {
        this.image = image;
    }
}
