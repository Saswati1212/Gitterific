package models;

import java.util.List;

public class RepoResults {

	String user;
	int addt;
	int del;

	public RepoResults(String user, int addt,int del){
		this.user = user;
		this.addt = addt;
		this.del = del;
	}
		
	public void setUser(String user) {
		this.user = user;
	}


	public void setAddt(int addt) {
		this.addt = addt;
	}


	public void setDel(int del) {
		this.del = del;
	}


	public String getUser() {
		return user;
	}

	public int getAddt() {
		return addt;
	}
	
	public int getDel() {
		return del;
	}

}
