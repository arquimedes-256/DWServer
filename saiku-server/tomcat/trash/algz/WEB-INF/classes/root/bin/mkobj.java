package root.bin;

import root.etc.Binary;
import algz.factory.JSONFactory;

import com.fasterxml.jackson.core.type.TypeReference;


public class mkobj extends Binary {

	@Override
	protected void init() 
	{
		String jsonString = getArgs()[1];
		Object o = JSONFactory.getInstance()
		.fromJSON(jsonString, new TypeReference<algz.model.Object>() {});
		getHibernateTemplate().saveOrUpdate(algz.model.Object.class.getName(), o);
		return;
	}

	@Override
	public boolean needLogin() {
		return true;
	}
}
