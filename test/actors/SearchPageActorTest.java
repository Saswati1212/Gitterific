package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import play.cache.AsyncCacheApi;
import services.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


/**
 * Test class for SearchPageActor
 * @author AK06
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SearchPageActorTest {



    static ActorSystem actorSystem;
    private static TestKit testProbe;
    private static SearchService githubServiceMock;
    private static AsyncCacheApi asyncCacheApi;

    @Before
    public  void setup() {
        actorSystem = ActorSystem.create();
        testProbe = new TestKit(actorSystem);
        githubServiceMock = Mockito.mock(SearchService.class);
        asyncCacheApi = Mockito.mock(AsyncCacheApi.class);
    }

    /**
     * Test case for searchDetailsActor
     */
    @Test
    public void actorTest() {

        new TestKit(actorSystem) {
            {
                Mockito.when(asyncCacheApi.getOrElseUpdate(anyString(),any())).thenReturn(completionStageSearch());
                final ActorRef searchPageActor = actorSystem.actorOf(
                        SearchPageActor.props(testProbe.getRef(), githubServiceMock, asyncCacheApi));
                System.out.println("phrase");
                searchPageActor.tell(new Messages.SearchPageActor("phrase"), testProbe.getRef());
                Messages.SearchResult searchResultResponse = testProbe.expectMsgClass(Messages.SearchResult.class);
                System.out.println("output_searchResultResponse" + searchResultResponse);
                Messages.SearchResult search1 = searchResultResponse;

                assertEquals("searchResults",search1.searchResult.get("responseType").asText());
            }
        };
    }

    /**
     * Mock searchResult object
     * @return future of Object that gets return on calling search function in github service
     */
//    public CompletionStage<Object> completionStageSearch()
//    {
//        return CompletableFuture.supplyAsync(() -> {
//            List<String> topics = Arrays.asList("topic1","topic2","topic3");
//            TopicResult topicList = new TopicResult();
//            topicList.setRepo("repoName");
//            topicList.setRepoId("id");
//            topicList.setUser("userName");
//            topicList.setTopics(topics);
//            List<TopicResult> topicsResults= Arrays.asList(topicList);
//            return topicList;
//        });
//    }


    public CompletionStage<Object> completionStageSearch()
    {
        return CompletableFuture.supplyAsync(() -> {
            ApiResults search_Service = new ApiResults("userName","repoName");
            Map<String,List<ApiResults>> map = new LinkedHashMap<>();
            map.put("JAVA AI DL", Arrays.asList(search_Service));
            return map;
        });
    }






}
