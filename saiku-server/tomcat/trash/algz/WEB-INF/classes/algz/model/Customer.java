package algz.model;

import javax.persistence.Entity;

import core.base.BaseEntity;

@Entity
public class Customer extends BaseEntity {
	
	private static final long serialVersionUID = 4482797120626635594L;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
