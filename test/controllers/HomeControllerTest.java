//package controllers;
//
//import models.Repository;
//import models.TopicResult;
//import models.User;
//import org.junit.Test;
//
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import play.Application;
//import play.inject.guice.GuiceApplicationBuilder;
//import play.mvc.Http;
//import play.mvc.Result;
//
//
//import play.test.WithApplication;
//import services.TopicService;
//import services.UserProfile;
//import models.IssuesWordLevelStatistics;
//import services.WordLevelIssueStats;
//
//
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import play.libs.ws.WSBodyReadables;
//import play.libs.ws.WSBodyWritables;
//import play.libs.ws.WSClient;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//import static play.mvc.Http.Status.OK;
//import static play.test.Helpers.*;
//import static org.mockito.ArgumentMatchers.*;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import models.DataFormatter;
//
//
//@RunWith(MockitoJUnitRunner.class)
//public class HomeControllerTest extends WithApplication {
//
//    @Mock
//    UserProfile userProfileTimeline ;
//
//    @Mock
//    TopicService topicService;
//
//    @Mock
//    WSClient ws;
//
//    @InjectMocks
//    HomeController homeController;
//
//    @Override
//    protected Application provideApplication() {
//        return new GuiceApplicationBuilder().build();
//    }
//
//    @Test
//    public void testIndex() {
//        Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/");
//
//        Result result = route(app, request);
//        assertEquals(OK, result.status());
//    }
//
//    @Test
//    public void testUser() {
//
//
//        Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/repo/trekhleb");
//
//        Http.RequestBuilder request2 = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/repo/trekhleb/javascript-algorithms/");
//
//        Result result = route(app, request2);
//        assertEquals(404, result.status());
//    }
//
//
//     /**
//     * search API test
//     * @author Urvish Tanti
//     */
//
//    @Test
//    public void indexSearch() {
//        Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/?search=java");
//
//        Result result = route(app, request);
//        assertEquals(200, result.status());
//    }
//
//    /**
//     * Test the WordLevelIssueStats Service which  returns computed Word Level Issues Title Statistics
//     * @author Rajat Kumar
//     */
//
//     @Test
//    public void testWordLevelIssueStats(){
//
//    	ArrayList<String> mockList = new ArrayList<String>();
//
//		mockList.add("Issue is new dependency");
//		mockList.add("Issue is new dependency");
//
//		HashMap<String,Integer> mockHashMap = new HashMap<String,Integer>();
//
//				mockHashMap.put("issue", 2);
//				mockHashMap.put("is", 2);
//				mockHashMap.put("new", 2);
//				mockHashMap.put("dependency", 2);
//    	WordLevelIssueStats obj = new WordLevelIssueStats();
//    	IssuesWordLevelStatistics res = obj.computeIssueWordLevelStats(mockList);
//
//    	assertEquals(mockHashMap,res.frequencyOfWords);
//    }
//
//    @Test
//  (expected = NullPointerException.class)
//  public void testStats() {
//
//  	Http.RequestBuilder request4 = new Http.RequestBuilder()
//            .method(GET)
//            .uri("/wordStats/");
//
//      Result result = route(app, request4);
//      assertEquals(OK, result.status());
//  }
//
//    /**
//     * Test the User Profile page (with all their repositories)
//     * @author Saswati Chowdhury
//     */
//    @Test
//    public void testUserTimeline() {
//        running(provideApplication(), () -> {
//            try {
//                when(userProfileTimeline.getUserProfile(anyString())).thenReturn(mockuser());
//                Result result = homeController.userRepoList("anything").toCompletableFuture().get();
//
//                assertEquals("text/html", result.contentType().get());
//                assertTrue(contentAsString(result).contains("repo1"));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
//    }
//
//    /**
//     * Test the topic result
//     * @author Urvish Tanti
//     */
//
//    @Test
//    public void testTopicResults(){
//        running(provideApplication(), () -> {
//            try {
//                when(topicService.getRepoByTopics(anyString())).thenReturn(mockTopicResults());
//                Result result = homeController.getTopics("java");
//
//                assertEquals("text/html", result.contentType().get());
//                assertTrue(contentAsString(result).contains("topic1"));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
//    }
//
//    private List<TopicResult> mockTopicResults(){
//        List<String> topics = Arrays.asList("topic1","topic2","topic3");
//        TopicResult topicList = new TopicResult();
//        topicList.setRepo("repoName");
//        topicList.setRepoId("id");
//        topicList.setUser("userName");
//        topicList.setTopics(topics);
//        List<TopicResult> topicsResults= Arrays.asList(topicList);
//       return topicsResults;
//    }
//
//    private User mockuser(){
//
//        //System.out.println("Mocking user");
//        List<Repository> repositoryList= new ArrayList<>();
////        Repository repo = new Repository("repo1", "desc1","id1");
////        repositoryList.add(repo);
//        Repository repo = new Repository();
//        repo.setName("repo1");
//        repo.setDescription("desc1");
//        repo.setRepoId("id1");
//        repositoryList.add(repo);
//
//        User user = new User();
//        user.setName("user1");
//        user.setLogin("mocklogin");
//        user.setRepositories(repositoryList);
//        return user;
//    }
//
//
//
//
//
//	    /**
//     * Test the repo commits result
//     * @author Hasandeep Singh
//     */
//
//    @Test
//    (expected = NullPointerException.class)
//    public void testRepoCommits() {
//    	// sample user repo
//    	Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/repo/TheAlgorithms/Java/63477660");
//
//        Result result = route(app, request);
//        assertEquals(200, result.status());
//    }
//
//
//}
