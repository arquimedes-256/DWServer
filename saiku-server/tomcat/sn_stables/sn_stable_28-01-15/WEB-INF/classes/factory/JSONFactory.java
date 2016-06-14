package factory;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONFactory 
{	
	private static ObjectMapper mapper;
	private static JSONFactory instance;
	static
	{
		mapper = new ObjectMapper().setVisibility(PropertyAccessor.ALL,Visibility.PUBLIC_ONLY);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}
	public String toJSON(Object o)
	{ 
		try 
		{
			String s = mapper.writeValueAsString(o);
			return s;
		} 
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
		}
		try
		{
			return mapper.writeValueAsString(new Error("error"));
		}
		catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Object fromJSON(String jsonString,TypeReference<?> typeReference)
	{
		Object objectList = null;
		try 
		{
			objectList =  mapper.readValue(jsonString, typeReference);
		} 
		catch (JsonParseException e) 
		{
			e.printStackTrace();
		} 
		catch (JsonMappingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return objectList;
	}
	public static JSONFactory getInstance()
	{
		if(instance == null)
		{
			instance = new JSONFactory();
		}
		return instance;
	}
}
