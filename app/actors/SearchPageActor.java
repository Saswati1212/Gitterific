package actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ApiResults;
import models.Repository;
import play.cache.AsyncCacheApi;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import services.SearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class SearchPageActor extends AbstractActorWithTimers {

    private ActorRef sessionActor;
    private SearchService githubService;
    private AsyncCacheApi asyncCacheApi;
    private String phrase;
    private List<Repository> baseSearchResults;

    public SearchPageActor(ActorRef sessionActor, SearchService githubService, AsyncCacheApi asyncCacheApi) {
        this.sessionActor = sessionActor;
        this.githubService = githubService;
        this.asyncCacheApi=asyncCacheApi;
    }

    public static Props props(ActorRef sessionActor, SearchService githubService, AsyncCacheApi asyncCacheApi) {
        return Props.create(SearchPageActor.class, sessionActor, githubService , asyncCacheApi);
    }

    @Override
    public void preStart() {
        System.out.println("SearchPageActor actor created.");
    }


//    @Override
//    public Receive createReceive() {
//        return receiveBuilder()
//                .match(Messages.SearchPageActor.class, searchPageActorQuery -> {
//                    onGetSearch(searchPageActorQuery).thenAcceptAsync(this::processSearchResult);
//                    getTimers().startTimerAtFixedRate("searchPage",
//                            new Messages.SearchPageActor(this.phrase),
//                            FiniteDuration.create(10, TimeUnit.SECONDS));
//                            //Duration.create(10, TimeUnit.SECONDS));
//                })
//                .build();
//    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.SearchPageActor.class, searchPageActorQuery -> {
                    onGetSearch(searchPageActorQuery).thenAcceptAsync(this::processSearchResult);
                    getTimers().startPeriodicTimer("searchPage",
                            new Messages.SearchPageActor(this.phrase),
                            Duration.create(10, TimeUnit.SECONDS));
                })
                .build();
    }




    private CompletionStage<JsonNode> onGetSearch(Messages.SearchPageActor searchPageActor) throws Exception {
        System.out.println("ongetsearch");
        return asyncCacheApi.getOrElseUpdate(searchPageActor.phrase,
                        () -> githubService.getSearchResults(searchPageActor.phrase))
                .thenApplyAsync(
                        searchResults -> {
                            ObjectMapper mapper = new ObjectMapper();
                            ObjectNode searchData = mapper.createObjectNode();
                            searchData.put("responseType", "searchResults");
                            JsonNode searchMapJsonNode = mapper.convertValue(searchResults, JsonNode.class);
                            searchData.set("searchMap",searchMapJsonNode);
                            System.out.println("Inside actor:"+ searchData);
                            return searchData;
                        }
                );
    }

    private void processSearchResult(JsonNode searchResult) {
        sessionActor.tell(new Messages.SearchResult(searchResult), getSelf());
    }






}
