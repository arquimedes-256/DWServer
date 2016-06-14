package root.bin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import root.dao.ObjectDAO;
import root.etc.Binary;
import root.etc.SearchModel;
import root.etc.report.ReportBuilder;
import root.etc.report.SearchResult;
import algz.factory.JSONFactory;

import com.fasterxml.jackson.core.type.TypeReference;

public class search extends Binary
{
	private SearchModel searchModel;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void init()
	{
		SearchResult result = null;
		Object l = null;
		searchModel = (SearchModel)JSONFactory.getInstance()
				.fromJSON(getArgs()[1], 
						new TypeReference<SearchModel>()
		{
		});
		if(searchModel.getT() == 0)
		{
			l =  ObjectDAO.getInstance()
					.findObject(getHibernateTemplate(), 
							searchModel.getC(), 
							searchModel.getQ());
			getPipeline().setOutput(l);
		}
		else
		{			
			ReportBuilder rp = ReportBuilder.getInstance();
			
			l = findComplex(
					rp.build(searchModel.getQ(),getHibernateTemplate(),getCurrentSession()),
					getHibernateTemplate()
			);
			
			result = new SearchResult((List<Object>) l, rp.getParamClassifier());

			getPipeline().setOutput(result);		
		} 
		return;
	}
	@SuppressWarnings("unchecked")
	public static List<?> findComplex(ArrayList<String> queryList,HibernateTemplate template)
	{
		List<? extends Object> l = new ArrayList<Object>();
		
		if(queryList == null)
		{
			return new ArrayList<Object>();
		}
		else for(String stm : queryList)
		{
			l.addAll(template.find(stm));
		}
		return l;
	}
	@Override
	public boolean needLogin()
	{
		return true;
	}

}
