package core.base;

import orion.channel.app.OrionService;

public class SingletonsPool 
{
	private static OrionService service;
	
	public static OrionService getService() {
		return service;
	}

	public static void setService(OrionService service) {
		SingletonsPool.service = service;
	}
	
	
}
