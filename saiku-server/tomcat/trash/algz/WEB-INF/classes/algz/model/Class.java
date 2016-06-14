package algz.model;

import java.util.Collection;
import java.util.SortedSet;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import root.etc.AlgzFile;
import algz.model.Attribute.AttributeComparator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import core.base.BaseEntity;

@Entity
public class Class extends BaseEntity implements AlgzFile
{

	private static final long serialVersionUID = 1411836575033117947L;
	private static final Pattern NAME_PATTERN = Pattern.compile("nome|name|label|numero|number");
	
	public static Class Description = new Class(1,"Description"),
						Location = new Class(2,"Location"),
						Email = new Class(3,"Email"),
						Currency = new Class(4,"Currency"),
						Date = new Class(5,"Date"),
						File = new Class(6,"File"),
						Boolean = new Class(7,"Boolean"),
						Decimal = new Class(8,"Decimal"),
						Integer = new Class(9,"Integer"),
						String = new Class(10,"String"),
						Function = new Class(11,"Function"),
						User = new Class(12,"User");
	
	private boolean isPrimitive;
	
	public Class(long id,java.lang.String name) 
	{
		this.setId(id);
		this.setName(name);
	}
	public Class() {}
	public boolean isPrimitive() {
		return isPrimitive;
	}
	public void setPrimitive(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}
	private String name;
	
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(CascadeType.SAVE_UPDATE)
	@Sort(type=SortType.COMPARATOR,comparator=AttributeComparator.class)
	private SortedSet<Attribute> attributes;
	@OneToOne
	private FileStatus fileStatus;
	
	@JsonIgnore
	@Transient
	private Collection<? extends AlgzFile> childrens;
	

	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public SortedSet<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(SortedSet<Attribute> attributes2) {
		this.attributes = attributes2;
	}
	public void setFileStatus(FileStatus fileStatus)
	{
		this.fileStatus = fileStatus;
	}
	public FileStatus getFileStatus()
	{
		return fileStatus;
	}
	public Attribute getAttr(String attrName) 
	{
		if(attributes != null)
			for(Attribute a : attributes)
			{
				if(attrName.startsWith("*"))
				{
						if(attrName.equals("*label"))
						{
							if(NAME_PATTERN.matcher(a.getName().toLowerCase()).find())
							{
								return a;
							}
						}
				}
				else
				{
					if(a.getName().equals(attrName))
					{
						return a;
					}
				}
			}
		return null;
	}
	@Override
	public String toString() {
		return getId()+"@"+getName();
	}
	@Override
	public Collection<? extends AlgzFile> getChildrens() 
	{
		return childrens;
	}

	public void setChildrens(Collection<? extends AlgzFile> findObjects)
	{
		this.childrens = findObjects;
	}
	@Transient
	public boolean isSimplePrimitive() 
	{
		return isPrimitive() && getAttributes().size() == 0;
	}
	
	@Transient
	public boolean isInventory()
	{
		boolean hasItem = false;
		boolean hasQtd = false;
		
		if(attributes != null) 
			for(Attribute attribute : attributes)
			{
				if(attribute.getName() != null)
					if(attribute.isQTD())
					{
						hasQtd = true;
					}
					else if(attribute.getTyper().getAttr("*label") != null)
					{
						hasItem = true;
					}
			}
			return hasItem && hasQtd;
	}
	
}
