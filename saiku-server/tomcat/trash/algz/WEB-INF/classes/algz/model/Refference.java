package algz.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import core.base.BaseEntity;

@Entity
public class Refference extends BaseEntity
{

	private static final long serialVersionUID = -4999788948925286131L;

	@OneToMany
	private List<Object> objectList;
	@OneToOne
	private Attribute attribute;

	public List<Object> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Object> objectList) {
		this.objectList = objectList;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
}
