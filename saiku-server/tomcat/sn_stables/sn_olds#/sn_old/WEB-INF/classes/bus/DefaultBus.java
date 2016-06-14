package bus;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;

import cache.RowsCache;

import model.Options;
import model.Row;
import model.User;
import dao.LoginDAO;
import dao.SIGDAO;
import factory.JSONFactory;
import factory.RowsFactory;
import factory.UserFactory;

public class DefaultBus extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		resp.setHeader("Content-type", "text/html; charset=utf-8");

		
		String user = null;
		String pass = null;
		if((user = req.getParameter("user")) != null && (pass = req.getParameter("pass")) != null)
		{
			PrintWriter p = resp.getWriter();
			
			User u = UserFactory.getInstance().buildUser(user,pass);
			p.write(JSONFactory.getInstance().toJSON(u));
			/*
			p.write("{\"status\":"+isSuccess+",\"login\":\""+user+"\"" +
					",\"nome\":\"" + buildName(user) +"\""+
					"}");
			*/
		}
		else
			callDAO(req,resp);
	}
	@SuppressWarnings("unchecked")
	private void callDAO(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		PrintWriter p = resp.getWriter();
		ArrayList<Row> rows = null;
		String finder = req.getParameter("finder");
		SIGDAO sigdao = new SIGDAO();
		Method m = null;
		Options options = new Options();
		try 
		{
			m = SIGDAO.class.getMethod(finder, Options.class);
		} 
		catch (NoSuchMethodException e) 
		{
			e.printStackTrace();
		} 
		catch (SecurityException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			options.filial = req.getParameter("filial");
			options.mesInicial = req.getParameter("mes");
			options.mesFinal = req.getParameter("mesFinal");
			options.ano = req.getParameter("ano");
			options.idGrupoMaster = req.getParameter("idGrupoMaster");
			options.idGrupo = req.getParameter("idGrupo");
			options.isEvolucao =  Boolean.valueOf(req.getParameter("isEvolucao"));
			options.isConsolidado = Boolean.valueOf(req.getParameter("isConsolidado"));
			options.metodo = finder;
			String kcc = req.getParameter("isKeepContaContabil");
			if(kcc != null)
				options.isKeepContaContabil = Boolean.valueOf(kcc);
			
			if(options.filial != null && options.filial.equals("Total Empresa"))
				options.filial = null;
			
			if(options.mesFinal == "" || options.mesFinal == null)
				options.mesFinal = options.mesInicial;

			if(Integer.valueOf(options.mesInicial) > Integer.valueOf(options.mesFinal))
				options.mesFinal = options.mesInicial;
			
			String jsonOutput = null;
			
			if((jsonOutput = RowsCache.getInstance().get(options)) == null)
			{
				try
				{
					rows = (ArrayList<Row>) m.invoke(sigdao, 
							options);
				}
				catch(InvocationTargetException e)
				{
					rows = (ArrayList<Row>) m.invoke(sigdao, 
							options);
				}
				Object output = rows;
				
				if(options.isEvolucao)
				{
					output = new RowsFactory().buildEvolucao(rows);
				}
				else if(options.filial != null && options.filial.equals("LISTAR"))
				{
					if(options.isKeepContaContabil)
						output = new RowsFactory().buildFiliais(rows);
				}
				else if(!options.isKeepContaContabil)
				{
					output = new RowsFactory().buildTotal((ArrayList<Row>) output);
				}
				jsonOutput = JSONFactory.getInstance().toJSON(output);
				RowsCache.getInstance().put(options, jsonOutput);
			}
			p.write(jsonOutput);
			
			
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (InvocationTargetException e) 
		{
			e.printStackTrace();
		}
		
		
	}
}
