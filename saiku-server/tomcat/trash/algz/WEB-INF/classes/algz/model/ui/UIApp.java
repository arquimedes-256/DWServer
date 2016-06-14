package algz.model.ui;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import algz.model.Customer;
import core.base.BaseEntity;

@Entity
public class UIApp extends BaseEntity
{
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public List<Box> getBoxes() 
	{
		return boxes;
	}
	public void setBoxes(List<Box> boxes) 
	{
		this.boxes = boxes;
	}
	private static final long serialVersionUID = -7934339998663442181L;
	@Index(name="IDX_UIAPP_NAME")
	private String name;
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(CascadeType.SAVE_UPDATE)
	private List<Box> boxes;
	
	public List<Box> getBindedBoxes() {
		return bindedBoxes;
	}
	public void setBindedBoxes(List<Box> bindedBoxes) {
		this.bindedBoxes = bindedBoxes;
	}
	@Transient
	private List<Box> bindedBoxes;
	@OneToOne
	private Customer customer;
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	private UIConfig config;

	public UIConfig getConfig() {
		return config == null ? config = new UIConfig(): config;
	}
	public void setConfig(UIConfig config) {
		this.config = config;
	}
}
