package algz.model.ui;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import core.base.BaseEntity;

@Entity
public class Element extends BaseEntity 
{
	private static final long serialVersionUID = -8535004512976226551L;
	@OneToOne
	private Component component;
	private int row,col,sizeY,sizeX;
	@Column(length=16000)
	private String jsonData;
	private String position;
	
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	public String getPosition() {
		return position;
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
	public int getSizeY() {
		return sizeY;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
}
