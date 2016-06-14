package algz.model;

import javax.persistence.Entity;

import core.base.BaseEntity;

@Entity
public class FileStatus extends BaseEntity
{
	public static final FileStatus IN_TRASH = new FileStatus(1,"IN_TRASH");
	public static final FileStatus LOCKED = new FileStatus(2,"LOCKED");
	public static final FileStatus UNLOCKED = new FileStatus(3,"UNLOCKED");
	
	private static final long serialVersionUID = 2103291105446433666L;
	private String name;
	
	public FileStatus(Integer id,String name)
	{
		this.setId(id);
		this.name = name;
	}
	public FileStatus(){};
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	@Override
	public boolean equals(java.lang.Object a) 
	{
		if(a instanceof FileStatus)
		{
			return ((FileStatus)a).getId() == this.getId();
		}
		return false;
	}
}
