package root.etc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.model.Application;
import algz.model.Attribute;
import algz.model.Class;
import algz.model.Customer;
import algz.model.FileStatus;
import algz.model.Module;
import algz.model.Session;
import algz.model.TTY;
import algz.model.User;
import algz.utils.StringUtils;
import core.base.BaseEntity;

public class ClassDAO extends AlgoozDAO {
	public static String CLASS_FIND_QUERY = "select c from Application a "
			+ " join a.modules m " + " join m.classes c " + " where c.name = ?"
			+ " and m.id = ?" + " and a.id = ? "
			+ "and c.fileStatus.id in(2,3)";
	private static ClassDAO instance;

	public static ClassDAO getInstance() {
		if (instance == null)
			instance = new ClassDAO();
		return instance;
	}

	@Override
	public List<?> getAll(HibernateTemplate hibernateTemplate,
			String folderName, BaseEntity baseEntityAux) {
		TTY tty = (TTY) baseEntityAux;
		return hibernateTemplate.find(CLASS_FIND_QUERY, new Object[] {
				folderName, tty.getConnectedModule().getId(),
				tty.getConnectedApplication().getId() });
	}

	public void sendObjectToTrash(HibernateTemplate hibernateTemplate,
			String fileList, Module connectedModule) {
		StringBuilder sb = new StringBuilder("");
		if (fileList.contains(",")) {
			String[] LfileList = fileList.split("\\s?,\\s?");
			for (int i = 0; i < LfileList.length; i++) {
				sb.append(String.format("'%s',",
						StringUtils.scapeSQL(LfileList[i])));
			}
			sb.deleteCharAt(sb.length() - 1);
		} else
			sb.append(String.format("'%s'", StringUtils.scapeSQL(fileList)));

		List<?> list = hibernateTemplate.find(
				"select c2 from Module m " + "join m.classes c2 "
						+ "where m.id = ? and c2.name in(" + sb.toString()
						+ ")", connectedModule.getId());

		if (list.size() > 0) {
			Class c = (Class) list.get(0);
			c.setFileStatus(FileStatus.IN_TRASH);
			connectedModule.getClasses().remove(c);
			hibernateTemplate.update(c);
			hibernateTemplate.update(connectedModule);
		}
	}

	public List<?> findClassByAttributes(HibernateTemplate template,
			Customer customer, ArrayList<String> attributes,
			ArrayList<String> classes, ArrayList<String> values,
			Application application) {
		StringBuilder CLASS_QUERY = new StringBuilder("Select  c " // x)
				+ "from Application app"
				+ " join app.modules m"
				+ " join m.classes c \n"
				+ "  join c.attributes a \n"
				+ "  join c.attributes a2 \n"
				);
		boolean isFirst = true;
		for (String attr : attributes) 
		{
			if(isFirst)
				CLASS_QUERY.append(" where (");
			else
				CLASS_QUERY.append(" or ");
			if (attr.equalsIgnoreCase("name") || attr.equalsIgnoreCase("nome")
					|| attr.equalsIgnoreCase("nombre"))
				continue;
			CLASS_QUERY.append(" (regexp(a.name,'" + attr + "')=1)"
					+ " or (regexp(a.typer.name,'" + attr + "')=1) ");
			isFirst = false;
		}
		CLASS_QUERY.append("\n ) and app.customer.id = " + customer.getId());
		CLASS_QUERY.append("\n and app.id = " + application.getId());
		CLASS_QUERY.append("\n and a2.typer.name = 'Date'");
		String finalQuery = CLASS_QUERY.toString();
		List<?> rs = template.find(finalQuery);
		return rs;
	}

	public Long getAttributesCount(HibernateTemplate template, Session session,
			String p) {
		String sql = String.format("Select count(a) from "
				+ "Application app\n" + "join app.modules m\n"
				+ "join m.classes c\n" + "join c.attributes a\n" + "where\n"
				+ "app.customer.id = %d and regexp(a.name,?)=1 ", session
				.getUser().getCustomer().getId());
		List<?> l = template.find(sql, p);
		return l.size() > 0 ? (Long) l.get(0) : 0;
	}

	public boolean exists(String p, HibernateTemplate template, Session session) {
		String sql = String.format("Select count(c) from "
				+ "Application app\n" + "join app.modules m\n"
				+ "join m.classes c\n" + "join c.attributes a\n" + "where\n"
				+ "app.customer.id = %d and regexp(c.name,?)=1 ", session
				.getUser().getCustomer().getId());
		List<?> l = template.find(sql, p);
		return (Long) l.get(0) > 0;
	}

	public Class findByName(String p, HibernateTemplate template,
			Session session, Module module) {
		long customerId = 0;
		if (session != null) {
			customerId = session.getUser().getCustomer().getId();
		}
		Application app = session.getTty().getConnectedApplication();
		String sql = String
				.format("Select c from Class c "
						+ "where\n"
						+ " (regexp(c.name,?) =1) and(c.isPrimitive is true or c.id in ("
						+ " select c.id from Application a join a.modules m join m.classes c "
						+ "where a.customer.id = %d and c.fileStatus.id in(2,3) "
						+" and a.id = " + app.getId() + 
						 (module != null ? " and m.id = " + module.getId()
								: "") + ")) " +
										"group by c ", customerId);
		List<?> l = template.find(sql, p);
		return l.size() > 0 ? (Class) l.get(0) : null;
	}

	public Class findById(long id, HibernateTemplate template, Session session) {
		if (id != 0) {
			String sql = String.format("Select c from " + "Application app\n"
					+ "join app.modules m\n" + "join m.classes c\n"
					+ "join c.attributes a\n" + "where\n"
					+ "app.customer.id = %d and c.id = ? group by c ", session
					.getUser().getCustomer().getId());
			List<?> l = template.find(sql, id);
			return l.size() > 0 ? (Class) l.get(0) : null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFromUser(User user, Class clazz,
			Attribute userAttribute, HibernateTemplate hibernateTemplate) {
		String sql = "select o from Object o "
				+ "join o.clazz.attributes a "
				+ "join  o.valores v "
				+ "join v.refferences r "
				+ "join r.valores v2 "
				+ "	where o.clazz.id = ? and a.id = ? and v2.stringData = ? and r.clazz.name = 'User' and v2.attribute.name = 'Login' ";

		List<?> objects = hibernateTemplate.find(
				sql,
				new Object[] { clazz.getId(), userAttribute.getId(),
						user.getLogin() });
		return (List<Object>) objects;
	}

	@SuppressWarnings("unchecked")
	public Object getAvaliables(HibernateTemplate hibernateTemplate,
			Application connectedApplication) {
		if (connectedApplication == null)
			return new ArrayList<Object>();
		String sql_s = "Select c from " + "Class c "
				+ "where c.isPrimitive is true and c.id <= 26";

		String sql = "Select c from "
				+ "Application app\n" + "join app.modules m\n"
				+ "join m.classes c\n" + "where\n"
				+ "app.customer.id = ? and app.id = ? and c.fileStatus.id in(2,3)";

		ArrayList<Object> a = new ArrayList<Object>();

		ArrayList<Object> s = (ArrayList<Object>) hibernateTemplate.find(sql_s);
		ArrayList<Object> c = (ArrayList<Object>) hibernateTemplate.find(sql,
				new Object[] { connectedApplication.getCustomer().getId(),
						connectedApplication.getId() });
		a.addAll(s);
		a.addAll(c);
		return a;
	}
}