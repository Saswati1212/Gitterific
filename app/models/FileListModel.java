package models;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FileListModel {

	List<FileStat> files;
	Committer committer;
	public Committer getCommitter() {
		return committer;
	}

	public void setCommitter(Committer committer) {
		this.committer = committer;
	}

	public List<FileStat> getFiles() {
		return files;
	}

	public void setFiles(List<FileStat> files) {
		this.files = files;
	}
}
