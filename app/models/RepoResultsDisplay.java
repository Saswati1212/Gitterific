package models;

public class RepoResultsDisplay {

	public String user;
	public int min;
	public int max;
	public int delMin;
	public int delMax;
	public int avg;
	public int delAvg;
	public int commit_count;

	
	public RepoResultsDisplay(String user, int min,int max, int avg,int delMin,int delMax,int delAvg, int commit_count)
	{
		this.user = user;
		this.min = min;
		this.max = max;
		this.delMin = delMin;
		this.delMax = delMax;
		this.avg = avg;
		this.delAvg = delAvg;
		this.commit_count = commit_count;

	}
		
	public int getCommit_count() {
		return commit_count;
	}

	public void setCommit_count(int commit_count) {
		this.commit_count = commit_count;
	}

	public String getUser() {
		return user;
	}
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public int getDelMin() {
		return delMin;
	}
	public int getDelMax() {
		return delMax;
	}
	public int getAvg() {
		return avg;
	}
	public int getDelAvg() {
		return delAvg;
	}

	public String getKey() {
		return user+Integer.toString(min)+Integer.toString(max)+Integer.toString(avg)+Integer.toString(delMin)+Integer.toString(delMax)+Integer.toString(delAvg);
	}
}
