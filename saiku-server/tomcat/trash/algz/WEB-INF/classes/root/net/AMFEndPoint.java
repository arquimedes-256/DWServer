package root.net;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import flex.messaging.io.MessageIOConstants;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.AmfMessageSerializer;
import flex.messaging.io.amf.AmfTrace;
import flex.messaging.io.amf.MessageBody;

public class AMFEndPoint {

	public static void emmitResponse(HttpServletResponse response, Object output) throws IOException {
		
		response.setHeader("Content-Type", "application/x-amf;charset=x-user-defined");
	    ServletOutputStream out = response.getOutputStream();
		

	    ActionMessage requestMessage = new ActionMessage(MessageIOConstants.AMF3);

	    MessageBody amfMessage = new MessageBody();
	    
	    amfMessage.setData(output);
	    requestMessage.addBody(amfMessage);
	    
	    AmfMessageSerializer amfMessageSerializer = new AmfMessageSerializer();
	    amfMessageSerializer.initialize(SerializationContext.getSerializationContext(), out, new AmfTrace());
	    amfMessageSerializer.writeMessage(requestMessage);

	    out.close();
		
	}

}
