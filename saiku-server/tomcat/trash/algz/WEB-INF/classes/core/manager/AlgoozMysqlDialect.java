package core.manager;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

public class AlgoozMysqlDialect extends MySQLDialect 
{
	public AlgoozMysqlDialect()
	{
		super();
		registerFunction("regexp", 
				new SQLFunctionTemplate(Hibernate.INTEGER, 
						" lower(?1) REGEXP lower(?2) "));
	}
}
