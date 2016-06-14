package algz.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import root.etc.AlgzFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import core.base.BaseEntity;

@Entity
public class TTY extends BaseEntity implements AlgzFile
{
	private static final long serialVersionUID = 4055028960423046384L;

	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	private Object connectedObject;

	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	private Class connectedClass;

	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	private Module connectedModule;

	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	private Application connectedApplication;
	
	
	
	@Transient
	private Collection<? extends AlgzFile> applications;
	
	
	public Object getConnectedObject() {
		return connectedObject;
	}
	public void setConnectedObject(Object connectedObject) {
		this.connectedObject = connectedObject;
	}
	public Class getConnectedClass() {
		return connectedClass;
	}

	public void setConnectedClass(Class connectedClass) {
		this.connectedClass = connectedClass;
	}
	public Module getConnectedModule() {
		return connectedModule;
	}
	public void setConnectedModule(Module connectedModule) {
		this.connectedModule = connectedModule;
	}
	public Application getConnectedApplication() {
		return connectedApplication;
	}
	public void setConnectedApplication(Application connectedApplication) {
		this.connectedApplication = connectedApplication;
	}
	@JsonIgnore
	@Transient
	public AlgzFile getCurrentFile() {
		if(getConnectedObject() != null)
			return getConnectedObject();
		else if(getConnectedClass() != null)
			return getConnectedClass();
		else if(getConnectedModule() != null)
			return getConnectedModule();
		else if(getConnectedApplication() != null)
			return getConnectedApplication();
		return this;
	}
	@Override
	public String getName() {
		
		return "tty@"+getId();
	}
	@Override
	public void setName(String name) {
		return;
		
	}
	public void setApplicationsList(List<Application> applications)
	{
		this.applications = applications;
	}
	@Override
	public Collection<? extends AlgzFile> getChildrens() {
		return this.applications;
	}
	@Override
	@Transient
	@JsonIgnore
	public FileStatus getFileStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}
