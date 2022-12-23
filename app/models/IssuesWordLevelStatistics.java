package models;

/**
 * Model class for Repository Issues Title word level Statistics
 * @author Rajat Kumar
 */

import java.util.Map;


public class IssuesWordLevelStatistics {
	
	
	public Map<String, Integer> frequencyOfWords;
	
	/**
	 * Construtor to intialize IssuesWordLevelStatistics Object
	 * @param frequencyOfWords
	 * @author Rajat Kumar
	 */
	
	public IssuesWordLevelStatistics(Map<String, Integer> frequencyOfWords) 
	{
		
		this.frequencyOfWords = frequencyOfWords;
	}
	
	

}
