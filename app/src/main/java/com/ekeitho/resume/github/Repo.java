package com.ekeitho.resume.github;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class Repo  {

    private long id;
    private String name;

    public Repo(long id, String name) {

        this.id = id;
        this.name = name;
    }


    public long getGitId() {
        return this.id;
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