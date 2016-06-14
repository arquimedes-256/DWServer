package algz.utils;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import root.etc.RegexPool;
import algz.model.Object;
import algz.model.Value;

public class StringUtils {


	private static final Pattern PATTERN = Pattern.compile(RegexPool.CONTAINS_NON_NUMERIC.toString());
	
	public static String scapeSQL(String val)
	{
		val = val.replace("'", "\\'");
		if(isNotNumber(val))
		{
			val = "%"+val+"%";
		}
		return val;		
	}
	public static boolean isNotNumber(String string)
	{
		return PATTERN.matcher(string).find();
	}
	public static String fix(String s) 
	{
		return s.replace("_", " ");
	}
	public static String getRealValue(Value value)
	{
		if(value.getStringData() != null)
			return value.getStringData();
		if(value.getNumberData() != null)
			return value.getNumberData().toString();
		if(value.getDateTimeData() != null)
			return value.getDateTimeData().
						get(Calendar.DAY_OF_MONTH) + "/"+
				   value.getDateTimeData().
				   		get(Calendar.MONTH) +"/" +
				   value.getDateTimeData().
				   		get(Calendar.YEAR);
		else
			return getAllFirstValue(value.getRefferences());
	}
	public static String getAllFirstValue(List<Object> refferences) 
	{
		Pattern pattern = Pattern.compile("(nome|name|label|nombre)");
		StringBuilder stringVals = new StringBuilder();
		
		for (Object object : refferences) 
		{
			for (Value value : object.getValores()) 
			{
				if(value.getAttribute().getTyper().isPrimitive() &&
						pattern.matcher(value.getAttribute().getName().toLowerCase()).find())
				{
					stringVals.append(getRealValue(value) + ",");
					continue;
				}
			}
		}
		if(stringVals.length() > 0)
			stringVals.deleteCharAt(stringVals.length()-1);
		return stringVals.toString();
	}

}
