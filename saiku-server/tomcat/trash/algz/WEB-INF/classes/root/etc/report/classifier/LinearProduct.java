package root.etc.report.classifier;

import java.io.Serializable;
import java.util.regex.Pattern;

import algz.model.Attribute;

public class LinearProduct implements Serializable
{
	private static final long serialVersionUID = 1L;

	private ClassifierType type;
	
	private String stringValue;
	private Pattern pattern;
	private Attribute attribute;
	private String originalValue;
	
	public String getOriginalValue() {
		return originalValue;
	}
	public LinearProduct(ClassifierType type,String stringValue,String originalValue) 
	{
		this.type = type;
		this.stringValue = originalValue;
		this.originalValue = stringValue;
	}
	public boolean isVisible()
	{
		return (type.equals(ClassifierType.ATTRIBUTE) && attribute != null) 
				|| type.equals(ClassifierType.AGREGATION) || 
				type.equals(ClassifierType.TEMPORAL_FILTER);
	}
	public LinearProduct(ClassifierType type, String stringValue, Pattern pattern) 
	{
		this(type,stringValue,"");
		this.pattern = pattern;
	}
	public ClassifierType getClassifierType(){
		return type;
	}
	public String getStringValue(){
		return stringValue;
	}
	public Pattern getPattern() {
		return pattern;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	@Override
	public String toString() {
		return type.name()+"@"+stringValue+ (pattern != null ? "#"+pattern : "");
	}
	public void setClassifierType(ClassifierType type) 
	{
		this.type = type; 	
	}
}
