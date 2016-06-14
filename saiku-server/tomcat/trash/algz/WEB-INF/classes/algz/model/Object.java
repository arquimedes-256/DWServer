package algz.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import root.etc.AlgzFile;
import core.base.BaseEntity;


@Entity
public class Object  
	extends BaseEntity 
	implements 
		Comparable<Object>,
		AlgzFile
{

	private static final long serialVersionUID = -8519546602725880046L;
	
	@Transient
	private String name;
	@OneToOne
	private Class clazz;
	@OneToMany
	@Cascade(value=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Value> valores;
	private int naturalID;
	@OneToOne
	private FileStatus fileStatus;
	
	public FileStatus getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(FileStatus fileStatus) {
		this.fileStatus = fileStatus;
	}

	public List<Value> getValores() {
		return valores;
	}

	public void setValores(List<Value> valores) {
		this.valores = valores;
	}
	public int getNaturalID() {
		return naturalID;
	}
	public void setNaturalID(int naturalID) {
		this.naturalID = naturalID;
	}
	public Class getClazz() 
	{
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	@Transient
	public Value getVal(String attributeName)
	{
		for(Value v:this.getValores())
		{
			if(v.getAttribute().getName().equals(attributeName))
			{
				return v;
			}
		}
		return null;
	}
	@Transient
	public Value getEndTemporal()
	{
		for (Value v : getValores()) 
		{
			if(v.getAttribute().isEndTemporal())
			{
				return v;
			}
		}
		return null;
	}
	@Transient
	public Value getStartTemporal()
	{
		for (Value v : getValores())
		{
			if(v.getAttribute().isStartTemporal())
			{
				return v;
			}
		}
		return null;
	}
	@Transient 
	public Attribute getAttr(String attributeName)
	{
		Value v = getVal(attributeName);
		if(v != null)
			v.getAttribute();
		return null;
	}

	@Override
	public int compareTo(Object object) 
	{
		return this.getId() < object.getId() ? -1 	:
		(this.getId() > object.getId()) ? 1 	: 0;
	}

	@Override
	public String getName() 
	{
		String d = getFullName();
		if(getQTD()!=null)
		{
			for(Value v:valores)
			{
				for(Object object:v.getRefferences())
				{
					Attribute labelAttr =  object.getClazz().getAttr("*label");
					if(labelAttr != null)
					{
						String labelName = labelAttr.getName();
						
						if(labelName != null)
						{
							return object.getVal(labelName).getStringData();
						}
					}
				}
			}
		}
		return d;
		
	}
	@Transient
	public String getFullName()
	{
		Attribute labelAttribute = this.clazz.getAttr("*label");
		String defaultName = clazz.getName()+"@"+getId();
		if(labelAttribute != null)
		{
			String labelName = labelAttribute.getName();
			Value v = getVal(labelName);
			return labelName != null && v != null ? 
					v.getStringData() : 
						defaultName;	
		}
		return defaultName;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Collection<? extends AlgzFile> getChildrens() {
		return getValores();
	}
	@Transient
	public String getQTD()
	{
		String svlr = null;
		
		for(Value v:valores)
		{
			if(v.getAttribute().isQTD())
			{
				svlr = String.valueOf(v.getNumberData());
				break;
			}
		}
		return svlr;
	}

}
