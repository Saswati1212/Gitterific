package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.JsonNode;

import models.ApiResults;
import models.TopicResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import play.cache.AsyncCacheApi;
import services.TopicService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * SearchPageActor test class
 * @author Urvish Tanti
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class TopicsActorTest {

    static ActorSystem actorSystem;
    private static TestKit testProbe;
    private static TopicService githubServiceMock;
    private static AsyncCacheApi asyncCacheApi;

    @Before
    public  void setup() {
        actorSystem = ActorSystem.create();
        testProbe = new TestKit(actorSystem);
        githubServiceMock = Mockito.mock(TopicService.class);
        asyncCacheApi = Mockito.mock(AsyncCacheApi.class);
    }

    /**
     * TopicsActor test case
     */
    @Test
    public void TopicActorTest() {

        new TestKit(actorSystem) {
            {
                Mockito.when(asyncCacheApi.getOrElseUpdate(anyString(),any())).thenReturn(TopicStageSearch());
                final ActorRef topicActor = actorSystem.actorOf(
                        TopicsActor.props(testProbe.getRef(), githubServiceMock, asyncCacheApi));
                topicActor.tell(new Messages.GetRepositoryfromTopic("JAVA"), testProbe.getRef());
                Messages.TopicDetails actual = testProbe.expectMsgClass(Messages.TopicDetails.class);
                Messages.TopicDetails topicResponse = actual;
                assertEquals("topicsDetails",topicResponse.topicDetails.get("responseType").asText());
            }
        };
    }

    /**
     * Mock topicResult object
     * @return Object of topicResult
     */
    public CompletionStage<Object> TopicStageSearch()
    {
        return CompletableFuture.supplyAsync(() -> {
            List<String> topics = Arrays.asList("topic1","topic2","topic3");
            TopicResult topicList = new TopicResult();
            topicList.setRepo("repoName");
            topicList.setRepoId("id");
            topicList.setUser("userName");
            topicList.setTopics(topics);
            List<TopicResult> topicsResults= Arrays.asList(topicList);
            return topicsResults;
        });
    }
}