TableController = function($scope,$http,$sce,$compile)
{
	window.tableController = $scope;
	$scope.principalFilial = 'Total Empresa';
	$scope.isEvolucao = false;
	$scope.showCalendar = false;
	$scope.isConsolidado = null;
	
	$scope.cacheTableFactory = (function()
	{
		$public = {};
		
		$public.table = {size:0};
		
		$public.put = function(url,form)
		{
			var k = $public.get(url);
			if(url && !(k))
			{
				$public.table[url] = form;
				$public.table.size++;
			}
		};

		$public.get = function(url)
		{
			console.log(url);
			return $public.table[url];
		};
		
		return $public;
	})();
	
	$scope.relatorios = 
		[
		 	{
		 		visible:true,
		 		method:'orcadorealizado',title:'Or\u00e7ado X Realizado',
		 		enableContruct:true,
		 		bus:"../bus",
		 		onInit:function()
		 		{
		 			$scope.idGrupoMaster = null;
		 		},
		 		onComplete:function(rel,options)
		 		{
		 			if(options && options.fromChangeMes)
		 				verificaPeriodo();
		 		}
		 	},
		 	{
		 		method:'orcadorealizado_detalhado',
		 		title:'Or\u00e7ado X Realizado - Detalhado',
		 		bus:"../bus",
		 		onInit:function()
		 		{
		 			
		 		}
		 		,onComplete:function(rel)
		 		{
	 				if(!rel.idContaContabil && rel.idGrupoMaster)
		 				$scope.subTitleRowsModal = $scope.rowsModal.length > 0  ? $scope.rowsModal[0].nomeGrupoMaster : ""; 
		 		}
		 	},
		 	{
		 		method:'orcadorealizado_detalhado_intern',
		 		title:'Or\u00e7ado X Realizado - Detalhado - por Conta ',
		 		bus:"../bus",
		 		isConsolidado:true,
		 		onInit:function()
		 		{
					
		 		},
		 		beforeCall:function()
		 		{
		 			$scope.rowsRequest = true;
		 		},
		 		onComplete:function(rel,options)
		 		{
		 			var f = "MTZ";
		 			var greatest = f;
		 			var idFistContaContabil = null;
		 			
		 			$scope.distinctRows0 = null;
		 			$scope.distinctRows1 = null;
		 			$scope.distinctRows2 = null;
		 			
		 			for(var k in $scope.rowsModal[0].filiais)
		 			{
		 				if(!$scope.isEmptyObj($scope.rowsModal[0].filiais[k]))
		 					f = k;
		 			}
		 			greatest = f;
		 			
		 			if($scope.rowsModal && $scope.rowsModal[0].filiais)
		 			{

			 			for(var k in $scope.rowsModal[0].filiais[f])
			 			{
			 				idFistContaContabil = k;
			 				break;
			 			}
			 			$scope.distinctRows0 = $scope.rowsModal[0].filiais[greatest];
		 			}
		 			if($scope.rowsModal[1])
		 			{
			 			if($scope.rowsModal && $scope.rowsModal[1].filiais)
			 			{
				 			$scope.distinctRows1 = $scope.rowsModal[1].filiais[greatest];
			 			}
		 			}
		 			else
		 				$scope.distinctRows1 = [];
		 			
		 			if($scope.rowsModal[2])
		 			{
			 			if($scope.rowsModal && $scope.rowsModal[2].filiais)
			 			{
				 			$scope.distinctRows2 = $scope.rowsModal[2].filiais[greatest];
			 			}
		 			}
		 			else
		 				$scope.distinctRows2 = [];
		 			
		 			if(rel)
		 			{
		 				if(!rel.idContaContabil && rel.idGrupoMaster);
		 					$scope.subTitleRowsModal =
		 						(options && options.nomeGrupoMaster)
		 								 ? options.nomeGrupoMaster :
		 									 $scope.rowsModal[0].filiais[f][idFistContaContabil].nomeGrupo; 
		 			}
		 			
		 			
		 			if(!options.fromComboInModal)
			 			if(options.filial != 'CONSOLIDE' && options.isKeepContaContabil === undefined)
			 			{
			 				$scope.openRelatorio('orcadorealizado_detalhado_intern',
						    		{
						    			idGrupoMaster:$scope.idGrupoMaster,
						    			filial:'CONSOLIDE',
						    			isKeepContaContabil:false,
						    			isConsolidado:!!options.isConsolidado,
						    			isKeepContaContabil:false,
						    			inModal:true
						    		});
			 			}

					$scope.rowsRequest = false;
		 		}

		 	}
		 	
		];
	$scope.meses = 
		[
		 	'Janeiro','Fevereiro','Mar\u00e7o','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'
		];
	$scope.mesesUtilizaveis_aloc = [null];
	$scope.mesesUtilizaveis = $scope.mesesUtilizaveis_aloc;
	$scope.filiais = 
		[
		 	'RGD','PNG','STR','SEP','RJ1','RJ1 + SEP','VIT','SDR','TMD','SDR + TMD','MAC','MTZ'
		];
	$scope.colunas = 
		[
		 	'orc','real','dif','vari'
		];
	
	$scope.evolucaoColInUse = {'orc':true,'real':true,'dif':false,'vari':false};
	
	$scope.rowsRequest = false;
	$scope.rowsModalRequest = false;
	
	$scope.distinctRows0 = [];
	$scope.distinctRows1 = [];
	
	$scope.vlrDespExtr = 0;
	$scope.$modalFilial = null;
	
	$scope.rows = [];
	$scope.rowsModal = [];
	$scope.rowsComboModal = [];
	
	
	$scope.tgl = {};
	$scope.mesFinalLabel = null;
	$scope.anoAtual = new Date().getFullYear();
	$scope.hojeMes = $scope.mesAtual = new Date().getMonth();
	$scope.mesFinal =  $scope.hojeMes;
	$scope.mesAtualLabel = $scope.meses[$scope.mesAtual-1];
	$scope.idGrupoMaster = "";
	$scope.subTitleRowsModal = "";
	$scope.isPeriodoAberto = undefined;
	$scope.loginResult = {status:true};
	
	
	$scope.logar = function(user,pass)
	{
		$http.get("../bus?user="+user+"&pass="+pass)
			.success(function(data)
				{
					$scope.loginResult = data;
					__call_login(data);
					
				});
	};
	function __call_login(data)
	{
		if(data.status)
		{
			$('.login-modal').css('position','absolute');
			TweenMax.to($('.login-modal'),.5,{css:{opacity:0},onComplete:function()
				{
					$('.login-modal').css('display','none');
					$('.login-modal').css('position','relative');
				}
			});
			
			$('.main-app').css('display','block');
			TweenMax.to($('.main-app'),.5,{css:{opacity:1}});
			$scope.showCalendar = true;
			if(!($scope.loginResult.perfil.isMaster))
			{
				$scope.filiais = $scope.loginResult.perfil.filiais;
				$scope.principalFilial = $scope.loginResult.perfil.filiais[0];
			}
		}
		init();
	}
	if(window.location.host == "127.0.0.1:8080")
	{
		//__call_login({status:true});
	}
	
	
	$scope.colInUse = function(c)
	{
		return (!$scope.isEvolucao)||($scope.evolucaoColInUse[c]);
	};
	$scope.getPosition = function()
	{
		return $("#scrollLedContainer").scrollLeft() + 30;
	};
	
	$scope.inBlackList = function(row)
	{
		var index = ["10","15","16","18","9999","18988","28482","58"].indexOf((row.idGrupoMaster)) ;
		return row && index > -1;
	};
	function verificaPeriodo()
	{
		$scope.isPeriodoAberto = undefined;
		$http.get("../bus?finder=disponibilidade_contabil&mes="+$scope.mesAtual+"&mesFinal="+$scope.mesFinal+"&ano="+$scope.anoAtual).
		success(function(data)
		{
			$scope.isPeriodoAberto = data.length === 4;
		});
	}
	function init(_i)
	{
		var i =_i || 0;
		var rel = $scope.relatorios[i];
		if(i < $scope.relatorios.length)
		{
			$http.get("views/"+rel.method+".html?_="+Math.random())
				.success(function(data) 
				{
					rel.template = data;
					if(rel.enableContruct)
					{
						if(rel.onInit)
							rel.onInit(rel);
						if(!$scope.relatorioAtual)
						{
							$scope.openRelatorio(rel);
							relatorioAtual = {};
							refreshRel(rel);
						}
					}
					i++;
					init(i);
			    });
		};
	}
	$scope.isEmptyObj = function(o)
	{
		for(var k in o)
		{
			if(k)
				return false;
		}
		return true;
	};
	function refreshRel(rel,options)
	{

		if(options && rel.method != 'orcadorealizado_detalhado_intern')
			$scope.idGrupo = options.idGrupo = null;
		 
		var locMesAtual = (options && parseInt(options.__mes)) || $scope.mesAtual;
		var locMesFinal = $scope.mesFinal;
		var locIsEvolucao = $scope.isEvolucao;
		var locIsConsolidado = undefined;
		var locFilial = undefined;
		
		if(options)
		{
			$scope.isConsolidado = options.isConsolidado;
		}
		
		
		if(options && angular.isDefined(options.isConsolidado))
			locIsConsolidado = options.isConsolidado;
		else
			locIsConsolidado = rel.isConsolidado;
			
		var locIsKeepContaContabil = true;
		
		if(options && options.inModal)
		 {

        	$('#myModal').modal({ backdrop: 'static'});
			 if($scope.isEvolucao)
			 {
				 $scope.mesAtualLabelModal = "M\u00eas "+$scope.meses[locMesAtual-1];
			 }
			 
			 //remove evolucao de modal
			 locIsEvolucao = false;
			 $scope.rowsModalRequest = true;
			 if(options.__mes)
			 {
				 locMesAtual = locMesFinal = parseInt(options.__mes);
			 }
			 if(options.isKeepContaContabil === false)
			 {
				 locIsKeepContaContabil = false;
			 }
			if(locIsKeepContaContabil)
			{
			 	$scope.rowsModal = null;
			 	if(!options.fromComboInModal)
			 		$scope.rowsComboModal = null; 
			}
				
		 }
		 else
		 {	
        	$scope.rows = [];
			$scope.rowsRequest = true;
			if(options && options.filial)
				$scope.principalFilial = options.filial;
         }

		 if(options && options.filial)
		 {
			 $scope.$modalFilial = options.filial;
		 }
		 else
		 {
			 if($scope.loginResult.perfil.isMaster)
				 $scope.$modalFilial = "Total Empresa";
			 else
				 $scope.principalFilial = $scope.$modalFilial = $scope.loginResult.perfil.filiais[0];
		 }
		 if(locIsConsolidado)
		 {
			 $scope.$modalFilial = "LISTAR";
		 }
		 if(rel.beforeCall)
			 rel.beforeCall();


		 
		 var URL = rel.bus+"?mes="+locMesAtual+
			"&ano="+$scope.anoAtual+
			"&mesFinal="+locMesFinal+
			"&finder="+rel.method+
			(options && options.idGrupoMaster ? "&idGrupoMaster="+ (options.idGrupoMaster) :"")+
			
			(locIsKeepContaContabil ? 
					(
							(
									options && options.idGrupo && ($scope.idGrupo = options.idGrupo)
							) 
					|| 
					$scope.idGrupo
					) ? 
							"&idGrupo="+($scope.idGrupo)
							:""
			:"")+
					
			($scope.$modalFilial ? "&filial="+(locFilial = (locIsKeepContaContabil ? $scope.$modalFilial : 'CONSOLIDE')).replace('+','%2B') : "")+
			(locIsEvolucao ? "&isEvolucao=true" : "")+
			(locIsConsolidado ? "&isConsolidado=true":"")+
			(locIsKeepContaContabil!== undefined ? "&isKeepContaContabil="+locIsKeepContaContabil:"");
		
		var valuesF = false;//$scope.cacheTableFactory.get(URL);
			
		if(valuesF)
		{
			$scope.onCompleteGet(URL,rel,options,valuesF.data,valuesF.x,true);
		}
		else
		{
			 $http.get(URL)
			.success(function(data) 
			{
				$scope.onCompleteGet(URL,rel,options,data,null,false);
		    });
		}
	}
	$scope.onCompleteGet = function(URL,rel,options,data,x,fromCache)
	{
		if(options)
		{
			if(options.inModal || options.isConsolidado)
			{
				if(options.idGrupo)
					$scope.idGrupo =  options.idGrupo;
			}
			else
				options.idGrupo = null;
			
			$scope.idGrupoMaster = options.idGrupoMaster;
		}
		
		if(options && options.isKeepContaContabil === false)
		{
			delete $scope.rowsComboModal;
			$scope.rowsComboModal = data;
			$scope.rowsModalRequest = false;
		}
		else if(options && options.inModal)
        {
			delete $scope.rowsModal;
        	$scope.rowsModal = data;
			$scope.rowsModalRequest = false;
        }
        else
        {
        	delete $scope.rows;
        	$scope.rows = data;
			$scope.rowsRequest = false;
        }
        if(!x)
        {
        	x = $compile(rel.template)($scope);
        }
        
		/*if(!fromCache)
			$scope.cacheTableFactory.put(URL,{x:x,data:data});*/
		
        if(options && options.inModal)
        {
    		angular.element(".template-rel-modal").html(x);
    		
        }
        else
        {
    		angular.element(".template-rel").html(x);
    		$scope.relatorioAtual = rel;
        }

        if(rel.onComplete)
        	rel.onComplete(rel,options);
	};
	$scope.list = function(object)
	{
		var s = "";
		for(var key in object)
		{
			if(object[key])
				s += key+",";
		}
		return s.replace(/,$/,"");
	};
	$scope.listLength = function(object)
	{
		var i = 0;
		for(var key in object)
		{
			if(object[key])
				i++;
		}
		return i;
	};
	$scope.inverter = function(object)
	{
		for(var key in object)
		{
			object[key] = !object[key];
		}
	};
	$scope.changeMes = function(index,mesAtualLabel,mesFinalLabel)
	{
		if(mesAtualLabel)
		{
			if(mesAtualLabel == 'Acumulado')
			{
				$scope.mesAtual = 1;
				$scope.mesFinal = $scope.hojeMes;//mes Passado
			}
			else
			{
				$scope.mesAtual = index + 1;
				//$scope.mesFinal = index + 1;
			}
		}
		if(mesFinalLabel)
			$scope.mesFinal = index + 1;
		
		if($scope.mesAtual > $scope.mesFinal)
		{
			$scope.mesFinal = $scope.mesAtual;
		}
		if($scope.mesAtual != $scope.mesFinal)
		{
			$scope.mesAtualLabel = 
				($scope.isEvolucao ? "Evolu\u00e7\u00e3o":"Acumulado")+" de "
				+$scope.meses[$scope.mesAtual-1]+" at\u00E9 "+$scope.meses[$scope.mesFinal-1];
		}
		else
		{	
			$scope.mesAtualLabel = $scope.meses[index];
			$scope.isEvolucao = false;
		}
		refreshRel($scope.relatorioAtual,{fromChangeMes:true,filial:$scope.principalFilial});
	};
	$scope.getMesLabel = function(s)
	{
		return $scope.meses[parseInt(s.$parent.key)-1];
	};
	$scope.isShowed = function(s)
	{
		return !($scope.isEvolucao && s.$parent.$index);
	};
	$scope.openRelatorio = function(rel,options)
	{
		if(typeof rel == "string")
		{
			for(var i=0;i<$scope.relatorios.length;i++)
			{
				if($scope.relatorios[i].method == rel)
					rel = $scope.relatorios[i];
			}
			if(typeof rel == "string")
				throw new Error("rel not exists");
		}

		
		
		if(options && options.inModal)		
		{
			$scope.idGrupoMaster = options.idGrupoMaster || $scope.idGrupoMaster;
			if(options && (options.mes !== undefined || options.mesFinal !== undefined))
			{
				if(options.mes == 'Acumulado')
				{
					$scope.mesAtual = 1;
					$scope.mesFinal = $scope.hojeMes-1;
				}
				else
				{
					$scope.mesAtual = options.mes +1 || $scope.mesAtual;
					$scope.mesFinal = options.mesFinal +1 || $scope.mesFinal ||  options.mes;
				}	
				if($scope.mesAtual > $scope.mesFinal)
					$scope.mesFinal = $scope.mesAtual;
				
				if($scope.mesAtual != $scope.mesFinal)
				{
					$scope.mesAtualLabelModal = 'Acumulado de '+$scope.meses[$scope.mesAtual-1]+" at\u00E9 "+$scope.meses[$scope.mesFinal-1];
				}
				else
					$scope.mesAtualLabelModal = $scope.meses[options.mes];
			}
			else
			{
				if(options && options.mes)
					$scope.mesAtualLabelModal = $scope.meses[options.mes];
			}
			if(!$scope.mesAtualLabelModal)
				$scope.mesAtualLabelModal = $scope.mesAtualLabel;
					
			$scope.relatorioAtualInModal = rel;
			$scope.subTitleRowsModal = options.nomeGrupoMaster;
		}
		refreshRel(rel,options);
	};
	$scope.sum = function(fieldValue,metric,series,fieldName,filial,mes,enablePlus)
	{
		var E = 0;
		series = series || $scope.rows;
		fieldName = fieldName || "nomeGrupoMaster";
		
		if($scope.$modalFilial.indexOf('+') != -1)
			enablePlus = true;
		
		for(var i=0;i<series.length;i++)
		{
			if(!filial || filial == series[i]["filial"])
			{
				if(!series[i].filial || (enablePlus || series[i].filial.indexOf('+') == -1))
				{
					if(mes !== null && mes != undefined  && ($scope.meses.indexOf(mes)+1) != series[i]["mes"])
						continue;
					
					if(fieldValue && fieldValue == series[i][fieldName])
					{
						E += series[i][metric];
					}
					else if(fieldValue == null)
					{
						E += series[i][metric];
					}
				}
			}
		}
		return $scope.numeralize(E) ;
	};
	$scope.numeralize = function(num,r)
	{
		return Math.round(num/1000);
	};
	$scope.perc = function(v)
	{
		g = new String(v*100).match(/\-*\d+\.*\d{1}/);				
		return (g ? g[0] : "0")+" %";
	};
	$scope.getStyle = function(v)
	{
		
		v = parseInt(new String(v).replace("%",""));
		if(v == 0)
			return 'neutro';
		return v > 0;
	};
	$scope.buildRow = function(index,filial,idContaContabil,nomeContaContabil)
	{
		var r = null ;
		var ind = null;
		if($scope.rowsModal && (ind = $scope.rowsModal[index]))
		{
			var fz = ind.filiais[filial];
			r = fz[idContaContabil];
		}
		
		if(!r)
			r = {idContaContabil:idContaContabil,
				vlrOrcado:0,
				vlrRealizado:0,
				nomeContaContabil:nomeContaContabil,
				filial:idContaContabil
			};
		
		return r;
	};
};