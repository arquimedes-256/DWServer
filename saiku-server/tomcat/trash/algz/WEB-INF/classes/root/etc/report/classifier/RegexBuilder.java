package root.etc.report.classifier;

import java.util.regex.Pattern;

public class RegexBuilder {

	public static final Pattern IS_VOGAL = Pattern.compile(
			"[a\u00e0\u00e1\u00e3\u00e4\u00e24" +
			"e\u00e8\u00e9\u00eb\u00ea3" +
			"i\u00ec\u00ed\u00ef\u00ee" +
			"o\u00f2\u00f3\u00f6\u00f40" +
			"u\u00f9\u00fa\u00fc\u00fb]");
	
	public static final Pattern A =  	Pattern.compile("[a\u00e0\u00e1\u00e3\u00e4\u00e24]+");
	public static final Pattern E = 	Pattern.compile("[e\u00e8\u00e9\u00eb\u00ea3]+");
	public static final Pattern I = 	Pattern.compile("[i\u00ec\u00ed\u00ef\u00ee]+");
	public static final Pattern O = 	Pattern.compile("[o\u00f2\u00f3\u00f6\u00f40]+");
	public static final Pattern U = 	Pattern.compile("[u\u00f9\u00fa\u00fc\u00fb]+");
	
	private StringBuilder finalP = new StringBuilder();
	private final String originalName; 
	/**
	 * lucas -> l*?u+c*?a+s?
	 * @param p
	 */
	public RegexBuilder(String p) 
	{
		originalName = p.toLowerCase();
		
		for(Character c : p.toCharArray())
		{
			if(Pattern.compile("\\d").matcher(c.toString()).find())
			{
				finalP.append(c+"+");
			}
			else if(isVogal(c))
			{
				finalP.append(getVogal(c.toString()));
			}
			else
			{
				finalP.append(getConsoant(c.toString()));
			}
		}
	}
	private String getConsoant(String p) 
	{
		return p+"";
	}
	private String getVogal(String c) 
	{
		String r = null;
		
		if(A.matcher(c).find())
			r = A.toString();
		else if(E.matcher(c).find())
			r = E.toString();
		else if(I.matcher(c).find())
			r = I.toString();
		else if(O.matcher(c).find())
			r = O.toString();
		else if(U.matcher(c).find())
			r = U.toString();
		else
			throw new Error("vogal not exists!");
		
		return r;
	}

	private boolean isVogal(Character c) 
	{
		return IS_VOGAL.matcher(c.toString()).find();
	}
	@Override
	public String toString() {
		return String.format("(^%s$|%s|%s)",originalName,originalName,finalP.toString());
	}
	public String getOriginalName()
	{
		return originalName;
	}

}
