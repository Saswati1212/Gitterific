package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataFormatter{
	
	ObjectMapper mapper = new ObjectMapper();
    public static HashMap<String, JsonNode> searchMasterData = new HashMap<String, JsonNode>();
	Queue<String> entireSearchResults = new LinkedList<>();
	//HashMap<String, ArrayList<ApiResults>> resultsToDisplay = new HashMap<String, ArrayList<ApiResults>>();
	LinkedHashMap<String, ArrayList<ApiResults>> resultsToDisplay = new LinkedHashMap<String, ArrayList<ApiResults>>();
	
	


	/**
	 * Returns the latest 10 search containing the provided search keyword
	 * deletes result if it is more than 10, else it keeps adding in the main search page
	 * @param query
	 * @param singleEntry
	 * @author AKG06
	 */
    
	public void addEntrytoSearchList(String query, ArrayList<ApiResults> singleEntry) {
		
		if(!entireSearchResults.contains(query)) {
			if(entireSearchResults.size()==10) {
				String removedElement = entireSearchResults.remove();
				resultsToDisplay.remove(removedElement);
					
			}
			entireSearchResults.add(query);
			resultsToDisplay.put(query,singleEntry);
			
	}
}

/**
 * This function formats the data into required format and sequence the output in correct order.
 * @param query
 * @param nodeObject
 * @author AKG06
 * @throws InterruptedException
 * @throws ExecutionException
 * @return returns a linked hashmap contain list of repositories
 */
	
	
	public LinkedHashMap<String, ArrayList<ApiResults>> retrieveArrayOfData(String query, JsonNode nodeObject) throws InterruptedException, ExecutionException {
		
		List<ApiResults> entries = new ArrayList<ApiResults>();
		
		for(JsonNode data:nodeObject) {
			ApiResults result = new ApiResults(data.get("name").toString(),data.get("owner").findPath("login").toPrettyString(),data.get("topics"),query, data.get("id").toString());
			searchMasterData.put(query, nodeObject);		
			entries.add(result);	
		}
		
		addEntrytoSearchList(query, (ArrayList<ApiResults>) entries);
		List<String> reverseOrderedKeys = new ArrayList<String>(resultsToDisplay.keySet());
		Collections.reverse(reverseOrderedKeys);

	   LinkedHashMap<String, ArrayList<ApiResults>> lhm = new LinkedHashMap<String, ArrayList<ApiResults>>();
	   for(String key : reverseOrderedKeys)
	   {
		   lhm.put(key,resultsToDisplay.get(key));
	   }

	   return lhm;
	}

	
	/**
	 * Fetches JSON data for each api request, and processes it to add particular requirements into a list
	 * @author Hasandeep Singh
	 * @param user
	 * @param repos
	 * @param nodeObject
	 * @return list of each commit statistics
	 */
	public List<RepoResults> retrieveStats(String user,String repos,JsonNode nodeObject) 
	{		
		List<RepoResults> stats = new ArrayList<RepoResults>(); //stores each commit's committer, number of additions and deletions
		try 
		{
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			FileListModel fileListModel = mapper.readValue(nodeObject.toString(),FileListModel.class);		
			List<FileStat> files = fileListModel.getFiles();
			Committer committer = fileListModel.getCommitter();
		
			for(FileStat fileStat: files) 
			{
				stats.add(new RepoResults(committer.getLogin(),fileStat.getAdditions(),fileStat.getDeletions()));
			}
			
		} 
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
		}
		return stats;
	}


}
	
	

