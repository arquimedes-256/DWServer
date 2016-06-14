package core.base;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseEntity implements Serializable
{
	private static final long serialVersionUID = 4189421008497934515L;
	@Id
	@GeneratedValue
    private long id;
    @Transient
    private String className = this.getClass().getSimpleName();
    
	public long getId()
	{
		return id;
	}
	public void setId(long id){
		this.id = id;	
	}
    public String getClassName()
    {
    	return className;
    }
    public void setClassName(String className)
    {
    	this.className = className;
    }
}
