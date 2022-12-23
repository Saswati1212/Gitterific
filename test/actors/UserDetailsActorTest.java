package actors;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.JsonNode;
import models.Repository;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import play.cache.AsyncCacheApi;
import services.UserProfile;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


/**
 * Test class for UserDetailsActor
 * @author Saswati Chowdhury
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserDetailsActorTest {

    static ActorSystem actorSystem;
    private static TestKit testProbe;
    private static UserProfile githubServiceMock;
    private AsyncCacheApi asyncCacheApi;


    @Before
    public  void setup() {
        actorSystem = ActorSystem.create();
        testProbe = new TestKit(actorSystem);
        githubServiceMock = Mockito.mock(UserProfile.class);
        asyncCacheApi = Mockito.mock(AsyncCacheApi.class);
    }

    /**
     * Mock UserDetails object
     * @return UserDetails
     */
    public CompletionStage<Object> userDetailsCompletionStage()
    {
        return CompletableFuture.supplyAsync(() -> {

            User userDetails = new User();
            userDetails.setLogin("MockUserName");
            userDetails.setRepositories(repositories());
            return userDetails;

        });
    }


    /**
     * mock Repository
     * @return repository list
     */
    private List<Repository> repositories()
    {
        Repository repository = new Repository();
        repository.setName("title");
        return Arrays.asList(repository);
    }

    /**
     * Test case for UserDetailsActor
     */
    @Test
    public void actorTest() throws ExecutionException, InterruptedException {

        new TestKit(actorSystem) {
            {
                Mockito.when(asyncCacheApi.getOrElseUpdate(anyString(),any())).thenReturn(userDetailsCompletionStage());
                final ActorRef userActor = actorSystem.actorOf(
                        UserDetailsActor.props(testProbe.getRef(), githubServiceMock,asyncCacheApi,"userName"));
                userActor.tell(new Messages.GetUserDetails("userName"), testProbe.getRef());
                Messages.UserDetails userDetailsResponse = testProbe.expectMsgClass(Messages.UserDetails.class);
                JsonNode userDetails = userDetailsResponse.userDetails.get("searchProfile");
                assertEquals("MockUserName",userDetails.get("login").asText());
            }

        };
    }







}




