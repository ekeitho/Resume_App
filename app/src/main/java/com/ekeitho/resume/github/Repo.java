package com.ekeitho.resume.github;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class Repo  {

    private String name;

    public Repo(String name) {
        this.name = name;
    }


    public void setRepoName(String name) {
        this.name = name;
    }
    /**
     * Get's the github repo name
     *
     * @return the repo name
     */
    public String getRepoName(){
        return this.name;
    }





}