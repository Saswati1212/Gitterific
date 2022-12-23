package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/** Initialize collated data - all the variables for repository details 
 * @author Siddhartha Nanda
 *
 */
public class RepositoryCollatedData {
    // key details to be shown
    public String queryString;
    public String repoId;
    public String repoName;
    public String userName;
    public String desc;
    public String noOfIssues;
    public String issue_Url;
    public String collaborators_url;
    
    // other details to be shown
    public String size;
    public String watch;
    public String forks;
    public String lang;
    public String stars;
    public String repoUrl;
    public String createdOn;
    public ArrayList<RepositoryIssues> listOfIssues = new ArrayList<>();
    public ArrayList<RepositoryCollaborators> listOfCollaborators = new ArrayList<>();
    public ArrayList<String> issueTitleList = new ArrayList<>();

    ObjectMapper mapper = new ObjectMapper();
    JsonNode collaboratorsObj = mapper.createObjectNode();
    JsonNode issuesObj = mapper.createObjectNode();

	/**
	 * Initialise repository details page with the details like id, name, collaborators, issues, commits
	 * @author Siddhartha Nanda
	 * @param result	json object containing entire data to assign
	 * @param queryString	user searched term
	 * @param repoId	user repository id
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
    public RepositoryCollatedData(JsonNode result, String queryString, String repoId) throws InterruptedException, ExecutionException {
        this.queryString = queryString;
        this.repoId = repoId;
		for(JsonNode data:result) {
			if(data.get("id").toString().equals(repoId)) {
                this.repoName= data.get("name").toString().substring(1, data.get("name").toString().length()-1);
                this.userName = data.get("owner").findPath("login").toPrettyString().substring(1,data.get("owner").findPath("login").toPrettyString().length()-1);
                this.desc = data.get("description").toString();
                noOfIssues = data.get("open_issues").toString();
                collaborators_url = "https://api.github.com/repos/"+ userName + "/"+ repoName + "/contributors";
                issue_Url = "https://api.github.com/repos/"+ userName + "/"+ repoName + "/issues";
                size = data.get("size").toString();
				stars = data.get("stargazers_count").toString();
				watch = data.get("watchers").toString();
				lang = data.get("language").toString();
                createdOn = data.get("created_at").toString();
				forks = data.get("forks").toString();
				repoUrl = data.get("html_url").toString();
            }
        }
    }

	/**
	 * Add issues or collaborators data based on the entityOption passed to the function
	 * This can be viewed by clicking on any repository name on user profile or main web page.
	 * @author Siddhartha Nanda
	 * @param nodeObject	json object containing entire data of repository details
	 * @param entityOption	issues or collaborators passed
	 * @return boolean value
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
    public boolean getResponseBasedOnQueryParam(JsonNode nodeObject, String entityOption) {
        if(entityOption.equals("collaborators")) {
        	
            for(JsonNode result:nodeObject) {
                RepositoryCollaborators repoCollaborator = new RepositoryCollaborators(result.get("id").toString(), result.get("login").toString(), result.get("avatar_url").toString(), result.get("contributions").toString());
				listOfCollaborators.add(repoCollaborator);
			}
		}
        
        int issueCounter=0;
        
		if(entityOption.equals("issues")) {
			for(JsonNode result:nodeObject) {
				if(issueCounter < 20) {
				    RepositoryIssues issueItem = new RepositoryIssues(result.get("number").toString(), result.get("body").toString(), result.get("title").toString(), result.get("state").toString(), result.get("created_at").toString());
					listOfIssues.add(issueItem);
					
					issueTitleList.add(result.get("title").toString().substring(1, result.get("title").toString().length()-1));
					
					issueCounter++;
				}				
			}
		}
		return true;
	}
}