package root.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import core.base.BaseEntity;
import root.etc.AlgoozDAO;

public class ModuleDAO extends AlgoozDAO
{
	public static String MOD_FIND_QUERY = 
			
			"Select m from Application "
			+ "a join a.modules m "
			+"join m.classes c"
			+ "	where m.name = ?"
			+ " and a.id = ? and m.fileStatus.id in(2,3)"
			+"	and c.fileStatus.id in(2,3)";
		
	private static ModuleDAO instance;
	public static ModuleDAO getInstance()
	{
		if (instance == null)
			instance = new ModuleDAO();
		return instance;
	}

	public List<?> getAll(HibernateTemplate hibernateTemplate,
			String folderName, BaseEntity application)
	{
		return hibernateTemplate.find(MOD_FIND_QUERY,new Object[]{folderName,application.getId()});
	}

}
