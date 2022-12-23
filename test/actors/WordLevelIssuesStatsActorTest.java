//package actors;
//
//import akka.actor.ActorRef;
//import akka.actor.ActorSystem;
//import akka.testkit.javadsl.TestKit;
//import com.fasterxml.jackson.databind.JsonNode;
//import models.*;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import play.cache.AsyncCacheApi;
//import services.*;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.CompletionStage;
//import java.util.concurrent.ExecutionException;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//
///**
// * Test class for WordLevelIssuesStatsActor
// * @author Rajat Kumar
// */

//public class WordLevelIssuesStatsActorTest {
//
//    static ActorSystem actorSystem;
//    system = ActorSystem.create();
//    
//    ArrayList<String> mockList = new ArrayList<String>();
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
//
//    @Test
//   public void testIssues() {
//    
//    final TestKit testProbe = new TestKit(system);
//
//       WordLevelIssueStats ws = new WordLevelIssueStats();
//       ActorRef WordLevelIssuesStatsActor;
//       IssueStatsInfo si = new IssueStatsInfo(mockList);
//       
//
//       Props props = Props.create(WordLevelIssuesStatsActor.class);
//        TestActorRef<WordLevelIssuesStatsActor> ref = TestActorRef.create(system, props);
//        ref.tell(si.issueList, testProbe.getRef());
//
//    
//   WordLevelIssueStats S = new WordLevelIssueStats(); 
//     IssuesWordLevelStatistics res =S.computeIssueWordLevelStats(mockList);
//     
//    
//    assertEquals(res.frequencyOfWords,mockHashMap); 
//
//
//  
//    }
//   
//    
//}
