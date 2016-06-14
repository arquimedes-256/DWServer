package root.bin;

import java.util.ArrayList;
import java.util.HashMap;

import root.etc.Binary;
import algz.model.Class;
import algz.model.Object;
import algz.model.Value;

public class to_hashmap extends Binary {

	
	@Override
	protected void init() 
	{
		
		if(getPipeline().getInput() instanceof Object)
		{
			HashMap<String, java.lang.Object> h = new HashMap<String, java.lang.Object>();
			Object o = (Object) getPipeline().getInput();
			
			h = buildHash(o);
			getPipeline().setOutput(h);
		}
		else if(getPipeline().getInput() instanceof ArrayList<?>)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Object> inList = (ArrayList<Object>) getPipeline().getInput();
			ArrayList<HashMap<String, java.lang.Object>> outList = new ArrayList<HashMap<String, java.lang.Object>>();
			
			for(Object o : inList)
			{
				outList.add(buildHash(o));
			}

			getPipeline().setOutput(outList);
		}
		
	}

	private HashMap<String , java.lang.Object> buildHash(Object o) 
	{

		HashMap<String, java.lang.Object> hashMap = new HashMap<String, java.lang.Object>();
		
		for(Value v : o.getValores())
		{
			String key = v.getAttribute().getName();
			java.lang.Object val = getVal(v);
			hashMap.put(key, val);
			
			if(v.getAttribute().getTyper().getId() == Class.Location.getId())
			{
				hashMap.put(key+"/History", v.getLocationHistory());
			}
		}
		
		return hashMap;
		
	}

	private java.lang.Object getVal(Value v) 
	{
		Object x = null;
		Long typerId =  v.getAttribute().getTyper().getId();
		
		if(typerId.equals(Class.Decimal.getId()))
		{
			return v.getNumberData();
		}
		else if(typerId.equals(Class.String.getId()))
		{
			return v.getStringData();
		}
		else if(typerId.equals(Class.Location.getId()))
		{
			return v.getLocation();
		}
		else if(typerId.equals(Class.Date.getId()))
		{
			if(v.getDateTimeData() != null)
				return v.getDateTimeData().getTimeInMillis();
			return 0;
		}
		else if(!v.getAttribute().getTyper().isPrimitive())
		{
			ArrayList<HashMap<String, java.lang.Object>> refferences = new ArrayList<HashMap<String, java.lang.Object>>();
			
			for(Object o : v.getRefferences())
			{
				refferences.add(buildHash(o));
			}
			return refferences;
		}
		
		return x;
	}

	@Override
	public boolean needLogin() {
		return false;
	}

}
