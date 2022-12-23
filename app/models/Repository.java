package models;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * The Repository class to hold the content of a repository
 * @author Saswati Chowdhury
 */

public class Repository {

    private String name;
    private String description;
    private String repoId;


    /** The parameterized constructor */
    public Repository(String name, String description, String repoId){
        this.name = name;
        this.description = description;
        this.repoId = repoId;
    }

    /** The empty constructor */
    public Repository(){}

    /**
     * @return the name of the repository
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the description of the repository
     */
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return the repoid of the repository
     */
    public String getRepoId() {
        return repoId;
    }
    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }


    /**
     * parsing function to convert jsonNode in the required format
     */
    public static Repository parseRepositoryFromJSON(JsonNode repositoryJSON) {
        Repository repository = new Repository();
        repository.name = repositoryJSON.get("name").asText();
        repository.description = repositoryJSON.get("description").asText();
        repository.repoId = repositoryJSON.get("id").asText();

        return repository;
    }

    /**
     * @return the String format
     */
    @Override
    public String toString() {
        return "UserRepository{" +
                "id='" + repoId + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}