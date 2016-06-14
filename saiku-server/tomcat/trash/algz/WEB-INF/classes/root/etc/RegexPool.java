package root.etc;


public enum RegexPool {
	
	QUOTES_OR_NOT	("^\\w+\\s?\"?(.*?)\"?$"),
	QUOTES_W_PROP	("\\s(.*?)\\s?\\[(.*?)\\]\\s?(\\-.)?\\s?$"),
	BRACES_DATA		("^\\w+\\s*\\s?\\w+(\\{.*?\\})\\s?$"), 
	GT_1PATH		("(^\\/.+)|(\\/.+$)"), 
	PATH_SEP_SPLIT	("^\\/|\\/"), 
	CONTAINS_NON_NUMERIC ("[A-Za-z]"),
	RESERVED_CHARS	("(^@|\\/|\\{|\\}\\|)");
	
	private String value;

	
	RegexPool(String value)
	{
		this.value = value;
	}
	@Override
	public String toString() 
	{
		return value;
	}
}
