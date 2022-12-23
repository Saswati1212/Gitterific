package models;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


/**
 * The User class defines the structure of an User
 * @author Saswati Chowdhury
 */


public class User {

    private String login;
    private String name;
    private String repoId;
    List<Repository> repositories;


    /**
     * @return the login of the user
     */
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }


    /**
     * @return the name of the user
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the name of the user
     */
    public String getRepoId() {
        return repoId;
    }
    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }


    /**
     * @return the list of repositories of the user
     */
    public List<Repository> getRepositories() {
        return repositories;
    }
    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }


    /**
     * parsing function to convert jsonNode in the required format
     */
    public static User parseCommunityFromJSON(JsonNode userJSON) {
        User user = new User();

        user.login = userJSON.get("login").asText();
        user.name = userJSON.get("name").asText();
        user.repoId = userJSON.get("id").asText();

        return user;
    }

    /**
     * @return the String format
     */
    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                "repoId='" + repoId + '\'' +
                ", name='" + name + '\'' +
                ", repositories=" + repositories +
                '}';
    }


}