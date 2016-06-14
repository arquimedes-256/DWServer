var gf = PolymerExpressions.prototype;

gf.formatData = function(dateTime)
{
  var zeroFill = gf.zeroFill;
	if(dateTime)
	{
		var date = new Date(dateTime);
		return zeroFill(date.getDate()) + '/'
  		+ zeroFill(date.getMonth() + 1)+ '/'
  		+  date.getFullYear()
  		+ " "+
  			zeroFill(date.getHours()) + ":"+ zeroFill(date.getMinutes());
	}
	else
		return "";
};
gf.zeroFill = function(v)
{
	var a = v.toString();
	return (a.length==1?"0"+a:a);
};
gf.isName = function(valor)
{
  $const ={NAME_LABEL : ['nome','name','nombre']};
	if(!valor.attribute)
		return false;
	var lwAttr = valor.attribute.name.toLowerCase();
	for (var i = 0; i < $const.NAME_LABEL.length; i++)
	{
		var nm = $const.NAME_LABEL[i];
		if(lwAttr.indexOf(nm) > -1)
			return true;
	}
	return false;
};
gf.getValue = function(value,options)
{
  if(typeof value == "object")
  {
    var options = options || {};
  	if(value)
  	{
  		var typerName = value.attribute.typer.name;
  		if(typerName == "String")
  			return value.stringData;
  		else if(typerName == "Integer" || typerName == "Decimal")
  			return value.numberData;
  		else if(typerName == "Date")
  		{
  			if(!value.dateTimeData)
  				value.dateTimeData = Date.now();
  			if(options.onlyDate)
  				if (options.intanceDate)
  					return new Date(value.dateTimeData);
  				else
  					return value.dateTimeData;
  			else
  				return gf.formatData(value.dateTimeData);
  		}
  		else if(value.refferences && options.enableRef)
  			return value.refferences;
  		else if(typerName == "Function")
  		{
  			return evalFunction(value.attribute.functionBody,options.object);
  		}
  		else
  			return gf.getAllFirstValues(value.refferences);
  	}
  	if(options.showFirst)
  	{
  		return "F";
  	}
  	throw new Error("Pass full");
  }
	return value;
};
gf.getAllFirstValues = function(refs)
{
	var d = "";
	for (var i = 0; i < refs.length; i++)
	{
		var r = refs[i];
		d += gf.getFirstValue(r) + (i+1 == refs.length ? "" : ", ");
	}
	return d;
};
gf.getFirstValue = function(object,isChecked,times)
{
  if(object)
  {
    var count = times || 0;
    /** Prevent recursive bug */
    if(count == 3)
      return object.name;
  	var valores = object.valores;
  	var checked = isChecked || false;

  	if(valores)
    	for(var i=0;i<valores.length;i++)
    	{
    		var valor = valores[i];
    		if(checked)
    		{
    			if(valor.stringData)
    				return valor.stringData;
    			else if(valor.numberData)
    				return valor.numberData;
    			else if(valor.dateTimeData)
    				return  gf.formatData(valor.dateTimeData);
    		}
    		if(gf.isName(valor))
    		{
    			if(valor.stringData)
    				return valor.stringData;
    		}
    		if(i == valores.length-1)
    		{
    			return gf.getFirstValue(object,true,++count);
    		}
    	}
  }
  return undefined;
};