

var config = { 
	"Habilitar desbloqueio": true, //**(marcar como "true" ou "false" )
									//se marcar como false, ele ignora as definicoes de desbloqueio
	
	"Bloquear a partir do mes":7, //significa que todo mes >= n estara bloqueado, 
								  //ou seja, se você escreve n=3, entao de marco para frente estara BLOQUEADO
	//"Forcar abrir dia":"01-02-2016" //**(marcar como null ou "dd-mm-yyyy")
								   //forca abrir uma determinada data para todas as filiais,  
}

//definicoes de desbloqueio
var exceptions = {
   	'1':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//VIT
   	'2':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//rj1
   	'9':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//SEP
   	'3':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//STR
  	'6':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//SDR
   	'7':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//MAC
   	'5':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//PNG
   	'8':{dtInicio:new Date(new Date("2016-06-01")-1),dtFim:new Date("2016-06-30")},//RGD ,"Forcar abrir dia":"01-02-2016"
};
if(!config["Habilitar desbloqueio"])
	exceptions = {};