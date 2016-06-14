TableController = function($scope,$http,$sce,$compile)
{
	window.tableController = $scope;
	$scope.principalFilial = 'Total Empresa';
	$scope.isEvolucao = false;
	$scope.showCalendar = false;
	$scope.isConsolidado = null;
	$scope.rowsCentroDeCustoRequest = false;

   $scope.currentMousePos = { x: -1, y: -1 };
   
	jQuery(function($) {
	    $(window).mousemove(function(event) 
	    {
	        $scope.currentMousePos.x = event.pageX;
	        $scope.currentMousePos.y = event.pageY;
	    });
	});
	$scope.selectText = function() 
	{
	    var doc = document
	        , text = doc.getElementById('selection')
	        , range, selection
	    ;    
	    if (doc.body.createTextRange) {
	        range = document.body.createTextRange();
	        range.moveToElementText(text);
	        range.select();
	    } else if (window.getSelection) {
	        selection = window.getSelection();        
	        range = document.createRange();
	        range.selectNodeContents(text);
	        selection.removeAllRanges();
	        selection.addRange(range);
	    }
	};
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
//centro_de_custo_mtz
	$scope.relatorios = 
		[
		 	{
				method:'evolucao_grafico',
		 		title:'Evolucao',
		 		bus:"../bus",
		 		onInit:function()
		 		{
		 			
		 		}
		 		,onComplete:function(rel,options)
		 		{
		 			$scope.nomeGrupo = options.nomeGrupo;
		 			$scope.subTitleRowsModal = $scope.rowsModal.length > 0  ? $scope.rowsModal[0].nomeGrupoMaster : "";
		            google.load('visualization', '1', {packages: ['corechart']});
		            google.setOnLoadCallback(drawChart);
		            
		            $scope.arrTotalOrcado = 0;
		            $scope.arrTotalRealizado = 0;
		            
		            function drawChart() {

		              var data = new google.visualization.DataTable();
		              var arr = [];
		              var allArr = [];
		              var rw = $scope.rowsModal;
		              $scope.diffTotal = 0;
		              data.addColumn('string', 'X');
		              data.addColumn('number', 'Acumulado Orcado');
		              data.addColumn('number', 'Acumulado Realizado');
		              
		              data.addColumn('number', 'Orcado');
		              data.addColumn('number', 'Realizado');
		              data.addColumn('number', 'Dif');
		              data.addColumn('number', 'Var');
		              
		              arr.push([
		                        "Acum.-"+$scope.anoAtual ,
		            			 //acumulado orcado x realizado
		            			 {v:0,f:''},
		            			 {v:0,f:''},
		            			 
		            	         {v:null,f:''}, 
		            				 
		            	         {v:null,f:''}, 

		            			 {v:null,f:''},
		            	         
		            			 {v:null,f:''}
		                        ]);
		              for(var i=0;i<12;i++)
	            	  {
			            		
		            	var vari = ($scope.perc((
       			               (rw[i].vlrOrcado-rw[i].vlrRealizado)
    			               / rw[i].vlrOrcado)*-1)|| 0)+'';
		            	var x = [		            			
		            			 {v:$scope.mesesAbrev[rw[i].mes-1],f:$scope.meses[rw[i].mes-1]}, 
		            			 //acumulado orcado x realizado
		            			 {v:0,f:''},
		            			 {v:0,f:''},
		            			 //orc
		            	         {v:rw[i].vlrOrcado,
		            				 f:"R$ "+($scope.numeralize(rw[i].vlrOrcado).formatMoney())
		            				 }, 
		            			//real
		            	         {v:rw[i].vlrRealizado,
		            				 f:"R$ "+($scope.numeralize(rw[i].vlrRealizado).formatMoney())},
		            			//dif
	            				 {v:null,
		            				 f:"R$ "+($scope.numeralize(rw[i].vlrRealizado - rw[i].vlrOrcado).formatMoney())
		            				 },
		            			//var
		            			 {v:null,f:vari}
		            	        ];
		            	
		            	x.$style = 
		            		rw[i].vlrOrcado == 
		            		rw[i].vlrRealizado ? 'info' :
		            		rw[i].vlrOrcado > rw[i].vlrRealizado ? 'danger result-true' : 'success result-false';
		            		
		            	x.$diff = rw[i].vlrOrcado - rw[i].vlrRealizado;

		            	  
		            	if(i < $scope.mesFinal)
		            	{
			            	arr.push(x);
			            	
			            	$scope.arrTotalOrcado += rw[i].vlrOrcado;
			            	$scope.arrTotalRealizado += rw[i].vlrRealizado;
		            	}
		            	else
		            		x.$style = 'info';
		            	

		            	$scope.diffTotal += rw[i].vlrOrcado;
		            	
		            	allArr.push(x);

	            	  }
		              
		              arr[0][1] = {v:$scope.arrTotalOrcado,
		            		  f:$scope.numeralize($scope.arrTotalOrcado).formatMoney()
		              };
		              
		              arr[0][2] = {v:$scope.arrTotalRealizado,
		            		  f:$scope.numeralize($scope.arrTotalRealizado).formatMoney()
		              };
		            		  
		              arr[0][5] = {v:null,
		            		  f:$scope.numeralize($scope.arrTotalRealizado - $scope.arrTotalOrcado).formatMoney()
		              };
		              
		              arr[0][6] = {v:null,
		            		  f:$scope.perc(($scope.arrTotalRealizado - $scope.arrTotalOrcado)/$scope.arrTotalOrcado)|| 0
		              };
		              
		              
		              $scope.total$style = 
		            	$scope.arrTotalOrcado == 
		            	$scope.arrTotalRealizado ? 'info' :
		                $scope.arrTotalOrcado > $scope.arrTotalRealizado ? 'danger result-true' : 
		                	'success result-false';
		            
		              $scope.diffTotalStyle = 
			            	$scope.diffTotal == 
				            	$scope.arrTotalRealizado ? 'info' :
				                $scope.diffTotal > $scope.arrTotalRealizado ? 'danger result-true' : 
				                	'success result-false';
				                
		              $scope.arr = arr;
		              $scope.allArr = allArr;
		              
		              data.addRows(arr);

		              var options = {
		                width: '100%',
		                height: 400,
		                hAxis: {
		                  title: $scope.anoAtual+""
		                },
		                vAxis: {
		                  title: 'R$'
		                },
		                crosshair: { trigger: 'both',opacity:.3 },
		                seriesType:'line',
		                series:{
		                	0:{type:'bars',targetAxisIndex:0,visibleInLegend:false,color:'#AB0D06'},
		                	1:{type:'bars',targetAxisIndex:0,visibleInLegend:false,color:'#007329'},
		                	2:{targetAxisIndex:1,color:'#AB0D06'},
		                	3:{targetAxisIndex:1,color:'#007329'},
		                	4:{targetAxisIndex:1,opacity:.1,visibleInLegend:false},
		                	5:{targetAxisIndex:1,opacity:.1,visibleInLegend:false}
		                },
		                focusTarget: 'category'
		                
		              };
		              
		              if($scope.useTrendlines)
			              options.trendlines = {
			                    2: {
			                      type: 'polynomial',
			                      degree: 3,
			                      visibleInLegend: true,
			                    },
	
			                    3: {
			                      type: 'polynomial',
			                      degree: 3,
			                      visibleInLegend: true,
			                    }
			                  };
		              var chart = new google.visualization.ComboChart(document.getElementById('ex7'));

		              chart.draw(data, options);
		            }
		            drawChart();
		              
		 		}
		 	},
		 	{
				visible:true,
				method:'centro_de_custo_mtz',title:'Por Centro de Custo',
		 		enableContruct:false,
				bus:"../bus",
				onInit:function()
				{
				},
				onComplete:function(rel,options)
				{
				}
			},
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
	$scope.mesesAbrev = 
		[
		 	'Jan','Fev','Maro','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'
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
	$scope.anoAtual = 2014//new Date().getFullYear();
	$scope.hojeMes = $scope.mesAtual = 12//new Date().getMonth();
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
		var index = ["10"
		             ,"15"
		             ,"16"
		             ,"18"
		             ,"9999"
		             ,"18988"
		             ,"28482"//,"58"
		             ].indexOf((row.idGrupoMaster)) ;
		
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

		if(options && (rel.method != 'orcadorealizado_detalhado_intern' 
			&& rel.method != 'centro_de_custo_mtz' && rel.method != 'evolucao_grafico'))
			$scope.idGrupo = options.idGrupo = null;
		 
		var locMesAtual = (options && parseInt(options.__mes)) || $scope.mesAtual;
		var locMesFinal = $scope.mesFinal;
		var locIsEvolucao = options && options.isEvolucao || $scope.isEvolucao;
		var locIsConsolidado = undefined;
		
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
			 if(!options.isEvolucao)
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
		else if(options && options.inCentroDeCusto)
		{
			$scope.rowsCentroDeCusto = null;
			$scope.rowsCentroDeCustoRequest = true;
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
		 if(locIsConsolidado && rel.method != 'centro_de_custo_mtz' && rel.method != 'orcadorealizado')
		 {
			 $scope.$modalFilial = "LISTAR";
		 }
		 if(rel.beforeCall)
			 rel.beforeCall();


		 
		 var URL = rel.bus+"?";
		 
		 var params = {
			mes:locMesAtual,
			ano:$scope.anoAtual,
			mesFinal:locMesFinal,
			finder:rel.method
		 };
			if(options && options.idGrupoMaster)
				params["idGrupoMaster"] =  (options.idGrupoMaster);
			
			if(locIsKeepContaContabil)
			{
				if(((options && options.idGrupo && ($scope.idGrupo = options.idGrupo))|| $scope.idGrupo))
					params["idGrupo"] = $scope.idGrupo;
				if(options && !options.idGrupo && rel.method== "evolucao_grafico")
				{
					delete params["idGrupo"];
				}
					 
			}
			if($scope.$modalFilial)
				params["filial"] = (locFilial = (locIsKeepContaContabil ? $scope.$modalFilial : 'CONSOLIDE')).replace('+','%2B');
				
			if(locIsEvolucao)
				params["isEvolucao"] = "true";
			if(locIsConsolidado)
				params["isConsolidado"] = "true";
			
			if(locIsKeepContaContabil!== undefined)
				params["isKeepContaContabil"] = locIsKeepContaContabil;
			
			if(options && options.idContaContabil)
				params["idContaContabil"] = options.idContaContabil;
		
		var valuesF = false;//$scope.cacheTableFactory.get(URL);
			
		if(valuesF)
		{
			$scope.onCompleteGet(URL,rel,options,valuesF.data,valuesF.x,true);
		}
		else
		{
			if(params.finder == 'orcadorealizado_detalhado_intern' && params.filial != 'CONSOLIDE' )
				params.filial = "LISTAR";
			$http({
			    method: 'POST',
			    url: URL,
			    data: $.param(params),
			    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
			}).success(function(data) 
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
		else if(options && options.inCentroDeCusto)
		{
			delete $scope.rowsCentroDeCusto;
			$scope.rowsCentroDeCusto = data;
			$scope.rowsCentroDeCustoRequest = false;
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
        else if(options && options.inCentroDeCusto)
        {
    		angular.element(".template-rel-centrocusto").html(x);
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
	$scope.changeAno = function(ano)
	{
		$scope.anoAtual = ano;
		refreshRel($scope.relatorioAtual,{fromChangeMes:true,filial:$scope.principalFilial});
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
		
		if($scope.principalFilial.indexOf('+') != -1)
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
Number.prototype.formatMoney = function(c, d, t){
	var n = this, 
	    c = isNaN(c = Math.abs(c)) ? 2 : c, 
	    d = d == undefined ? "." : d, 
	    t = t == undefined ? "," : t, 
	    s = n < 0 ? "-" : "", 
	    i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", 
	    j = (j = i.length) > 3 ? j % 3 : 0;
	   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
	 };