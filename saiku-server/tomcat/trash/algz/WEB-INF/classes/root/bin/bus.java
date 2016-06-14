package root.bin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import root.etc.Binary;
import root.etc.MsgCode;
import root.etc.Pipeline;
import root.etc.SimpleStream;
import algz.model.Link;
import algz.model.Program;
import algz.model.Session;
import core.base.SingletonsPool;

public class bus extends Binary
{
	Binary binaryWorker;
	HibernateTemplate template = SingletonsPool.getService()
			.getHibernateTemplate();

	private Link link;
	private Binary bin = null;

	@Override
	protected void init()
	{
		findBinaryByLink(getLink());
	}

	public Binary getBin()
	{
		return bin;
	}

	public void setBin(Binary bin)
	{
		this.bin = bin;
	}

	@SuppressWarnings("unchecked")
	private void findBinaryByLink(Link link)
	{
		ArrayList<Program> programs = null;

		programs = ((ArrayList<Program>) template.find(
				"Select p from Program as p where p.links.name = ?",
				link.getName()));
		
		buildProgramIfNotExist(link,programs);
		
		runProgram(programs);
		
		
	}

	private void runProgram(List<Program> programs) {
		try
		{
			if (programs.size() > 0)
			{
				bin = buildBinary(programs.get(0));
				bin.setPipeline(new Pipeline());
				bin.setFullShell(getFullShell());
				boolean needLogin = !bin.needLogin();
				Session s = getCurrentSession();
				if (needLogin || s != null)
				{
					bin.setPipeline(this.getPipeline());
					bin.run(getArgs(), getSession(), getRequest(),
							getResponse());
					this.setPipeline(bin.getPipeline());
				}
				else
				{
					
					bin.getPipeline()
							.setOutput(new SimpleStream("You Need Login"));
				}
			}
			else if (bin == null)
			{
				bin = this;

			}
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}

	private void buildProgramIfNotExist(Link link,List<Program> programs) {
		if(programs.size() == 0)
		{
			try 
			{
				String fullProgramName = "root.bin."+link.getName();
				Class.forName(fullProgramName);
				Program program = new Program();
				List<Link> links = new ArrayList<Link>();
				links.add(link);
				program.setName(fullProgramName);
				program.setLinks(links);
				getHibernateTemplate().save(Program.class.getName(),program);
			} 
			catch (ClassNotFoundException e1) 
			{
				this.setPipeline(new Pipeline());
				getNotificator().emmitNotification(this, MsgCode.LNK_NF, null);
			}	
		}
		
	}

	private Binary buildBinary(Program program) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		return (Binary) Class.forName(program.getName()).newInstance();
	}

	public Link getLink()
	{
		return link;
	}

	public void setLink(Link link)
	{
		this.link = link;
	}

	@Override
	public boolean needLogin()
	{
		return false;
	}


}
