TableController = function($scope,$http,$sce,$compile)
{
	window.tableController = $scope;
	$scope.principalFilial = 'Total Empresa';
	$scope.isEvolucao = false;
	$scope.showCalendar = false;
	$scope.isConsolidado = null;
	$scope.rowsCentroDeCustoRequest = false;
	$scope.orcamentoTemadre = [
/*2411295.594,	
2497363.979,	
2672761.663,	
2438392.081,	
2577450.426,	
2579021.927,	
2658137.693,	
2681334.323,	
2698652.264,	
2781570.834,	
2781062.993,	
2733880.129*/
 2566291, 	 
 2591902 ,	 
 2778878 ,	 
 2640002 ,	 
 2745577 ,	 
 2699819 ,	 
 2782731 ,	 
 2799747 ,	 
 2865643 ,	 
 2881579 ,	 
 2858211 ,	 
 2877377 
	];
	$scope.orcamentoTemadreAfret = [
	/*
	-1345097.29	,
	-1375921.73	,
	-1376859.16	,
	-1321179.06	,
	-1343189.19	,
	-1314151.83	,
	-1337711.75	,
	-1367956.75	,
	-1388612.29	,
	-1392205.29	,
	-1384296.76	,
	-1389455.1	
	*/
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-181966.15,
		-281873.80
	];
	$scope.orcamentoTemadreCombust = [
	 -74754, 	 
	 -74754, 	 
	 -79613, 	 
	 -79613, 	 
	 -79613, 	 
	 -79613, 	 
	 -84788, 	 
	 -84788, 	 
	 -84788, 	 
	 -84788, 	 
	 -84788, 	 
	 -84788 
	]
	$scope.orcamentoDespTec = [
		-39*1e3,
		-37*1e3,
		-36*1e3,
		-65*1e3,
		-61*1e3,
		-65*1e3,
		-83*1e3,
		-51*1e3,
		-53*1e3,
		-64*1e3,
		-64*1e3,
		-55*1e3
	];
	$scope.orcamentoDespOperacionais = 
	[	-19*1e3,
		-11*1e3,
		-14*1e3,
		-13*1e3,
		-10*1e3,
		-11*1e3,
		-16*1e3,
		-12*1e3,
		-13*1e3,
		-12*1e3,
		-13*1e3,
		-10*1e3];
   
   $scope.orcamentoProvDocagem = [
   	-29*1e3,
	-29*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3,
	-30*1e3

   ];
   $scope.orcamentoSegurosPEI = [
	-21*1e3,
	-24*1e3,
	-31*1e3,
	-21*1e3,
	-32*1e3,
	-21*1e3,
	-32*1e3,
	-21*1e3,
	-11*1e3,
	-23*1e3,
	-34*1e3,
	-23*1e3
   ];

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
	$scope.selectTextOrcadoRealizado = function() 
	{
	    var doc = document
	        , text = doc.querySelector('orcadorealizadotable table')
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
	
	$scope.isOpenCorporativa = false;
	$scope.openDespCorporativa = function(force)
	{
		if(force)
		{
			$scope.isOpenCorporativa = false;
		}
		else
			$scope.isOpenCorporativa = !$scope.isOpenCorporativa;
		
		if($scope.isOpenCorporativa)
			$('[id="99100600"],[id="99100610"]').show();
		else
			$('[id="99100600"],[id="99100610"]').hide();
	};
	
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
		                width: 1200,
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
		 			
		 			setTimeout(function()
 					{
			 			var x = document.querySelector('[id="9898989"]');
			 			var j = document.querySelector('[id="99100110"]');
			 			var y = document.querySelector('[id="ebt-gerencial"]');
			 			var z = document.querySelectorAll('[id="ebt-cada-filial"]');
			 			var w = document.querySelectorAll('[id="desp-corporativa"]');
			 			
			 			$(x).after(y);
			 			$(y).after(z);
			 			
			 			$(j).after(w);
			 			

			 			var mtz = document.querySelector('[meta-id="ebt-cada-filial-MTZ"]');
			 			var mtzScope = angular.element(mtz).scope();
			 			
			 			$scope.openDespCorporativa(true)
			 			
 					},3000);
					function SumBetween(X,a,b){var S=0;_.each(X,function(x,k){ if(a <= k && k <= b) { S+= x } }); return S;}
					_.each($scope.rows,function(row){
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "RECEITA BRUTA" || row.nomeGrupoMaster == "RECEITA LÃQUIDA")) 
						{
							row.vlrOrcado = SumBetween($scope.orcamentoTemadre,$scope.mesAtual-1,$scope.mesFinal-1);
						}
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "RECEITA BRUTA" || row.nomeGrupoMaster == "RECEITA LÃQUIDA")) 
						{
							row.vlrOrcado = row.vlrOrcado - SumBetween($scope.orcamentoTemadre,$scope.mesAtual-1,$scope.mesFinal-1);
						}
						//orcamentoTemadreAfret
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "CUSTOS DE AFRETAMENTO")) 
						{
							var x = SumBetween($scope.orcamentoTemadreAfret, $scope.mesAtual-1,$scope.mesFinal-1 );
							row.vlrOrcado =  x - x*0.0925;
						}
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "CUSTOS DE AFRETAMENTO")) 
						{
							var x = SumBetween($scope.orcamentoTemadreAfret, $scope.mesAtual-1,$scope.mesFinal-1 );
							var TOTAL_SDRTMD = _($scope.rows).filter(function(r){ return r.filial == "SDR + TMD" && (r.nomeGrupoMaster == "CUSTOS DE AFRETAMENTO") })[0].vlrOrcado
							row.vlrOrcado = TOTAL_SDRTMD - (x - x*0.0925);
						}
						//orcamentoTemadreCombust
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "CUSTO DE COMBUSTÍVEL")) 
						{
							row.vlrOrcado = SumBetween($scope.orcamentoTemadreCombust, $scope.mesAtual-1,$scope.mesFinal-1 );
						}
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "CUSTO DE COMBUSTÍVEL")) 
						{
							row.vlrOrcado = row.vlrOrcado - SumBetween($scope.orcamentoTemadreCombust, $scope.mesAtual-1,$scope.mesFinal-1 );
						}
						//orcamentoDespTec
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "DESP TÉCN E MANUTENÇÃO")) 
						{
							row.vlrOrcado = SumBetween($scope.orcamentoDespTec, $scope.mesAtual-1,$scope.mesFinal-1 );
						}
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "DESP TÉCN E MANUTENÇÃO")) 
						{
							row.vlrOrcado = row.vlrOrcado - SumBetween($scope.orcamentoDespTec, $scope.mesAtual-1,$scope.mesFinal-1  );
						}
						//orcamentoDespOperacionais
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "OUTRAS DESPESAS OPERACIONAIS")) 
						{
							row.vlrOrcado = SumBetween($scope.orcamentoDespOperacionais, $scope.mesAtual-1,$scope.mesFinal-1 );
						}
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "OUTRAS DESPESAS OPERACIONAIS")) 
						{
							row.vlrOrcado = row.vlrOrcado - SumBetween($scope.orcamentoDespOperacionais, $scope.mesAtual-1,$scope.mesFinal-1  );
						}
						//orcamentoProvDocagem
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "PROVISÕES PARA DOCAGEM")) 
						{
							row.vlrOrcado = SumBetween($scope.orcamentoProvDocagem, $scope.mesAtual-1,$scope.mesFinal-1 );
						}
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "PROVISÕES PARA DOCAGEM")) 
						{
							row.vlrOrcado = row.vlrOrcado - SumBetween($scope.orcamentoProvDocagem, $scope.mesAtual-1,$scope.mesFinal-1  );
						}
						//orcamentoSegurosPEI
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "SEGUROS E P&I")) 
						{
							row.vlrOrcado = SumBetween($scope.orcamentoSegurosPEI, $scope.mesAtual-1,$scope.mesFinal-1 );
						}
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "SEGUROS E P&I")) 
						{
							row.vlrOrcado = row.vlrOrcado - SumBetween($scope.orcamentoSegurosPEI, $scope.mesAtual-1,$scope.mesFinal-1  );
						}
						if(row.filial == "TMD" && (row.nomeGrupoMaster == "DESPESAS ADMINISTRATIVAS")) 
						{
							row.vlrRealizado = 0;
						}
						if(row.filial == "SDR" && (row.nomeGrupoMaster == "DESPESAS ADMINISTRATIVAS")) 
						{
							row.vlrRealizado = row.vlrRealizado + _.first(_.filter(tableController.rows,function(r){ return r.filial == "TMD" & r.nomeGrupoMaster == "DESPESAS ADMINISTRATIVAS"})).vlrRealizado;
						}

					});

					if(options && options.isTMD) {
						setTimeout(function() {
							$scope.principalFilial = "TMD"
							$scope.$apply(function() {
								
								$scope.rows = _.filter($scope.rows,function(r){ return r.filial == "TMD" });

							})
						},1000)
						setTimeout(function() {
						$('tr[grupomaster="RECEITA BRUTA"],tr[grupomaster="RECEITA LÍQUIDA"],tr[grupomaster="PESSOAL OPERACIONAL"],tr[grupomaster="RECEITA BRUTA"],tr[grupomaster="CUSTOS DE AFRETAMENTO"],tr[grupomaster="CUSTO DE COMBUSTÍVEL"],tr[grupomaster="DESP TÉC E MANUTENÇÃO"],tr[grupomaster="RECEITA BRUTA"],tr[grupomaster="SEGUROS E P&I"],tr[grupomaster="PROVISÕES PARA DOCAGEM"],tr[grupomaster="OUTRAS DESPESAS OPERACIONAIS"],tr[grupomaster="DESPESAS ADMINISTRATIVAS"],tr[grupomaster="DESP. COMERCIAIS E COMISSÕES"]')
							.show().css('opacity',1)
						$('tr[grupomasternegrito]').hide().css('opacity',0)
						$('tr#ebt-gerencial,tr#desp-corporativa').hide();
						},1000)

					}
		 		}
		 	},
			{
				method:'consumo_de_frota',
				title:'Consumo de Frota (Conferencia)',
				bus:"http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo=snapp_consumofrota&_="+new Date().getTime(),
				onComplete:function(rel,options){
			/*
			<td>{{ r.filial }}</td>
			<td>{{ r.reb}}</td>
			<td>{{ r.nvlInicial }} </td>
			<td>{{ r.transferencia }}</td>
			<td>{{ r.apd }}</td>
			<td>{{ r.compra }}</td>
			<td>{{ r.nvlFinal }}</td>
			<td>{{ r.consumo }}</td>
			<td>{{ r.hmpc }}</td>
			<td>{{ r.hmca }}</td>
			*/
					$scope.consumoRows = [];
					_.each($scope.rowsModal,function(r){
						var o = {
							reb:r.reb,
							nvlInicial:parseInt(r.nvlInicial|0),
							transFornec: parseInt(r.fornec|0),
							transReceb:parseInt(r.receb|0),
							filial:r.filial,
							apd:parseInt(r.apd|0),
							compra:parseInt(r.compra|0),
							nvlFinal:0,
							consumo:parseInt(r.consumo|0),
							hmpc:parseFloat((r.hmcp|0) && r.hmcp.replace(',','.')),
							hmca:parseFloat((r.hmca|0) && r.hmca.replace(',','.'))
						};
						$scope.consumoRows.push(o);
					});
					$.get("http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo=snapp_nivelinicial&mes="+$scope.mesAtual+'&ano='+$scope.anoAtual+"&_="
						+new Date().getTime(),
						function(data){
							var data = JSON.parse(data);
							console.log("Nvl Inicial");
							console.log(data)
							
							$scope.$apply(function() {	
								_.each($scope.consumoRows,function(c){
									_.each(data,function(d){
										if(d.reb == c.reb) {
											c.nvlInicial = parseInt(d.saldo|0)
											c.nvlFinal = c.nvlInicial + c.compra - c.transFornec + c.transReceb - c.consumo + c.apd;
										}
									})
								})
								
					$.get("http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo=snapp_estoquecombustivel&mes="+$scope.mesAtual+'&ano='+$scope.anoAtual+"&_="
						+new Date().getTime(),
						function(data){
							var data = JSON.parse(data);
							console.log("Nvl Inicial");
							console.log(data)
							
							$scope.$apply(function() {	
								_.each($scope.consumoRows,function(c){
									_.each(data,function(d){
										if(d.reb == c.reb) {
											c.estoqueEntrada = parseInt(d.entrada|0);
											c.estoqueSaida = parseInt(d.saida|0);
											c.reb = c.reb+'('+d.mov_tpestoque+')';
										}
									})
								})
								$.get("http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo=snapp_consumoviagem&mes="+$scope.mesAtual+'&ano='+$scope.anoAtual+"&_=",
								function(data){
									$scope.viagemRows = [];
									var data = JSON.parse(data);
									console.log("Nvl Viagem");
									console.log(data)
									var Y = _.union(
										calc(["Viajando"],"Viagem"),
										calc(["Fora de Operacao","Manutencao","Docando","Teste","Teste de Maquina"],"Docagem"));
									
									function calc(tp,tp2){
										var Y = [];
										var last = {TP_CONSUMO:null};
										var x = {}
										_.each(data, function(d) {
											if (_(tp).contains(d.TP_CONSUMO)) {
												if(!_(tp).contains(last.TP_CONSUMO))
													Y.push(x = {
															nvlInicial:parseInt(d.SALDO),
															transReceb:0,
															transFornec:0,
															consumo:0,
															apd:0,
															compra:0,
															filial:tp2,
															reb:d.reb
														});
												x.consumo 		+= parseFloat(d.consumo|0);
												x.compra 		+= parseInt(d.compra|0);
												x.transFornec 	+= parseInt(d.fornec|0);
												x.transReceb 	+= parseInt(d.receb|0);
												x.apd += parseInt(d.apd|0)
											}
											last = d;
										});
										return Y;
									};
									$scope.$apply(function(){
										$scope.viagemRows = Y;
									})
								}
								);
							})
							
						});
							})
							
						});
				}
			},
			{
				method:'previsao_faturamento',
				title:'Previsao de Faturamento',
				bus:"https://sn-indisponibilidade.firebaseio.com/relatorios/previsao-faturamento/",
				onInit:function(rel,options)
				{
					$scope.rowsModalRequest = false;
					$scope.loadingOrcamento = true;
					$scope.loadingFaturamento = true;
					$scope.loadingRecibos = true;
				},onComplete:function(rel,options)
				{
					$scope.getFromTopWeb();
					$scope.rowsModalRequest = false;
					function daysInMonth(iMonth, iYear)
					{
						return 32 - new Date(iYear, iMonth, 32).getDate();
					}
					var D = daysInMonth($scope.mesAtual-1,$scope.anoAtual);
					var d = tableController.mesAtual == new Date().getMonth()+1 ? new Date().getDate() : D;
					
						
					for(var i = 0;i<tableController.rows.length;i++)
					{
						var row = tableController.rows[i]
						if(row.nomeGrupoMaster == "RECEITA BRUTA" && row.filial != 'MTZ') {
							if($scope.rowsModal=="null") 
								$scope.rowsModal = {};
							if(!$scope.rowsModal[row.filial])
								$scope.rowsModal[row.filial] = {};
								
								$scope.rowsModal[row.filial].orcado = row.vlrOrcado;
								$scope.rowsModal[row.filial].orcadoProporcional = row.vlrOrcado/D * d;
						}
					}
					$scope.loadingOrcamento = false;
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
	
	

	$scope.anoComparativo = 2017;
	
	$scope.labelAno = function(ano)
	{
		if(ano == $scope.anoComparativo)
			{
				$scope.anoComparativoReal = ($scope.anoAtual-2);
				$scope.anoComparativoOrc = ($scope.anoAtual-1);
				
				return 'Comp. Orc '+($scope.anoComparativo-1)+' x Real '+($scope.anoComparativo-2);
			}
		else
			return ano;
	};
	
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
	$scope.anoAtual = 2016//new Date().getFullYear();//2014
	$scope.hojeMes  =  new Date().getMonth()+1;//$scope.mesAtual = 3//new Date().getMonth();// 12
	$scope.mesAtual = $scope.hojeMes - 1;
	$scope.mesFinal =  $scope.mesAtual;
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
			init();
		}
	}
	if(window.location.host == "127.0.0.1:8080")
	{
		//__call_login({status:true,perfil:{isMaster:true}});
	}
	
	
	$scope.colInUse = function(c)
	{
		return (!$scope.isEvolucao)||($scope.evolucaoColInUse[c]);
	};
	$scope.getPosition = function()
	{
		return $("#scrollLedContainer").scrollLeft() + 30;
	};
	
	$scope.inShowButtonBlackList = function(row)
	{
		var index = ['99100110','9898989','99100125','99100600','99100610']
						.indexOf((row.idGrupoMaster)) ;
		
		return row && index > -1;
	}
	$scope.inShowButtonBlackList2 = function(row)
	{
		var index = ['99100600','99100610']
						.indexOf((row.idGrupoMaster)) ;
		
		return row && index > -1;
	}
	
	$scope.inBlackList = function(row)
	{
		var index = ["10"
		             ,"15"
		             ,"16"
		             ,"18"
		             ,"9999"
		             ,"18988"
		             ,"28482"
		             ,"99100125"
					 ,"58" //Desp Financ
		             ].indexOf((row.idGrupoMaster)) ;
		
		return row && index > -1;
	};
	
	$scope.openGrupoMaster = function(nomeGrupoMaster)
	{
		var x = $('[grupoMaster="'+nomeGrupoMaster+'"]')
		var v = parseInt(x.css('opacity'))
		var isClosed =  v == 0;
	
		
		if(isClosed)
		{
			x.css('display','table-row');
			TweenMax.staggerTo(x,.5,{css:{opacity:1}},.1);
		}
		else
		{
			x.css({opacity:0,display:'none'})
		}
		
		console.log(x)
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
		if(options && options.filial == "TMD" && rel.method == "orcadorealizado") {
			options.filial = "Total Empresa";
			options.isTMD = true;
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
			    method: params.finder == 'previsao_faturamento' ? 'GET' : 'POST',
			    url: params.finder == 'previsao_faturamento' ? (URL.replace('?','') + $scope.mesAtual+"-"+$scope.anoAtual+'.json') : (URL),
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
		$scope.RECEITA_BRUTA_DIFF = 0;
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
			
			if(mesAtualLabel == 'visao_ano')
			{
				$scope.mesAtual = 1;
				$scope.mesFinal = 12;//mes Passado
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
	
	$scope.relsMap = {};
	
	$scope.openRelatorio = function(rel,options)
	{
		/**
		var x = (JSON.stringify(rel))+'@@'+JSON.stringify(options);
		
		if(!$scope.relsMap[x])
			$scope.relsMap[x] = new Date();
		else
			{
				var a = $scope.relsMap[x];
				var b = new Date();
				var t = new Date(b-a).getSeconds();
				if(t <= 1)
					return;
			}
		*/	
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
	$scope.perc = function(v,s)
	{
		var v2 = v*100;
		if(s && (s.toUpperCase().indexOf('RECEITA') > -1 || s.indexOf('DESP. CORPORATIVA') >-1 ))
		{
			v2 = v2 * -1;
		}
		if(s=="EBITDA")
			v2 = v2/100;
		var g = new String(v2).match(/\-*\d+\.*\d{1,2}/);		
		var x = (g ? g[0] : "0")+" %";
		return x;
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
	$scope.getFromTopWeb = function(){
		$http.get("http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo=snapp_faturamento&mes="+$scope.mesAtual+"&ano="+$scope.anoAtual+"&_="+new Date().getTime())
			.success(function(O) 
			{
				while(_.isString($scope.rowsModal)){}
					
				
				_.each(O,function(o){
						function calc(filial){
							if(!$scope.rowsModal[filial])
								$scope.rowsModal[filial] = {};
							if(!$scope.rowsModal[filial][o.fat])
								$scope.rowsModal[filial][o.fat] = {};
								
							if(!$scope.rowsModal[filial][o.fat]['faturado'])
								$scope.rowsModal[filial][o.fat]['faturado'] = 0;
							if(!$scope.rowsModal[filial][o.fat]['carta-de-credito'])
								$scope.rowsModal[filial][o.fat]['carta-de-credito'] = 0;
							if(!$scope.rowsModal[filial][o.fat]['realizado-nao-faturado'])
								$scope.rowsModal[filial][o.fat]['realizado-nao-faturado'] = 0;
							
							if(o.nf && o.nf.match(/^\d+$/)) {
								$scope.rowsModal[filial][o.fat]['faturado'] += parseFloat((o.vlr||"0").replace(',','.'))
								if(!$scope.rowsModal[filial][o.fat+'-set-faturada'])
									$scope.rowsModal[filial][o.fat+'-set-faturada'] = [];
								$scope.rowsModal[filial][o.fat+'-set-faturada'].push(o);
							}
							else {
								$scope.rowsModal[filial][o.fat]['realizado-nao-faturado'] += parseFloat((o.vlr||"0").replace(',','.'))
								if(!$scope.rowsModal[filial][o.fat+'-set-naofaturada'])
									$scope.rowsModal[filial][o.fat+'-set-naofaturada'] = [];
								$scope.rowsModal[filial][o.fat+'-set-naofaturada'].push(o);
							}
								
							$scope.rowsModal[filial][o.fat]['carta-de-credito'] += -(
								parseFloat((o.vlrCC||"0").replace(',','.')) 
								* parseFloat((o.vlrCamb||"0").replace(',','.')));
						}
						calc(o.filial)
						if(o.filial.match(/(SDR|TMD)/)) {
							calc('SDR + TMD')
						}
						if(o.filial.match(/(RJ1|SEP)/)) {
							calc('RJ1 + SEP')
						}
							
					})
					if(!$scope.rowsModal['RJ1 + SEP'].faturas)
						$scope.rowsModal['RJ1 + SEP'].faturas = {};
						
					$scope.rowsModal['RJ1 + SEP'].faturas.esperado = 
						($scope.rowsModal['SEP'].faturas.esperado|0) + ($scope.rowsModal['RJ1'].faturas.esperado|0)
						
					$scope.rowsModal['RJ1 + SEP'].invoices.esperado = 
						($scope.rowsModal['SEP'].invoices.esperado|0) + ($scope.rowsModal['RJ1'].invoices.esperado|0)
						
					if(!$scope.rowsModal['SDR + TMD'].faturas)
						$scope.rowsModal['SDR + TMD'].faturas = {};
					if(!$scope.rowsModal['TMD'].faturas)
						$scope.rowsModal['TMD'].faturas = {};	
					
					if(!$scope.rowsModal['TMD'].faturas)
						$scope.rowsModal['TMD'].faturas = {};
					
					if(!$scope.rowsModal['TMD'].invoices)
						$scope.rowsModal['TMD'].invoices = {};
						
					
					$scope.rowsModal['SDR + TMD'].faturas.esperado = 
						($scope.rowsModal['SDR'].faturas.esperado|0) + ($scope.rowsModal['TMD'].faturas.esperado|0)
					$scope.rowsModal['SDR + TMD'].invoices.esperado = 
						($scope.rowsModal['SDR'].invoices.esperado|0) + ($scope.rowsModal['TMD'].invoices.esperado|0)
					
					$scope.loadingFaturamento = false;
			});
			$http.get("http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo=snapp_recibos&mes="+$scope.mesAtual+"&ano="+$scope.anoAtual+"&_="+new Date().getTime())
			.success(function(L) 
			{
				while(_.isString($scope.rowsModal)){}
				
				_.each(L,function(l){
						function calc(filial) {
							if(!$scope.rowsModal[filial])
								$scope.rowsModal[filial] = {}
							if(!$scope.rowsModal[filial].recibos)
								$scope.rowsModal[filial].recibos = 0;
								
							$scope.rowsModal[filial].recibos += parseFloat((l.vlr||"0").replace(',','.'));
						}
						calc(l.filial)
						if(l.filial.match(/(SDR|TMD)/)) {
							calc('SDR + TMD')
						}
						if(l.filial.match(/(RJ1|SEP)/)) {
							calc('RJ1 + SEP')
						}
					})
				$scope.loadingRecibos = false;
			});
			
	}
	$scope.labelGrupoMaster = function(nomeGrupoMaster) {
		if(nomeGrupoMaster == "CUSTOS DE AFRETAMENTO")
			return "CUSTOS DE AFRET."
		if(nomeGrupoMaster == "DESP TÉCN E MANUTENÃ‡ÃƒO")
			return "DESP TÉCN/MANUNT."
		if(nomeGrupoMaster == "PROVISÃ•ES PARA DOCAGEM")
			return "PROV. PARA DOCAGEM"
		if(nomeGrupoMaster == "OUTRAS DESPESAS OPERACIONAIS")
			return "DESP. OPERACIONAIS"
		if(nomeGrupoMaster == "DESPESAS ADMINISTRATIVAS")
			return "DESP. ADMINISTRATIVAS";
		if(nomeGrupoMaster == "DESP. COMERCIAIS E COMISSÕES")
			return "COMERCIAIS/COMISSÕES"
		if(nomeGrupoMaster == "DESPESAS EXTRAORDINÁRIAS")
			return "DESP. EXTRAORDINÁRIAS";
		return nomeGrupoMaster;
	}
	$scope.miniCarga = function() {
			$scope.loadingCarga = true;
			setTimeout(function(){
				
				$scope.RECEITA_BRUTA_DIFF = 0;
				$http.get("http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo=snapp_faturamento_realtime&mes="+$scope.mesAtual+"&ano="+$scope.anoAtual+"&_="+new Date().getTime())
				.success(function(F){
					_.each(F,function(f){
						_.each($scope.rows,function(row){
							if((row.filial == f.filial) && row.nomeGrupoMaster == "RECEITA BRUTA") {
								var v = parseFloat(f.vlr.replace(',','.'));
								if(v != row.vlrRealizado || row.dt) {
									
									row.vlrRealizadoOld = row.vlrRealizadoOld || row.vlrRealizado;
									row.vlrRealizado = v
									$scope.RECEITA_BRUTA_DIFF += row.vlrRealizado - row.vlrRealizadoOld;
									
									row.dt = f.dt;
								}
							}
						})
					});
					alert("Carga concluÃ­da com sucesso!");
					$scope.loadingCarga = false;
				})
			},500)
	}
	$scope.edit = function(namespace,filial) {
		var td = $("*[namespace=\""+namespace+"\"][filial="+filial+"]")[0];
		var Input = $(td).find('input');
		var Value = $(td).text().replace('.','').replace('R$','').trim();
		var Filial = $(td).attr('filial');
		
		if(!Input.length) {
			var I = $('<input class="vlInput" value="'+Value+'" />');
			$(td).append(I)
			I.on('blur',function(){
				commit();
				$(this).remove()
			});
			I.on('keydown',function(e){
				if(e.keyCode == 13)
					commit(), $(this).remove()
			});
			function commit() {
				var FBRef = new Firebase("https://sn-indisponibilidade.firebaseio.com/relatorios/previsao-faturamento/"+$scope.mesAtual+"-"+$scope.anoAtual+"/"+Filial)
				var K = namespace.split('.');
				$scope.$apply(function(){
					var v = parseInt(I.val());
					if(!$scope.rowsModal[Filial][K[0]])
						$scope.rowsModal[Filial][K[0]] = {}
						
					$scope.rowsModal[Filial][K[0]][K[1]] = v;
					FBRef.child(K[0]+'/'+K[1]).set(v||0)
				})
				
			}
		}
		else
			Input[0].value = Value;
	}
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