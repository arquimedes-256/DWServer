package algz.lang;

import java.util.LinkedHashSet;

public class AlgoozList<T> extends LinkedHashSet<T> 
{

	private static final long serialVersionUID = -8504060304080696459L;
	
	@Override
	public boolean add(T e) {
		if(!contains(e))
			return super.add(e);
		else
			return false;
	}
}
