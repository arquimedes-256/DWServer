package algz.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import root.etc.AlgzFile;

import core.base.BaseEntity;

@Entity
public class Application extends BaseEntity implements AlgzFile
{	
	private static final long serialVersionUID = -4917851551935112717L;
	@OneToOne
	private Customer customer;
	private String name;
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Class> annotationList;
	

	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Module> modules;
	@OneToOne
	private FileStatus fileStatus;
	
	public List<Module> getModules() {
		return modules;
	}
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Class> getAnnotationList() {
		return annotationList;
	}
	public void setAnnotationList(List<Class> annotationList) {
		this.annotationList = annotationList;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public void setFileStatus(FileStatus fileStatus)
	{
		this.fileStatus = fileStatus;
	}
	public FileStatus getFileStatus()
	{
		return fileStatus;
	}
	@Override
	public Collection<? extends AlgzFile> getChildrens() {
		return getModules();
	}
}
