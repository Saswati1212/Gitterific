package models;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Model class for getting username, repoName, repo Id and topic list in home page
 * @author AK06
 */

public class ApiResults{
	
	public String queryString;
	public String userName;
	public String repoName;
	public String repoId;

	ArrayList<String> topics = new ArrayList<>() ;

	/**
	 * Construtor to intialize ApiResults
	 * @author AK06
	 * @param repo
	 * @param user
	 * @param topic
	 * @param query
	 * @param id
	 */

	
	public ApiResults(String repo, String user,JsonNode topic, String query, String id){
		repoName = repo.substring(1, repo.length()-1);
		userName = user.substring(1, user.length()-1);
		topics = new ObjectMapper().convertValue(topic, ArrayList.class);
		queryString= query;
		repoId = id;
	}

    public ApiResults(String userName, String repoName) {

    }

    /**
	 * getter function to get userName 
	 * @author AK06
	 * @return userName
	 */
		
	public String getUserName() {
		return userName;
	}

	/**
	 * getter function to get repoName 
	 * @author AK06
	 * @return repoName
	 */

	public String getRepoName() {
		return repoName;
	}

	/**
	 * getter function to get list of topics 
	 * @author AK06
	 * @return topics
	 */
	
	public ArrayList<String> getTopics(){
		return topics;
	}
}