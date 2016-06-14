package algz.model;

import javax.persistence.Entity;

import core.base.BaseEntity;

@Entity
public class Link extends BaseEntity {

	private static final long serialVersionUID = 7934057606611092382L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
