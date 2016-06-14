package algz.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import core.base.BaseEntity;

@Entity
public class Program extends BaseEntity {


	private static final long serialVersionUID = -3691859369374315066L;
	
	private String name;
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value={CascadeType.ALL})
	private List<Link> links;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}

}
