package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/** Constructor to initialize TopicResults 
 * @author Urvish Tanti
 */

public class TopicResult implements Serializable {
    private String repoId;
    private String user;
    private List<String> topics;
    private String repo;



    /**
	 * getter function to get repository id 
     * @author Urvish Tanti
	 * @return repoId
	 */

    public String getRepoId() {
        return this.repoId;
    }

      /**
	 * getter function to get repository id 
     * @author Urvish Tanti
	 * @return repoId
	 */

    public void setRepoId(String user) {
        this.repoId = repoId;
    }

      /**
	 * getter function to get userName
     * @author Urvish Tanti
	 * @return user
	 */

    public String getUser() {
        return this.user;
    }

      /**
	 * setter function to set userName
     * @author Urvish Tanti
	 * @return user
	 */

    public void setUser(String user) {
        this.user = user;
    }

      /**
	 * getter function to get topics
     * @author Urvish Tanti
	 * @return topics
	 */

    public List<String> getTopics () {
        return this.topics;
    }

      /**
	 * setter function to set topics
     * @author Urvish Tanti
	 * @return topics
	 */

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

      /**
	 * getter function to get repository 
     * @author Urvish Tanti
	 * @return repo
	 */

    public String getRepo() {
        return this.repo;
    }

      /**
	 * setter function to set repository 
     * @author Urvish Tanti
	 * @return repo
	 */
    public void setRepo (String repo) {
        this.repo = repo;
    }
    
/** parsing function to convert jsonNode to required format 
 * @author Urvish Tanti
 * @param node
 * @return List of repositories 
 */


    public static TopicResult parseRepositoryFromJSON(JsonNode node) {
        TopicResult repo = new TopicResult();
        repo.user = node.get("owner").get("login").asText();
        repo.repo = node.get("name").asText();
        repo.repoId = node.get("id").asText();
        
        List<String> strList = new ArrayList<>();
        
        for (int i = 0; i < node.get("topics").size(); i++) {
            strList.add(node.get("topics").get(i).asText());
        }
        repo.topics = strList;
        return repo;
    }
}
