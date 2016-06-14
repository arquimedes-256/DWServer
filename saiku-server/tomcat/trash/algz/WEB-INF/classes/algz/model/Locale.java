package algz.model;

import javax.persistence.Entity;

import core.base.BaseEntity;

@Entity
public class Locale extends BaseEntity {

	private static final long serialVersionUID = 7616853567461425623L;

	public static final Locale EN = new Locale(1,"English","en");
	public static final Locale PT = new Locale(2,"Portugues","pt");
	
	private String name;
	private String sign;
	
	public Locale()
	{
		super();
	}
	public Locale(int id,String name, String sign) 
	{
		this.setId(id);
		this.name = name;
		this.sign = sign;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
