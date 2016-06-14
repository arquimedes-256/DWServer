package root.etc.report;

import java.util.List;

import root.etc.report.classifier.ParamClassifier;
import core.base.BaseEntity;

public class SearchResult extends BaseEntity 
{
	private static final long serialVersionUID = 1L;
	private List<Object> data;
	private ParamClassifier paramClassifier;
	
	public List<Object> getData() {
		return data;
	}

	public ParamClassifier getParamClassifier() {
		return paramClassifier;
	}

	public SearchResult(List<Object> l, ParamClassifier paramClassifier)
	{
		if(l.size() > 0  && l.get(0) instanceof String)
		{
			toLObject(l);
		}
		this.data = l;
		this.paramClassifier = paramClassifier;
	}

	private void toLObject(List<Object> l) 
	{
		for (int i = 0; i < l.size(); i++) 
		{
			Object o = l.get(i);
			l.set(i, new Object[]{o});
		}
	}
}
