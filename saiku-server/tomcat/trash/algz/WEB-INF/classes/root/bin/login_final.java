package root.bin;

import java.util.List;

import root.etc.Binary;
import algz.factory.JSONFactory;

import com.fasterxml.jackson.core.type.TypeReference;

public class login_final extends Binary {

	public static class FinalUser
	{
		public String login;
		public String pass;
	};
	public static class LoginRequest
	{
		public String user;
		public FinalUser finalUser;
	}

	@Override
	protected void init() 
	{
		String jsonString = getArgs()[1];
		LoginRequest request = (LoginRequest) JSONFactory.getInstance().fromJSON(jsonString, 
				new TypeReference<root.bin.login_final.LoginRequest>() {});
		String sql = "select o  " +
				"from Class c" +
				",Object o \n" +
				"" +
				"join o.valores v_login " +
				"join o.valores v_passw " +
				"join c.attributes a_login \n" +
				"join c.attributes a_passw \n" +
				"where 	a_login.name = 'login' "+
				"and 	a_passw.name = 'passw' " +
				"and 	v_login.stringData = ? " +
				"and 	v_passw.stringData = ? " +
				"" +
				"and v_login.attribute.id = a_login.id " +
				"and v_passw.attribute.id = a_passw.id ";
		
		List<?> list = getHibernateTemplate().find(sql,new Object[]{request.finalUser.login,request.finalUser.pass});
		

		
		getPipeline().setOutput((algz.model.Object) (list.size() > 0 ? list.get(0) : null));
	}

	@Override
	public boolean needLogin() 
	{
		return false;
	}

}
