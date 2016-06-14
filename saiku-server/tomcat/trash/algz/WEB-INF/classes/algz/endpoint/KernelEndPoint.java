package algz.endpoint;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import root.bin.bus;
import root.etc.Pipeline;
import root.net.JSONEndPoint;
import algz.model.Link;
import algz.model.Notification;
import algz.model.Session;
 

public class KernelEndPoint extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private bus bus;
	private static Pattern PARAMS_PATTERN = Pattern.compile("(\\S+|--?\\S+|((new)?\\s\\{.+\\})|(\\w+.+\\[.+\\]))");
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    String shell = request.getParameter("shell");
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    
	    Object[] pipedList = shell.split("\\|");
	    Pipeline lastPipe = new Pipeline();
	    
	    for (int i = 0; i < pipedList.length; i++)
		{
	    	if(!(lastPipe.getOutput() instanceof Notification))
	    		lastPipe = initShell(pipedList[i].toString(),request.getRemoteAddr(),request,response,lastPipe);
	    	else break;
		}
	    try
		{
			JSONEndPoint
			.emmitResponse(response, lastPipe.getOutput(),request.getParameter("debug")!= null);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	private String[] getGroups(Matcher matcher) 
	{
		ArrayList<String> groups = new ArrayList<String>();
		while(matcher.find())
		{
			groups.add(matcher.group(1));
		}
		return Arrays.copyOf(groups.toArray(), groups.toArray().length, String[].class);
	}
	public Pipeline initShell(String shellString,String ip, HttpServletRequest request, HttpServletResponse response,Pipeline lastPipeline) throws UnsupportedEncodingException 
	{
		String fullShell = shellString;
		String[] shells = getGroups(PARAMS_PATTERN.matcher(shellString));
		request.setCharacterEncoding("utf-8");
		Session session = new Session();
		session.setIp(ip);
		
		Link link = new Link();
		link.setName(shells[0]);
		Pipeline currentPipe = new Pipeline();
		currentPipe.setInput(lastPipeline.getOutput());
		bus = new bus();
		bus.setLink(link);
		bus.setFullShell(fullShell);
		bus.setPipeline(currentPipe);
		bus.run(shells,session,request,response);	
		
		return bus.getPipeline();
	}
	public bus getBus()
	{
		return bus;
	}
}
