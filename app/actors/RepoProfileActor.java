package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import play.Logger;
import play.libs.Json;
import models.RepositoryCollatedData;
import models.DataFormatter;
import services.RepoProfileService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import services.RepoCommitStatsService;
import services.RepoProfileService;
import models.RepoResultsDisplay;

/**
 * Handles Issues and Collaborators request - for retrieving details for repository page
 * @author Siddhartha Nanda and Hasandeep Singh
 */

public class RepoProfileActor extends AbstractActor{ 

	public RepoCommitStatsService repoCommitService;
	private ActorRef supervisorActor;
	
	static public final class RepoProfileInfo{
		public final String queryString;
		public final String repoID;
		public final RepoProfileService repoService;
		public final String username;

		public RepoProfileInfo(String queryString, String username, String repoID, RepoProfileService repoServ) {
			this.queryString = queryString;
			this.username = username;
			this.repoID = repoID;
			this.repoService = repoServ;
		}
	}
	
	public RepoProfileActor(ActorRef supervisorActor, RepoCommitStatsService repoCommitService) {
		super();
		this.supervisorActor = supervisorActor;
		this.repoCommitService = repoCommitService;
	}

	/**
     * Creates an actor with properties specified using parameters
     * @return A <code>Props</code> object holding actor configuration
     * @author Siddhartha Nanda and Hasandeep Singh
     */
	
	public static Props props(ActorRef supervisorActor, RepoCommitStatsService repoCommitService) {
		return Props.create(RepoProfileActor.class, supervisorActor, repoCommitService );
	}
	
	/**
     * Handles incoming messages for this actor - matches the class of an incoming message and takes appropriate action
     * @return builder object after formation
     * @author Siddhartha Nanda and Hasandeep Singh
     */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(RepoProfileInfo.class, repoProfileInfo -> {
					sendRepoProfileData(repoProfileInfo).thenAcceptAsync(this:: processRepoResults);
					})
				.build();
	}
	
	
	/**
     * Retrieves and send all Issues and Collaborators requests - about retrieving repository details to display
     * @param rpi repoProfileInstance consisting details to call api for issues and collaborators
     * @author Siddhartha Nanda and Hasandeep Singh
     */
	private CompletionStage<JsonNode> sendRepoProfileData(RepoProfileInfo rpi) throws Exception{
			// RepositoryCollatedData repoCollatedData = new RepositoryCollatedData(DataFormatter.searchMasterData.get(rpi.queryString),rpi.queryString, rpi.repoID);        	
		    // rpi.repoService.getIssuesAndCollaboratorsDataFromGitHubApi(repoCollatedData.issue_Url, repoCollatedData, "issues");
		    // rpi.repoService.getIssuesAndCollaboratorsDataFromGitHubApi(repoCollatedData.collaborators_url, repoCollatedData, "collaborators");
		    // sender().tell(repoCollatedData, self());
					
					return repoCommitService.repoCommits(rpi.queryString, rpi.username)
	                .thenApplyAsync(
	                        searchResults -> {
	                        	
	                            ObjectMapper mapper = new ObjectMapper();
	                            ObjectNode searchData = mapper.createObjectNode();
	                            searchData.put("responseType","repoProfile");
	                            JsonNode searchMapJsonNode = mapper.convertValue(searchResults, JsonNode.class);
	                            searchData.set("searchRepoMap",searchMapJsonNode);
	                            //System.out.println("Inside Repo actor:"+ searchData);
								//supervisorActor.tell(new Messages.RepoProfile(repopro), getSelf());
								return searchData;
	                        }
	                );
	}

	private void processRepoResults(JsonNode repopro) {
        supervisorActor.tell(new Messages.RepoProfile(repopro), getSelf());
    }
}
