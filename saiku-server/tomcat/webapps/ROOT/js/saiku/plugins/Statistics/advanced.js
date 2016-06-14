
MeasureType =
{
	NORMAL:"NORMAL",
	PERCENT:"PERCENT"
};

DimensionType = 
{
	ROWS:"ROWS",
	COLUMNS:"COLUMNS"
};

AdvancedSts = function()
{
	this.measuresMap 	= [];
	this.percentMap 	= [];
	this.data;
	this.getMeasuresFromData = function(data)//:void
	{
		this.data = data;
		
		this.measuresMap	= [];
		this.percentMap 	= [];
		
		for ( var i = 0; i < data.length; i++) 
		{
			for ( var j = 0; j < data[i].length; j++) 
			{
				var x = data[i][j].properties;
				var v = data[i][j];
				if(x.dimension == "Measures" && v.value.match(/\$|%|QTD|\(Combustivel\)/))
				{
					var measure = new MeasureBean();
					measure.row = (i);
					measure.col = (j);
					if(v.value.match(/%/))
					{
						measure.type = (MeasureType.PERCENT);
						this.percentMap.push(i);
					}
					this.measuresMap.push(measure);
				}
			}
		}
		/* calula sum e avg */
		this.calculateSUMandAVG();
		this.calculatePERCENT();
		/* calcula sum e avg de X */
		this.calculateSUMandAVG_X();
	};
	this.calculatePERCENT = function()//:void
	{
		for ( var i = 0; i < this.measuresMap.length; i++) 
		{
			var m =  this.measuresMap[i];
			for (var j = m.row+1; j < this.data.length; j++) 
			{
				var d = this.data[j][m.col];
				if(d.type != "DATA_CELL")continue;
				if(m.type == MeasureType.PERCENT)
				{
					var o = d.properties.raw / m.sum;
					o = o * 100;
					d.properties.raw = o;
					d.value = o.formatMoney(6, "%", ".", ",");
				}
			}
		}
	};
	this.linearRegression = function(y,x)//:LinearRegression
	{
		var lr = {};
		var n = y.length;
		var sum_x = 0;
		var sum_y = 0;
		var sum_xy = 0;
		var sum_xx = 0;
		var sum_yy = 0;
		
		for (var i = 0; i < y.length; i++) {
			
			sum_x += x[i];
			sum_y += y[i];
			sum_xy += (x[i]*y[i]);
			sum_xx += (x[i]*x[i]);
			sum_yy += (y[i]*y[i]);
		} 
		lr['slope'] = (n * sum_xy - sum_x * sum_y) / (n*sum_xx - sum_x * sum_x);
		lr['intercept'] = (sum_y - lr.slope * sum_x)/n;
		lr['r2'] = Math.pow((n*sum_xy - sum_x*sum_y)/Math.sqrt((n*sum_xx-sum_x*sum_x)*(n*sum_yy-sum_y*sum_y)),2);
		
		return lr;
	};
	this.calculateSUMandAVG = function()//:void
	{
		for ( var i = 0; i < this.measuresMap.length; i++) 
		{
			var m = this.measuresMap[i];
			var n = 0;
			
			var l = m.row;
			var c = m.col;
			
			m.sum = 0;
			m.avg = 0;
			
			for ( var j = l; j < this.data.length; j++) 
			{
				var d = this.data[j][c];
				if(d.properties.raw)
				{
					m.sum += parseFloat(d.properties.raw);
					n++;
				}
			}
			m.avg = m.sum/n;
			m.count = n;
			m.avgPercent = (m.avg/m.sum) * 100;
		}
	};
	this.calculateSUMandAVG_X = function()//:void
	{
		for ( var i = 0; i < this.measuresMap.length; i++) 
		{
			var m = this.measuresMap[i];			
			m.sumX = [];
			m.avgX = [];
	
			for (var j = 0; j < this.data.length; j++) 
			{
				var $sumX = 0;
				var $avgX = 0;
				var $n = 0;
				for ( var k = 0; k < this.data[j].length; k++) 
				{
					var d = this.data[j][k];
					if(d.properties.raw)
					{
						$sumX += parseFloat(d.properties.raw);
						$n++;
					}
				}
				if($n > 0)
				{
					$avgX = $sumX/$n;
					m.sumX.push($sumX);
					m.avgX.push($avgX);
					for ( var k = 0; k < this.data[j].length; k++) 
					{
						var d = this.data[j][k];
						if(d.type != "DATA_CELL")continue;
						if(this.percentMap.indexOf(j) != -1)
						{	
							d.properties.raw = (d.properties.raw/$sumX) * 100;
							d.value = (d.properties.raw).formatMoney(10, "%" || "", ".", ",");
						}
					}
				}
			}
		}
	};
	this.getMeasuresDimension = function()//:DimensionType
	{
		if($('.fields_list_body.columns')
				.find('.ui-draggable.d_measure').length >0)
			return DimensionType.COLUMNS;
		else
			return DimensionType.ROWS;
	};
};
MeasureBean = function()
{
	this.row = 0;
	this.col = 0;
	this.type = MeasureType.NORMAL;
	this.sum = 0;
	this.avg = 0;
	this.count = 0;
	this.avgPercent = 0;
	
	this.sumX = [];
	this.avgX = [];
	this.countX = [];
	this.avgPercentX = [];
};

function sumSeriesHTML(a/*:Array*/)//:String
{
	var sum = 0;
	for ( var i = 0; i < a.length; i++) 
	{
		var vlrString = currencyToNumber($(a[i]).html());
		var vlrNumber = parseFloat(vlrString);
		sum += vlrNumber;
	}
	return (sum).formatMoney(2, "" || "", ".", ",");
}

AdvancedStsController = new AdvancedSts();

Number.prototype.formatMoney = function(places, symbol, thousand, decimal) 
{
	places = !isNaN(places = Math.abs(places)) ? places : 2;
	symbol = symbol !== undefined ? symbol : "$";
	thousand = thousand || ",";
	decimal = decimal || ".";
	var number = this, 
	    negative = number < 0 ? "-" : "",
	    i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
	    j = (j = i.length) > 3 ? j % 3 : 0;
	return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};

currencyToNumber = function(s){return s.replace(/\./g,"").replace(/,/g,".").replace("%","");};
/*
$(document).ready(function()
{
	addEventListener("click", function() {
	    var
	          el = document.documentElement
	        , rfs =
	               el.requestFullScreen
	            || el.webkitRequestFullScreen
	            || el.mozRequestFullScreen
	    ;
	    rfs.call(el);
	});
});*/