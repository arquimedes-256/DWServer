package root.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.model.FileStatus;
import core.base.BaseEntity;
import root.etc.AlgoozDAO;

public class FileStatusDAO extends AlgoozDAO
{
	private static FileStatusDAO instance;
	public static FileStatusDAO getInstance()
	{
		if (instance == null)
			instance = new FileStatusDAO();
		return instance;
	}
	public FileStatus getFileStatus(FileStatus fileStatus,HibernateTemplate hibernateTemplate)
	{
		return (FileStatus)this.getAll(hibernateTemplate, "", fileStatus).get(0);
	}
	@Override
	public List<?> getAll(HibernateTemplate hibernateTemplate,
			String folderName, BaseEntity baseEntityAux)
	{
		return hibernateTemplate.find("Select f from FileStatus f where f.id = ?",new Object[]{baseEntityAux.getId()});
	}
}
