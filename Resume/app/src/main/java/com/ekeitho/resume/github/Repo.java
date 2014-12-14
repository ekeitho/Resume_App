package com.ekeitho.resume.github;

/**
 *
 */
public class Repo {

    private String name;

    public Repo(String name) {
        this.name = name;
    }

    /**
     *
     * Get's the github repo name
     *
     * @return
     */
    public String getRepoName(){
        return this.name;
    }

}
