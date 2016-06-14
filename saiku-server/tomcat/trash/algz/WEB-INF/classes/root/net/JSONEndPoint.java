package root.net;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import algz.factory.JSONFactory;

public class JSONEndPoint {
	
	public static void emmitResponse(HttpServletResponse response,Object output,boolean debug) throws IOException
	{
		response.setHeader("Content-type", "text/html; charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		String jsonString = JSONFactory.getInstance().toJSON(output);
		if(debug)
			jsonString = "<pre>"+jsonString+"</pre>";
		printWriter.write(jsonString);
	}
}
