package services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import models.DataFormatter;
import models.RepoResults;
import models.RepoResultsDisplay;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;


public class RepoCommitStatsService implements WSBodyReadables, WSBodyWritables{

    DataFormatter formatter = new DataFormatter();
    @Inject
    WSClient ws = null;

	/**
	 * Displays the statistics of top-10 committers of the repository and detailed statistics of their commits. 
	 * @author Hasandeep Singh
	 * @param user
	 * @param repo
	 * @return a list of the users and their commits statistics
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public CompletionStage<List<RepoResultsDisplay>> repoCommits(String repo, String user) throws InterruptedException, ExecutionException 
	{
		//System.out.println(jsonNodeObj.get("owner").findPath("login").toPrettyString());
		return CompletableFuture.supplyAsync(() -> {
			//String user, repo;
			//user = jsonNodeObj.get("owner").findPath("login").toPrettyString();
			//repo = jsonNodeObj.get("name").toString();
			System.out.println(repo);
			WSRequest req = ws.url("https://api.github.com/repos/"+user+"/"+repo+"/commits?per_page=100");
			req.addHeader("Authorization", "Token "+"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");
			req.setMethod("GET");
			try {
				CompletionStage<JsonNode> res = req.get().thenApply(r -> r.asJson());
				JsonNode nodeObject = Json.toJson(res.toCompletableFuture().get());
				System.out.println("request complete");
				return retrieveStatData(user,repo, nodeObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
		 });
	}


	/**
	 * Fetches actual data for commit statistics and processes min, max and average via streams
	 * @author Hasandeep Singh
	 * @param user
	 * @param repos
	 * @param nodeObject
	 * @return a list of the users and their commits statistics
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public List<RepoResultsDisplay> retrieveStatData(String user,String repos,JsonNode nodeObject) throws InterruptedException, ExecutionException 
	{		
		List<RepoResults> entries = new ArrayList<RepoResults>();
		List<RepoResults> rs_med = new ArrayList<RepoResults>();
		List<RepoResultsDisplay> rs = new ArrayList<RepoResultsDisplay>();
		
		Set<String> userSet = new HashSet<>();
		int min,max,avg,delMin,delMax,delAvg,commit_count;

		for(JsonNode data:nodeObject) 
		{
			String sha = data.get("sha").toString();
			rs_med = fetchStats(user,repos,sha);

			for (RepoResults rrm : rs_med) 
			{
				userSet.add(rrm.getUser());
				entries.add(rrm);
			}
		}
		
		for (String userStr : userSet) {
			final String k = userStr;
			List<RepoResults> rs_intr_med = new ArrayList<RepoResults>();
			rs_intr_med = entries.stream()
							.filter(usr -> usr.getUser().equals(k))
							.collect(Collectors.toList());
			
			commit_count = (int) rs_intr_med.stream()
					.count();

			if (commit_count > 100 )
				commit_count = 100;
			
			min = rs_intr_med.stream()
			.map(RepoResults::getAddt)
			.mapToInt(value -> value).min().orElse(0);		
			
			max = rs_intr_med.stream()
			.map(RepoResults::getAddt)
			.mapToInt(value -> value).max().orElse(0);		
			
			delMin = rs_intr_med.stream()
			.map(RepoResults::getDel)
			.mapToInt(value -> value).min().orElse(0);		
			
			delMax = rs_intr_med.stream()
			.map(RepoResults::getDel)
			.mapToInt(value -> value).max().orElse(0);

			avg = (int) rs_intr_med.stream()
			.map(RepoResults::getAddt)
			.mapToInt(value -> value).average().orElse(0);
			
			delAvg = (int) rs_intr_med.stream()
			.map(RepoResults::getDel)
			.mapToInt(value -> value).average().orElse(0);
			
			RepoResultsDisplay rslt = new RepoResultsDisplay(userStr,min,max,avg,delMin,delMax,delAvg,commit_count);
			rs.add(new RepoResultsDisplay(userStr,min,max,avg,delMin,delMax,delAvg,commit_count));
			System.out.println("Committer "+rslt.getUser()+"addition-min "+rslt.getMin()+"addition-man"+rslt.getMax()+" addition-avg"+rslt.getAvg()
			+" deletion-min "+rslt.getDelMin()+"deletion-max "+rslt.getDelMax()+"deletion-avg"+rslt.getDelAvg());
		}
		
		return rs;
	}
	
	/**
	 * Fetches the api page for the commit statistics
	 * @author Hasandeep Singh
	 * @param user
	 * @param repo
	 * @param sha
	 * @return a list of statistics for a commit sha i.e. list of every single addition and deletion of a committer
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public List<RepoResults> fetchStats(String user, String repo, String sha) throws InterruptedException, ExecutionException 
	{
		sha = sha.replaceAll("^\"|\"$", "");
		String url = "https://api.github.com/repos/"+user+"/"+repo+"/commits/"+sha;
		WSRequest req = ws.url(url);
		req.addHeader("Authorization", "Token "+"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");
		req.addHeader("Accept", "application/vnd.github.v3+json");
		req.setMethod("GET");
		CompletionStage<JsonNode> res = req.get().thenApply(r -> r.asJson());
		JsonNode nodeObject = Json.toJson(res.toCompletableFuture().get());
		return formatter.retrieveStats(user,repo, nodeObject);
	}
}
