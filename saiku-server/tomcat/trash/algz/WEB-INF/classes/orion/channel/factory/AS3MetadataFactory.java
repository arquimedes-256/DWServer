package orion.channel.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class AS3MetadataFactory 
{
	private static AS3MetadataFactory instance = new AS3MetadataFactory();
	
	public AS3MetadataFactory() 
	{
		if(!(instance == null))
		{
			try 
			{
				throw new Exception("use getInstance");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	public static AS3MetadataFactory getInstance()
	{
		return instance;
	}
	//FIXME
	public String buildXML(Class<?> c) throws NoSuchFieldException, SecurityException
	{
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><type name=\""+c.getPackage().getName()+"::"+c.getSimpleName()+"\">";
		xml += "<extendsClass type=\""+c.getSuperclass().getPackage().getName()+"::"+c.getSuperclass().getSimpleName()+"\"/>";
		xml += "<factory type=\""+c.getPackage().getName() +"::"+c.getSimpleName()+"\">";
		xml += "<extendsClass type=\""+c.getSuperclass().getPackage().getName()+"::"+c.getSuperclass().getSimpleName()+"\"/>";		
		for (int j = 0; j < c.getAnnotations().length; j++) 
		{
			Annotation an = c.getAnnotations()[j];
			xml += "<metadata name=\""+an+"\" />";
		}
		xml	+= buildFieldTags(c.getSuperclass().getDeclaredFields());
		xml	+= buildFieldTags(c.getDeclaredFields());
		
		xml += "</factory></type>";
		return xml;
	}
	private String buildFieldTags(Field[] declaredFields)
	{
		String xml = "";
		for (int i = 0; i < declaredFields.length; i++) 
		{ 
			Field f = declaredFields[i];
			String fieldName = f.getName();
			
			if(Pattern.compile("^class$|^proxyKey$|^proxyInitialized$|^serialVersionUID$")
					.matcher(fieldName)
					.find())continue;
			
			xml += "<variable name=\""+fieldName+"\" type=\""+convertReturnType(f.getType().getName())+"\">";
			Object[] annotations = f.getDeclaredAnnotations();
			for (int j = 0; j < annotations.length; j++) 
			{					
				xml += "<metadata name=\""+annotations[j]+"\" />";
			}
			xml +="</variable>";
		}
		return xml;
	}
	private String convertReturnType(String s)
	{
		/*
		switch (s) 
		{
			case "java.lang.String":
				s = "String";
				break;
			case "java.lang.Integer":
				s = "int";
				break;
			case "double":
			case "float":
			case "java.lang.Double":
			case "java.lang.Float":
				s = "Number";
				break;
			case "java.lang.Object":
				s = "Object";
				break;
			case "boolean":
			case "java.lang.Boolean":
				s = "Boolean";
				break;
			case "java.lang.Class":
				s = "Class";
				break;
			case "java.util.Calendar":
				s = "Date";
				break;
			case "java.util.Collection":
			case "java.util.ArrayList":
			case "java.util.List":
				s = "mx.collections.ArrayCollection";
				break;
		}
		*/
		return s;
	}
	
}
