package services;

import models.User;
import java.util.UUID;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;
import utils.Cache;

import com.fasterxml.jackson.databind.JsonNode;
import models.ApiResults;
import models.DataFormatter;
import models.RepositoryCollatedData;
import models.TopicResult;
import models.IssuesWordLevelStatistics;
import models.RepoResults;
import models.RepoResultsDisplay;
import services.TopicService;
import services.UserProfile;
import services.WordLevelIssueStats;

public class SearchService {

    //WSClient ws = null;
    @Inject
    WSClient ws = null;
    DataFormatter formatter = new DataFormatter();
    //LinkedHashMap<String, ArrayList<ApiResults>> resultsToDisplay = new LinkedHashMap<String, ArrayList<ApiResults>>();

    public CompletionStage<LinkedHashMap<String, ArrayList<ApiResults>>> getSearchResults(String name) throws ExecutionException, InterruptedException {
        List<ApiResults> entries = new ArrayList<ApiResults>();

        return CompletableFuture.supplyAsync(() -> {
        	System.out.println(name);
            WSRequest cacheRequest = ws.url("https://api.github.com/search/repositories?sort=updated&per_page=10&q=" + name);
            cacheRequest.setMethod("GET");
            CompletionStage<JsonNode> res = cacheRequest.get().thenApply(r -> r.asJson());
            JsonNode nodeObject = null;
            try {
                nodeObject = Json.toJson(res.toCompletableFuture().get().findPath("items"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            LinkedHashMap<String, ArrayList<ApiResults>> stringArrayListLinkedHashMap = null;

            try {
                stringArrayListLinkedHashMap = formatter.retrieveArrayOfData(name, nodeObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            return stringArrayListLinkedHashMap;
        });


    }


}














