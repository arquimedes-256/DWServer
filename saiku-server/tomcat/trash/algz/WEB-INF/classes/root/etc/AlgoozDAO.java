package root.etc;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import core.base.BaseEntity;

public abstract class AlgoozDAO
{
	public abstract List<?> getAll(HibernateTemplate hibernateTemplate,
			String folderName, BaseEntity baseEntityAux);
}
