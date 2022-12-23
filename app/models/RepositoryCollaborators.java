package models;

import java.util.concurrent.ExecutionException;

/** Initialize repository collaborator details to be shown
 * @author Siddhartha Nanda
 */
public class RepositoryCollaborators{
	
	public String repoCollaboratorsId;
	public String repoCollaboratorsName;
	public String repoCollaboratorsUrl;
	public String repoCollaboratorsContributions;

	/**
	 * Initialise repository collaborators section with collaborator details
	 * @author Siddhartha Nanda
	 * @param collaboratorsId	Unique id of the contributor
	 * @param collaboratorsName	Name of the contributor'
	 * @param collaboratorsUrl	Url to land at contributor's profile
	 * @param collaboratorsContributions	commits or issues raised by contributor
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */

	public RepositoryCollaborators(String collaboratorsId, String collaboratorsName, String collaboratorsUrl, String collaboratorsContributions) {
		repoCollaboratorsId = collaboratorsId;
		repoCollaboratorsName = collaboratorsName.substring(1, collaboratorsName.length()-1);
		repoCollaboratorsUrl = collaboratorsUrl.substring(1, collaboratorsUrl.length()-1);;
		repoCollaboratorsContributions = collaboratorsContributions;
	}
	
}