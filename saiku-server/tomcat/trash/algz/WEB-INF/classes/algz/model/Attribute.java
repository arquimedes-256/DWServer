package algz.model;

import java.util.Collection;
import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import root.etc.AlgzFile;
import core.base.BaseEntity;

@Entity
public class Attribute 
	extends BaseEntity 
	implements 
		Comparable<Attribute>,
		AlgzFile
{
	private static final long serialVersionUID = -5361860400039624049L;
	
	public static class AttributeComparator implements Comparator<Attribute>
	{
		@Override
		public int compare(Attribute o1, Attribute o2) 
		{
			return 
				o1.getOrdering() < o2.getOrdering() ? -1 	:
				(o1.getOrdering() > o2.getOrdering()) ? 1 	: 1;
		}
	}
	private String functionBody;
	private String name;
	public Attribute(){};
	public Attribute(long id,String name, Class typer) 
	{
		this.setId(id);
		this.setName(name);
		this.setTyper(typer);
	}
	public String getFunctionBody() {
		return functionBody;
	}
	public void setFunctionBody(String functionBody) {
		this.functionBody = functionBody;
	}
	@OneToOne
	private Class typer;
	@OneToOne
	private FileStatus fileStatus;
	public int ordering;
	@Transient
	private boolean endTemporal;
	@Transient
	private boolean startTemporal;
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public Class getTyper() {
		return typer;
	}
	public void setTyper(Class typer) {
		this.typer = typer;
	}
	public FileStatus getFileStatus()
	{
		return fileStatus;
	}
	public void setFileStatus(FileStatus fileStatus)
	{
		this.fileStatus = fileStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int compareTo(Attribute o) 
	{
		return new AttributeComparator().compare(this, o);
	}
	@Override
	public String toString() {
		return this.getName();
	}
	public boolean isEndTemporal()
	{
		if(getTyper() != null)
		{
			String ln = getName().toLowerCase();
			return getTyper().getName().equals("Date") && (ln.contains("end") || ln.contains("fim") || ln.contains("fin"));	
		}
		else
			return false;
	}
	public boolean isStartTemporal()
	{
		if(getTyper() != null)
		{
			String ln = getName().toLowerCase();
			return getTyper().getName().equals("Date") && (ln.contains("start") || ln.contains("inicio") || ln.contains("inicial"));
		}
		else
			return false;
	}
	public boolean isNumber()
	{
		return 
				typer.getName().equals("Decimal") || 
				typer.getName().equals("Integer") ||
				typer.getName().equals("Currency");
	}
	@Override
	public Collection<? extends AlgzFile> getChildrens() {
		return null;
	}
	@Transient
	public boolean isQTD() {
		return name.toLowerCase().matches("(.*)(qtd|quant|qntd)(.*)") 
		&& isNumber();
	}
}
