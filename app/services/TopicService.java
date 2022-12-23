package services;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.ws.WSRequest;
import play.libs.Json;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import java.io.IOException;
import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import models.ApiResults;
import models.DataFormatter;
import models.TopicResult;

/**
 * Service class to get list of repository based on topic
 * @author Urvish Tanti 
 * @return repository list
 */

public class TopicService {

    @Inject
    WSClient ws = null;
    DataFormatter formatter = new DataFormatter();

    String url = "https://api.github.com/";

    /**
     * This method is used to display User profile page with their name, id and all repository list
     * @author Urvish Tanti
     * @param topic      name of topic
     * @return          list of all repositories based on searched topic along with username and topics included.
     * @throws ExecutionException
     * @throws InterruptedException
     */

    public CompletionStage<List<TopicResult>> getRepoByTopics(String topic) throws InterruptedException,ExecutionException {
        // search/repositories?q=topic:haskell
        System.out.println("Here in the service");
        return CompletableFuture.supplyAsync(()->{

            CompletableFuture<JsonNode> jsonNodeTopicRepo = ws.url (this.url + "search/repositories?sort=updated&q=topic:" + topic)
                    .get ()
                    .thenApply (r->r.getBody ())
                    .thenApply (j -> Json.parse(j)).toCompletableFuture();

            List<TopicResult> repoList = new ArrayList<>();

            try {
                if (jsonNodeTopicRepo.get().get("items").size() < 10) {
                    for (JsonNode singleRepo : jsonNodeTopicRepo.get().get("items")) {
                        TopicResult repoTemp = TopicResult.parseRepositoryFromJSON(singleRepo);
                        repoList.add(repoTemp);
                    }
                } else {
                    for (int i = 0;i < 10; i++) {
                        JsonNode singleRepo = jsonNodeTopicRepo.get().get("items").get(i);
                        TopicResult repoTemp = TopicResult.parseRepositoryFromJSON(singleRepo);
                        repoList.add(repoTemp);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("topic List"+repoList);
            return repoList;
        });
    }
}