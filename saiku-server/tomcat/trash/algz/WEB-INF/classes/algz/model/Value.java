package algz.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import root.etc.AlgzFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import core.base.BaseEntity;

@Entity
public class Value extends BaseEntity implements AlgzFile {

	public Value(){};
	public Value(String stringData, Attribute attribute) 
	{
		this.stringData = stringData;
		this.attribute = attribute;
	}
	
	public Calendar getDateTimeData() {
		return dateTimeData;
	}

	public void setDateTimeData(Calendar dateTimeData) {
		this.dateTimeData = dateTimeData;
	}

	private static final long serialVersionUID = -5444833111453781293L;
	
	@Index(name = "IDXstringData")
	private String stringData;
	@Index(name="IDXnumberData")
	private Double numberData;
	
	@Index(name="IDXdateTimeData")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateTimeData;

	@ManyToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	private Location location;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) 
	{
		this.location = location;
	}

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(CascadeType.SAVE_UPDATE)
	private List<Location> locationHistory;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdDate;
	
	public List<Location> getLocationHistory() {
		return locationHistory;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public void setLocationHistory(List<Location> locationHistory) {
		this.locationHistory = locationHistory;
	}

	@ManyToOne
	private User user;	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Object> refferences;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar bornDate;
	
	public Calendar getBornDate()
	{
		return bornDate;
	}

	public void setBornDate(Calendar bornDate)
	{
		this.bornDate = bornDate;
	}

	public String getStringData() {
		return stringData;
	}

	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	public Double getNumberData() {
		return numberData;
	}

	public void setNumberData(Double numberData) {
		this.numberData = numberData;
	}


	public List<Object> getRefferences() {
		return refferences;
	}

	public void setRefferences(List<Object> refferences) {
		this.refferences = refferences;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Cascade(value=CascadeType.SAVE_UPDATE)
	@OneToOne
	private Attribute attribute;

	public Attribute getAttribute() {
		return attribute;
	}

	@Override
	public String getName() {
		return this.getAttribute().getName();
	}

	@Override
	public void setName(String name) {
		return;
	}

	@Override
	public Collection<? extends AlgzFile> getChildrens() {
		return getRefferences();
	}
	@Override
	@Transient
	@JsonIgnore
	public FileStatus getFileStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
