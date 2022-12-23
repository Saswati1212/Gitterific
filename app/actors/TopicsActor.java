package actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.cache.AsyncCacheApi;
import services.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

/**
 * Actor to find the repositories based on the topic queried
 * @author Urvish Tanti
 */

public class TopicsActor extends AbstractActorWithTimers {

    private ActorRef supervisorActor;
    private AsyncCacheApi asyncCacheApi;
    private TopicService topicService;


    /**
     * @author Urvish Tanti
     * @param supervisorActor supervisor actor
    nce of <code>GithubService</code> inteface, used to make external API calls to GitHub
     * @param asyncCacheApi asynchronous chaching
     * @param topicService access topic services
     */

    public TopicsActor(ActorRef supervisorActor, TopicService topicService, AsyncCacheApi asyncCacheApi) {
        this.supervisorActor = supervisorActor;
        this.topicService = topicService;
        this.asyncCacheApi = asyncCacheApi;

    }

    /**
     * Creates a topic-actor with properties passed in parameters
     * @author Urvish Tanti
     * @param supervisorActor Actor reference to the supervisor actor
     tance of <code>GitHubAPI</code> inteface, used to make external API calls to GitHub
     * @param topicService access topic services
     * @param asyncCacheApi asynchronous caching 
     * @return Props with the topic actor's configuration
     */
    public static Props props(ActorRef supervisorActor, TopicService topicService, AsyncCacheApi asyncCacheApi) {
        return Props.create(TopicsActor.class, supervisorActor, topicService , asyncCacheApi);
    }

    /**
     * Method called once directly during the initialization of the first instance of this actor
     */
    @Override
    public void preStart() {
        System.out.println("Topics actor created.");
    }

    /**
     * Match the class of an incoming message and take the appropriate action
     * @return Topics Search Response defined in an <code>AbstractActor.Receive</code>
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.GetRepositoryfromTopic.class, topicsInfo -> {
                    onGetTopicsSearch(topicsInfo).thenAcceptAsync(this::processTopicsResult);
                })
                .build();
    }
    /**
     * Gets search result information for given query and calls processTopicsResult to process it
     * @author Urvish Tanti
     * @param topicsRequest GetRepositoryfromTopic request to retrieve the information
     * @throws Exception 
     */

    private CompletionStage<JsonNode> onGetTopicsSearch(Messages.GetRepositoryfromTopic topicsRequest) throws Exception {
        System.out.println("WHTA THE HELL");

        return asyncCacheApi.getOrElseUpdate(topicsRequest.topicName,
                        () -> {
            return topicService.getRepoByTopics(topicsRequest.topicName);
                        })
                .thenApplyAsync(
                        searchDetails -> {
                            System.out.println("Here i am");
                            ObjectMapper mapper = new ObjectMapper();
                            ObjectNode repositoryData = mapper.createObjectNode();
                            JsonNode repositoryJsonNode = mapper.convertValue(searchDetails, JsonNode.class);
                            repositoryData.put("responseType", "topicsDetails");
                            repositoryData.put("keyword",topicsRequest.topicName);
                            repositoryData.set("searchProfile", repositoryJsonNode);
                            System.out.println("Inside topic actor my json:" + repositoryData);
                            return repositoryData;
                        }
                );
    }

    /**
     * Based on provides search response, creates and sends a JSON response
     * @author Urvish Tanti
     * @param topicInfo Search result containing information about 10 repositories based on the queried topic
     */

    private void processTopicsResult(JsonNode topicInfo) {
        supervisorActor.tell(new Messages.TopicDetails(topicInfo), getSelf());
    }
}
