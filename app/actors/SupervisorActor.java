package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import play.cache.AsyncCacheApi;

import services.RepoCommitStatsService;
import services.SearchService;
import services.UserProfile;
import services.RepoProfileService;
import services.TopicService;
import java.util.HashMap;
import java.util.Map;


public class SupervisorActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final ActorRef wsOut;
    private SearchService githubService;
    private final AsyncCacheApi asyncCacheApi;
    private UserProfile userService;
    private RepoProfileService repoProfileService;
    private TopicService topicService;
    private ActorRef userDetailsActor = null;
    private ActorRef searchPageActor = null;
    private ActorRef topicsSearchActor = null;
    private ActorRef repoProfileActor = null;
    private RepoCommitStatsService repoCommitStats;
    //private final ActorRef UserDetailsActor= null;

    public SupervisorActor(final ActorRef wsOut, SearchService githubService, UserProfile userService,TopicService topicService, RepoCommitStatsService repoCommitStats, RepoProfileService repoProfileService,  AsyncCacheApi asyncCacheApi) {
        this.wsOut =  wsOut;
        this.githubService = githubService;
        this.asyncCacheApi = asyncCacheApi;
        this.userService=userService;
        this.topicService = topicService;
        this.repoCommitStats = repoCommitStats;
        this.repoProfileService = repoProfileService;
    }


    public static Props props(final ActorRef wsout, SearchService githubService, UserProfile userService, TopicService topicService,RepoCommitStatsService repoCommitStats, RepoProfileService repoProfileService,AsyncCacheApi asyncCacheApi) {
        return Props.create(SupervisorActor.class, wsout, githubService,userService, topicService, repoCommitStats, repoProfileService, asyncCacheApi);
    }


    @Override
    public void preStart() {
        System.out.println("Supervisor actor created.");
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JsonNode.class, this::processRequest)
                .match(Messages.UserDetails.class, userDetails -> wsOut.tell(userDetails.userDetails, self()))
                .match(Messages.SearchResult.class, searchResult -> wsOut.tell(searchResult.searchResult, self()))
                .match(Messages.TopicDetails.class,topicSearchInfo->wsOut.tell(topicSearchInfo.topicDetails,self()))
                .match(Messages.RepoProfile.class,repoprofile->wsOut.tell(repoprofile.repopro,self()))
                .matchAny(other -> log.error("Received unknown message type: " + other.getClass()))
                .build();
    }


    private void processRequest(JsonNode receivedJson) {
    	log.info(receivedJson.asText());
        if(receivedJson.has("searchPage")) {

            if (searchPageActor == null) {
                System.out.println("searchpage in supervisor");
                log.info("Creating a search page actor.");
                searchPageActor = getContext().actorOf(SearchPageActor.props(self(), githubService, asyncCacheApi));
            }
            String phrase = receivedJson.get("searchPage").asText();
            searchPageActor.tell(new Messages.SearchPageActor(phrase), getSelf());

        } else if(receivedJson.has("userDetails")) {
            System.out.println("in the super user");
            String username = receivedJson.get("userDetails").asText();
            if(userDetailsActor == null) {
                log.info("Creating a repository profile actor.");
                userDetailsActor = getContext().actorOf(UserDetailsActor.props(self(), userService, asyncCacheApi,username));
            }
            userDetailsActor.tell(new Messages.GetUserDetails(username), getSelf());
        }
        else if(receivedJson.has("topicsDetails")){
            String topicName = receivedJson.get("topicsDetails").asText();
            if(topicsSearchActor == null){
                System.out.println("A topics actor created");
                System.out.println("supervisor topic:+" + topicName);
                topicsSearchActor = getContext().actorOf(TopicsActor.props(self(),topicService,asyncCacheApi));
            }
            topicsSearchActor.tell(new Messages.GetRepositoryfromTopic(topicName),getSelf());
        }
        else if(receivedJson.has("repoProfile")){
            String repoProfile = receivedJson.get("repoProfile").asText();
            String searchQuery = receivedJson.get("username").asText();
            if(repoProfileActor == null){
                System.out.println(repoProfile);
                System.out.println("Repo actor created");
                repoProfileActor = getContext().actorOf(RepoProfileActor.props(self(),repoCommitStats));
            }
            repoProfileActor.tell(new RepoProfileActor.RepoProfileInfo(repoProfile, searchQuery, "", repoProfileService),getSelf());
        }
        else{
            System.out.println("Bassdas");
        }
    }
}
