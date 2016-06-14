package algz.model;

import javax.persistence.Entity;

import core.base.BaseEntity;

@Entity
public class MessageType extends BaseEntity {
	
	private static final long serialVersionUID = 8836988559765183122L;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MessageType(int id,String name) 
	{
		setId(id);
		this.name = name;
	}
	public MessageType()
	{
		super();
	}
	public static final MessageType ERROR = new MessageType(1,"Error");
	public static final MessageType WARN = new MessageType(2,"Warnning");
	public static final MessageType INFO = new MessageType(3,"Information");
}
