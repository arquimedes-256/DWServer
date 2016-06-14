package algz.model.ui;

import javax.persistence.Entity;

import core.base.BaseEntity;

@Entity
public class UIConfig extends BaseEntity 
{
	private static final long serialVersionUID = -4481239296536188211L;
	private int columns =  6; // number of columns in the grid
	private int[] margins =  new int[]{10, 10}; // the margins in between grid items
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int[] getMargins() {
		return margins;
	}
	public void setMargins(int[] margins) {
		this.margins = margins;
	}
	
}
