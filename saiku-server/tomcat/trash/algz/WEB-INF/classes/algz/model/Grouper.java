package algz.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import core.base.BaseEntity;

@Entity
public class Grouper extends BaseEntity {

	private static final long serialVersionUID = 6035106961330303117L;

	private String name;
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<User> users;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

	
}
