package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import models.IssuesWordLevelStatistics;

/**
 * This service class contains method to compute word level statistics.
 * @author Rajat Kumar
 */

public class WordLevelIssueStats {
	
	
	/**
     * The method returns computed Word Level Issues Title Statistics   
     * @author Rajat Kumar
     * @param issuesList    list of all issues title list 
     * @return    IssuesWordLevelStatistics  
     * 
     */
	
public IssuesWordLevelStatistics computeIssueWordLevelStats(List<String> issuesList) {
		
		
			
			//Splitting words in issues title list
			
			List<String> tList = Stream.of(issuesList)
		            .flatMap(Collection::stream)
		            .flatMap(str -> Arrays.stream(str.split("\\s+")))
		            .collect(Collectors.toList());
			
			//Mapping words in issues title with their frequency 
			Map<String, Integer> wordsCountMap = tList.stream().map(eachWord -> eachWord)
					.collect(Collectors.toMap(w -> w.toLowerCase(), w -> 1, Integer::sum));
			
			//Sorting the result in descending order (by frequency of the words) 
			wordsCountMap = wordsCountMap.entrySet()
					.stream()
					.sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)-> e1, LinkedHashMap::new));
			
			wordsCountMap.entrySet().forEach(entry -> {
			    //System.out.println(entry.getKey() + " " + entry.getValue());
			});
			
			//Storing wordsCountMap in object of model class IssuesWordlevelStatistics and returning as result
			
			IssuesWordLevelStatistics resWordCountMap = new IssuesWordLevelStatistics(wordsCountMap);
			return resWordCountMap;

	}
}
