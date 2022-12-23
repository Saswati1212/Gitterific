package models;

import java.util.concurrent.ExecutionException;


/** Initialize repository issue data for repository detail page 
 * @author Siddhartha Nanda
 */
public class RepositoryIssues{
	
	public String repoIssueNo;
	public String repoIssueContent;
	public String repoIssueTitle;
	public String repoIssueState;
	public String repoIssueCreatedOn;

	/**
	 * Initialise repository issue section with issue details
	 * @author Siddhartha Nanda
	 * @param issueNo	issue number of particular repository
	 * @param body	content of particular issue
	 * @param title	issue title
	 * @param state	open or closed state of an issue
	 * @param createdOn	date at which issue was created
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public RepositoryIssues(String issueNo, String body, String title, String state, String createdOn) {
        repoIssueNo = issueNo;
		repoIssueContent = body.substring(1, body.length()-1);;
		repoIssueTitle = title.substring(1, title.length()-1);;
		repoIssueState = state.substring(1, state.length()-1);
		repoIssueCreatedOn = createdOn.substring(1, createdOn.length()-1);;
	}
	
}