package root.etc.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import algz.model.Attribute;

public class Pool
{
	private HashMap<Attribute, String> refAttributePool = new HashMap<Attribute, String>();
	private HashMap<Attribute, String> valAttributePool = new HashMap<Attribute, String>();
	private ArrayList<Attribute> checkList = new ArrayList<Attribute>();
	
	public String getVal(Attribute a)
	{
		if(a != null)
		{
			if(!valAttributePool.containsKey(a))
				valAttributePool.put(a, "v"+a.getId());
			return valAttributePool.get(a);	
		}
		return null;
	}
	public String getRef(Attribute a)
	{
		if(a != null)
		{
			if(!refAttributePool.containsKey(a))
				refAttributePool.put(a,"r"+a.getId());
			return refAttributePool.get(a);
		}
		return null;
	}
	public boolean containsVal(Attribute a)
	{
		return valAttributePool.containsKey(a);
	}
	public boolean containsRef(Attribute a)
	{
		return refAttributePool.containsKey(a);
	}
	public Collection<String> getValues()
	{
		return valAttributePool.values();
	}
	public boolean isChecked(Attribute a) {
		return checkList.contains(a);
	}
	public void check(Attribute a) {
		checkList.add(a);
		
	}
}
