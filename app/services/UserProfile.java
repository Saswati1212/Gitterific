package services;


import models.Repository;
import models.User;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;


/**
 * This UserProfile class uses the Github API to fetch the user related information
 * including username and its repository lists
 * @author Saswati Chowdhury
 *
 */
public class UserProfile implements WSBodyReadables, WSBodyWritables {

    @Inject
    WSClient ws = null;

    String url = "https://api.github.com/";


    /**
     * This method is used to display User profile page with their name, id and all repository list
     * @author Saswati Chowdhury
     * @param name      the author who owns all the repositories
     * @return          all the repository list of the provided Username along with his details
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletionStage<User> getUserProfile(String name) throws ExecutionException, InterruptedException {

        return CompletableFuture.supplyAsync(() -> {

        CompletableFuture<JsonNode> jsonUser = ws.url(this.url + "users/" + name)
                .get()
                .thenApply(x -> x.getBody())
                .thenApply(y -> Json.parse(y)).toCompletableFuture();

        CompletableFuture<JsonNode> jsonRepo = ws.url(this.url + "users/" + name + "/repos")
                .get()
                .thenApply(x -> x.getBody())
                .thenApply(y -> Json.parse(y)).toCompletableFuture();


            User user = null;
            try {
                user = User.parseCommunityFromJSON(jsonUser.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            List<Repository> allRepos = new ArrayList<Repository>();

            try {
                for (JsonNode jn : jsonRepo.get()) {
                    Repository userRepos = Repository.parseRepositoryFromJSON(jn);
                    allRepos.add(userRepos);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            user.setRepositories(allRepos);
            return user;

    });

    }
}














