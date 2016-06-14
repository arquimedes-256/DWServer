package algz.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import root.etc.AlgzFile;

import core.base.BaseEntity;

@Entity
public class Module extends BaseEntity implements AlgzFile
{
	private static final long serialVersionUID = 213853529463773985L;

	public List<Class> getClasses() {
		return classes;
	}
	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}
	private String name;
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Class> classes;
	
	@OneToOne
	private FileStatus fileStatus;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFileStatus(FileStatus fileStatus)
	{
		this.fileStatus = fileStatus;
	}
	public FileStatus getFileStatus()
	{
		return fileStatus;
	}
	@Transient
	@Override
	public List<? extends AlgzFile> getChildrens() {
		return getClasses();
	}
	
}
