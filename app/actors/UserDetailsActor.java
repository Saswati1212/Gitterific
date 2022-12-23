package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.cache.AsyncCacheApi;
import scala.concurrent.duration.Duration;
import services.UserProfile;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Handles all user profile feature related requests - about retrieving user profile and repositories information
 * @author Saswati Chowdhury
 */

public class UserDetailsActor extends AbstractActorWithTimers {

    private ActorRef supervisorActor;
    private AsyncCacheApi asyncCacheApi;
    private UserProfile userGithubService;
    private String userName;


    /**
     * @param supervisorActor Actor reference for the supervisor actor
     * @param userGithubService service used to fetch user details
     * @param asyncCacheApi For temporary data storage
     * @param userName For User Name
     */
    public UserDetailsActor(ActorRef supervisorActor, UserProfile userGithubService, AsyncCacheApi asyncCacheApi, String userName) {
        this.supervisorActor = supervisorActor;
        this.userGithubService = userGithubService;
        this.asyncCacheApi = asyncCacheApi;
        this.userName = userName;
    }


    /**
     * Creates an actor with properties specified using parameters
     * @param supervisorActor Actor reference for the supervisor actor
     * @param userGithubService service used to fetch user details
     * @param asyncCacheApi For temporary data storage
     * @return A <code>Props</code> object holding actor configuration
     * @author Saswati Chowdhury
     */


    public static Props props(ActorRef supervisorActor, UserProfile userGithubService, AsyncCacheApi asyncCacheApi, String userName) {
        return Props.create(UserDetailsActor.class, supervisorActor, userGithubService , asyncCacheApi, userName);
    }


    /**
     * Executes before any other action related to this actor
     * @author Saswati Chowdhury
     */
    @Override
    public void preStart() {
        System.out.println("User actor created.");
    }


    /**
     * Handles incoming messages for this actor - matches the class of an incoming message and takes appropriate action
     * @return builder object after formation
     * @author Saswati Chowdhury
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.GetUserDetails.class, userInfo -> {
                    onGetUserSearch(userInfo).thenAcceptAsync(this::processUserResult);
                    getTimers().startPeriodicTimer("userDetails",
                            new Messages.GetUserDetails(this.userName),
                            Duration.create(10, TimeUnit.SECONDS));
                })
                .build();
    }

    /**
     * Retrieves all available public profile information about a user, as well as all the repositories of that user
     * @param userRequest request object consisting username
     * @return JsonNode of the user details searched with repository list
     * @throws Exception If the call cannot be completed due to an error
     * @author Saswati Chowdhury
     */

    private CompletionStage<JsonNode> onGetUserSearch(Messages.GetUserDetails userRequest) throws Exception {

        return asyncCacheApi.getOrElseUpdate(userRequest.username ,
                        () -> userGithubService.getUserProfile(userRequest.username))
                .thenApplyAsync(
                        searchDetails -> {
                            asyncCacheApi.set(userRequest.username,searchDetails,60*20);
                            ObjectMapper mapper = new ObjectMapper();
                            ObjectNode repositoryData = mapper.createObjectNode();
                            JsonNode repositoryJsonNode = mapper.convertValue(searchDetails, JsonNode.class);
                            repositoryData.put("responseType", "userDetails");
                            repositoryData.set("searchProfile", repositoryJsonNode);
                            System.out.println("Inside user actor my json:" + repositoryData);
                            return repositoryData;
                        }
                );
    }


    /**
     * sends the user details JsonNode to the supervisorActor
     * @param userInfo JsonNode to be displayed on the page
     * @author Saswati Chowdhury
     */
    private void processUserResult(JsonNode userInfo) {
        supervisorActor.tell(new Messages.UserDetails(userInfo), getSelf());
    }
}
