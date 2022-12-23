package actors;



import akka.actor.AbstractActor;
import models.*;
import play.libs.Json;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import akka.actor.ActorRef;
import akka.actor.Props;
import models.IssuesWordLevelStatistics;

import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import akka.actor.AbstractActor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.*;
import services.WordLevelIssueStats;

/**
 * Handles all Word Level Issues Statistics requests - about retrieving word level stats from issue title list
 * @author Rajat Kumar
 */


public class WordLevelIssuesStatsActor extends AbstractActor{
	
	static public class IssueStatsInfo{

		public final ArrayList<String> issueList;
        
        public IssueStatsInfo(ArrayList<String> issueList) {
        	
			this.issueList = issueList;
        }
    }
	
	/**
     * Creates an actor with properties specified using parameters
     * @return A <code>Props</code> object holding actor configuration
     * @author Rajat Kumar
     */
	public static Props getProps() {
		return Props.create(WordLevelIssuesStatsActor.class);
	}
	
	/**
     * Handles incoming messages for this actor - matches the class of an incoming message and takes appropriate action
     * @return builder object after formation
     * @author Rajat Kumar
     */

	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return receiveBuilder()
				.match(IssueStatsInfo.class, this::sendIssueStats )
				.build();
	}

    /**
     * Retrieves and send all Word Level Issues Statistics requests - about retrieving word level stats from issue title list
     * @param issuStats request object consisting issues title list
     * @author Rajat Kumar
     */

	private void sendIssueStats(IssueStatsInfo issuStats) {
		HashMap<String,Object> send_data = new HashMap<String,Object>();
			
			WordLevelIssueStats wordLevelIssueStats = new WordLevelIssueStats();
			
			IssuesWordLevelStatistics stats = wordLevelIssueStats.computeIssueWordLevelStats(issuStats.issueList);

            
			send_data.put("list",stats);
			
			sender().tell(send_data, self());
	    		
	}

	
}
