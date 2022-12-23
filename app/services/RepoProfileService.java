package services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import models.RepositoryCollatedData;


/**
 * This service class contains method to compute repository details.
 * @author Siddhartha Nanda
 */

public class RepoProfileService implements WSBodyReadables, WSBodyWritables{

	private WSClient ws;
	
	@Inject
	public RepoProfileService(WSClient ws) {
		this.ws = ws;
	}
	
	/**
	 * Fetching repository's issue and Collaborator details
	 * @author Siddhartha Nanda
	 * @param query
	 * @param rp
	 * @param Option
	 * @return Return a boolean confirmation message
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public boolean getIssuesAndCollaboratorsDataFromGitHubApi(String query, RepositoryCollatedData rp, String Option) throws InterruptedException, ExecutionException {
		WSRequest req = ws.url(query);
		req.setMethod("GET");
		CompletionStage<JsonNode> res = req.get().thenApply(r -> r.asJson());
		System.out.println("res" + res);
		JsonNode nodeObject = Json.toJson(res.toCompletableFuture().get());
		return rp.getResponseBasedOnQueryParam(nodeObject, Option);
	}
	
}