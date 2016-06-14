package root.etc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.lang.AlgoozList;
import algz.model.Value;
import algz.utils.StringUtils;

public class SearchBuilder 
{

	public static AlgoozList<?> buildSearch(HibernateTemplate template, long m,
			String[] keywords) {
		StringBuilder ps = new StringBuilder("");
		StringBuilder pn = new StringBuilder("");
		
		/** String map */
		ArrayList<String> psm = new ArrayList<String>();
		/** Number map **/
		ArrayList<Double> pnm = new ArrayList<Double>();
		
		ArrayList<Object> objects = new ArrayList<Object>();

		for (int i = 0; i < keywords.length; i++)
		{
				String kw = StringUtils.scapeSQL(keywords[i]);
				
				if(StringUtils.isNotNumber(keywords[i]))
				{
					if(psm.size() > 0)
						ps.append("or ");
					ps.append("lower(v1.stringData) like lower(?) or "
								+ "lower(v2.stringData) like lower(?) or "
								+ "lower(v3.stringData) like lower(?) or "
								+ "lower(v4.stringData) like lower(?) ");
					
					psm.add(kw);
					psm.add(kw);
					psm.add(kw);
					psm.add(kw);
				}
				else		
				{
					if(pnm.size() > 0)
						pn.append(" or");
					pn.append(" v1.numberData like(?) or v2.numberData like(?)");
					
					Double d;
					try
					{
						d = Double.valueOf(kw);
					}
					catch(Exception e)
					{
						d = 0d;
					}
					pnm.add(d);
					pnm.add(d);
				}
		}
		for (int i = 0; i < psm.size(); i++)
		{
			objects.add(psm.get(i));
		}
		for (int i = 0; i < pnm.size(); i++)
		{
			objects.add(pnm.get(i));
		}
		String pfn = pn.toString();
		String pfs = ps.toString();
		final String OBJECT_SEARCH_QUERY = 
			"Select distinct o,r1,r2 \n"+
				"	from Object o \n"+
				"	left join o.valores v1 \n"+
				"	left join v1.refferences r1 \n"+
				"	left join r1.valores	v2 \n"+
				"	left join v2.refferences r2 \n"+
				"	left join r2.valores v3 \n"+
				"	left join v3.refferences r3 \n"+
				"	left join r3.valores v4 \n"+
				"		where "+
				"			( \n"+
			(psm.size() > 0 ? pfs+"\n" : "\n")+
			(pnm.size() > 0 ? pfn+"\n" : "\n")+
				"			)	";
		System.out.println(OBJECT_SEARCH_QUERY);
		List<?> l = template.find(OBJECT_SEARCH_QUERY,objects.toArray());
		return getClassInList(l,m);
	}

	private static AlgoozList<algz.model.Object> getClassInList(List<?> l, long m) 
	{
		AlgoozList<algz.model.Object> l1 = new AlgoozList<algz.model.Object>();
		for (Object o : l) 
		{
			Object[] la = (Object[])o;
			for (Object object : la) 
			{
				if(object != null)
				{
					AlgoozList<algz.model.Object> Lobject = new AlgoozList<algz.model.Object>();
					
					if(!(((algz.model.Object)object).getClazz().getId() == m))
					{
						Lobject = searchInRefferences((algz.model.Object)object,m);
					}
					else
						Lobject.add((algz.model.Object) object);
					l1.addAll(Lobject);
				}
			}
		}
		return l1;
	}

	private static AlgoozList<algz.model.Object> searchInRefferences(algz.model.Object object,long m) 
	{
		AlgoozList<algz.model.Object> Lobject = new AlgoozList<algz.model.Object>();
		
		for (Value v : object.getValores()) 
		{
			for(algz.model.Object o: v.getRefferences())
			{
				if(o.getClazz().getId() == m)
					Lobject.add(o);
				else
					Lobject.addAll(searchInRefferences(o, m));
			}
		}
		return Lobject;
	}

}
