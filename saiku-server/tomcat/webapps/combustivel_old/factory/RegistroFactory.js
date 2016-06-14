RegistroFactory = function()
{
	if(RegistroFactory.instance)
	{
		RegistroFactory.instance= new RegistroFactory();
	}
	
	this.buildRegistro = function()
	{
		
	};
};
RegistroFactory.getInstance = function()
{
	return RegistroFactory.instance;
};