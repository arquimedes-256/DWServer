package root.etc.report;

import java.util.ArrayList;
import java.util.HashMap;

import algz.model.Attribute;

class ResultFind
{
	private HashMap<String, Attribute> pathAttribute = new HashMap<String, Attribute>();
	private ArrayList<Attribute> parents = new ArrayList<Attribute>();
	
	private Attribute attribute;
	
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public void push(Attribute parent)
	{
		StringBuilder path = new StringBuilder();
		
		parents.add(parent);
		
		for(int i=0;i<parents.size();i++)
		{
			path.append("/"+parents.get(i).getName());
		}
		pathAttribute.put(path.toString(), parent);	
	}
	public Attribute getLastParent()
	{
		return parents.size() > 0 ? parents.get(parents.size() -1): null;
	}
	public ArrayList<Attribute> getParents() {
		return this.parents;
	}
}
