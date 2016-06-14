package algz.model.ui;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import algz.model.Object;

import com.fasterxml.jackson.annotation.JsonIgnore;

import core.base.BaseEntity;

@Entity
public class Box extends BaseEntity 
{

	private static final long serialVersionUID = -912082603440759721L;
	
	@JsonIgnore
	public Box getClone() 
	{
		Box box = new Box();
		box.setCol(getCol());
		box.setRow(getRow());
		box.setSizeX(getSizeX());
		box.setSizeY(getSizeY());
		box.setClazzId(getClazzId());
		box.setElements(getElements());
		box.setBackpackId(getBackpackId());
		return box;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public long getClazzId() {
		return clazzId;
	}
	public void setClazzId(long clazzId) {
		this.clazzId = clazzId;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	private int row;
	private int col;
	private int sizeX;
	private int sizeY;
	
	public long getBackpackId() {
		return backpackId;
	}
	public void setBackpackId(long backpackId) {
		this.backpackId = backpackId;
	}
	private long backpackId = 0;
	private long clazzId = 0;

	@OneToMany
	@Cascade(CascadeType.SAVE_UPDATE)
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<Element> elements;
	@Transient
	private Object object;
	
	public List<Element> getElements() {
		return elements;
	}
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	 
}
