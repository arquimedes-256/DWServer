package root.etc;

import java.util.Collection;

import algz.model.FileStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface AlgzFile 
{
	public String getName();
	public void setName(String name);
	public FileStatus getFileStatus();
	@JsonIgnore
	public Collection<? extends AlgzFile> getChildrens();
}
