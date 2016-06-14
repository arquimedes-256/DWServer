function TableController() {
	var idMes = undefined;
	var idAno = undefined;
	var idRebocador = undefined;
	var idFilial = 1;
	var rHTML = $();
	var hHTML = $();
	var comandantesHTML = $();
	var respostaInsert = null;
	var loginUsuario = "";
	var rebocadoresRow = $();
	var documentIsSaved = true;
	var comandantes;
	var maritimos;
	var allInput = '.inpt-saldo,.inpt-compra,.inpt-acd,.inpt-recebido,.inpt-fornecido,.inpt-cmcp,.inpt-cmca,.inpt-hmcp,.inpt-hmca';

	function _init() {
		_addListeners();
		$('.logarSe').click(login);
		$('.salvarRegistros button').click(buildJson);
	};

	function _addListeners() {
		//.ano-dropdown li a
		//
		$('.mes-dropdown li a').bind('click', _onClickMes);
		$('.ano-dropdown li a').bind('click', _onClickAno);
		$('#login,#senha').keydown(function () {
			$('.erroLogin').fadeOut();
		});
		$('.btn-comandantes').click(function () {
			$('#modalComandantes').modal();
		});
	}

	function _setValueOnInput(h, m, refInput) {
		console.log(h, m)
		var H = h + m;
		var inputReal = $(refInput).parent().find('input.zz');
		if (isNaN(H))
			H = 0;
		inputReal.val(H);
		validaConsumo(inputReal);
	}


	function _keyDownHourInput() {
		var input = $(this);
		var v = input.val();
		var s;
		var h = 0;
		var m = 0;

		if (v.match(/\d{1,2}\:\d{1,2}/)) {
			s = v.split(':');
		} else {
			s = [v, '0'];
		}
		h = parseInt(s[0]);
		m = parseInt(s[1]) * (1 / 60) * (s[1].length == 1 ? 10 : 1);

		_setValueOnInput(h, m, input);
	}

	function minTommss(minutes) {
		var sign = minutes < 0 ? "-" : "";
		var min = Math.floor(Math.abs(minutes))
		var sec = Math.floor((Math.abs(minutes) * 60) % 60);
		return sign + (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
	}

	function setRealToMMSS() {
		$('.inpt-hmcp,.inpt-hmca').each(function () {
			var x = $(this);
			var hhmmInput = x.parent().find('.hrtype');
			var val = x.val().replace(',', '.');
			var v = parseFloat(val);
			var MMSS = minTommss(v);
			if (MMSS == "NaN:NaN")
				MMSS = "";
			if (parseInt(val) == 0)
				MMSS = "";
			hhmmInput.val(MMSS);
			console.log(v, val);
		})
	}

	function _onClickTipoConsumo() {
		var vidTipoConsumo = $(this).attr('href').replace('#', '');
		var v = $(this).text();
		$(this).parent().parent().parent().find('.tipoConsumoDrop span').text(v);
	}

	function _onClickComandantes() {
		var v = $(this).text();
		$(this).parent().parent().parent().find('.comandantesDrop span').text(v);
	}

	function _onClickRebocador() {
		var vidRebocador = $(this).attr('href').replace('#', '');
		console.log(vidRebocador)
		var v = $(this).text();
		$('.rebocadorDrop span').text(v);
		idRebocador = vidRebocador;
		_callHistorico();
	}

	function _callHistorico() {
		$('.alert-mes,.alert-rebocador,.alert-ano').fadeOut();
		if (!idMes) {
			$('.alert-mes').fadeIn();
		}
		if (!idRebocador) {
			$('.alert-rebocador').fadeIn();
		}
		if (!idAno) {
			$('.alert-ano').fadeIn();
		}
		if (idMes != undefined && idAno != undefined && idRebocador != undefined) {
			if (documentIsSaved || confirm("Você não salvou essas alterações , deseja descartar ?")) {
				getHistorico();
				_setTitleDocument();
				validaTodosConsumos();
				$('.hrtype').mask('99:99', {
					placeholder: "00:00"
				});
				$('.salvarRegistros').fadeOut();
				documentIsSaved = true;
			}
		}
	}

	function sumAllInput() {
		var consumo = sumInputs('.inpt-cmcp') + sumInputs('.inpt-cmca') //+ sumInputs('.inpt-acd');
		var hrMCP = sumInputs('.inpt-hmcp');
		$('.val-consmcp').text(sumInputs('.inpt-cmcp').formatMoney());
		$('.val-consmca').text(sumInputs('.inpt-cmca').formatMoney());
		$('.val-totalcons').text(consumo.formatMoney());
		$('.val-hmcp').text(hrMCP.formatMoney());
		$('.val-hmca').text(sumInputs('.inpt-hmca').formatMoney());
		$('.val-litroshora').text((consumo / hrMCP).formatMoney());
	}

	function validaTodosConsumos() {
		setTimeout(function () {
			$(allInput).each(function () {
				validaConsumo($(this), true);
				calculaLitroHoraDoInput($(this));
			});
			console.log('Consumo Validado!');
			setRealToMMSS();
		}, 3000);
	}

	function calculaLitroHoraDoInput(inpt) {
		var tr = $(inpt).parent().parent();
		var lhInput = tr.find('.inpt-lh');

		var vlrConsumoMCP = parseFloat(tr.find('.inpt-cmcp').val().replace(",", ".") || 0);
		var vlrConsumoMCA = parseFloat(tr.find('.inpt-cmca').val().replace(",", ".") || 0);
		var vlrHoraMCP = parseFloat(tr.find('.inpt-hmcp').val().replace(",", ".") || 0);

		var x = (vlrConsumoMCP + vlrConsumoMCA) / vlrHoraMCP;
		if (isNaN(x))
			x = "-"
		else
			x = parseFloat(Math.round(x * 100) / 100).toFixed(2);
		lhInput.val(x);
	}

	function _setTitleDocument() {
		document.title = (documentIsSaved ? "" : "* ") + "@" + loginUsuario + " # " + $('.rebocadorDrop span').text() + " " + idMes + "/" + idAno;

		if (!documentIsSaved) {
			window.onbeforeunload = function (e) {
				return 'Seu documento não foi salvo.';
			};
		} else {
			window.onbeforeunload = null;
		}
	}

	function _onClickMes() {
		var vidMes = $(this).attr('href').replace('#', '');
		var v = $(this).text();
		$('.mesDrop span').text(v);
		idMes = vidMes;

		_callHistorico();
	}

	function _onClickAno() {
		var vidAno = $(this).attr('href').replace('#', '');
		var v = $(this).text();
		$('.anoDrop span').text(v);
		idAno = vidAno;

		_callHistorico();
	}

	function login() {
		if (jQuery.browser.name != 'Chrome') {
			$('.erroLogin').text('Para mais segurança baixe o Google Chrome.');
			if ($('.erroLogin').find('button').length == 0) $('.erroLogin').append('<button class="btn btn-primary" onclick="window.location = \'https://www.google.com/intl/pt-BR/chrome/browser/\'">Fazer o download do Google Chrome</button>');
			$('.erroLogin').fadeIn();
			return;
		}
		var login = $('input#login').val();
		var senha = $('input#senha').val();


		$('.loadingZ').fadeIn();
		$.ajax(APP_CONFIG.URL + "LogarPRD&usuario=" + login + "&senha=" + senha, {
			success: function (data, b, e) {
				var s = $($(data)[0]);
				var x = $(s).find('prd');
				var t = x.find('retorno codigo').text();
				if (t == "0") {
					$('.blocker').fadeOut();
					idFilial = parseInt(x.find('ID_FILIAL').text());
					loginUsuario = x.find('LOGIN_USUARIO').text();
					$('.userName').text(loginUsuario);
					$('.userInfo').fadeIn();
					$('#login,#senha').attr('disabled', 'true');

					$('#formS').fadeOut();
					_setTitleDocument();
					getRebocadoresByFilial();
					getComandantesByFilial();
				} else {
					$('.erroLogin').fadeIn();
				}
				$('.loadingZ').fadeOut();
			}
		});
	}

	function _setRowsFromMounth() {
		$('.valores-table tr').not('.valores-table tr:first').remove();

		function zeroFill(v) {
			return ((v + "").length == 1 ? "0" : "") + v;
		}
		for (var i = 1; i < 32; i++) {
			var str = idMes + "/" + i + "/" + idAno;
			var date = new Date(str);
			var z = formatDate(date.toString(), date);

			var comandantesLista = '<li><a>?</a></li>';

			for (var k = 0; k < comandantes.length; k++)
				comandantesLista +=
				'<li><a>' + comandantes[k].nome + '</a></li>';

			if (parseFloat(z.match(/^..\/(..)/)[1]) != parseFloat(idMes)) break;
			var tr =
				'<tr class="rowValue">' +
				'<td><input type="text" class="input-small inpt-data" placeholder="Data" id="' + (zeroFill(i) + "-" + zeroFill(idMes) + "-" + idAno) + '" value="' + (zeroFill(i) + "/" + zeroFill(idMes) + "/" + idAno) + '" disabled="true"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-saldo" disabled="true"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-compra zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Compra"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-acd zz" data-toggle="tooltip" data-placement="bottom" title=""  data-original-title="Acerto/Perda/Descarte"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-recebido zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Tranferência Recebido"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-fornecido zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Tranferência Fornecido"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-cmcp zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Consumo MCP"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-cmca zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Consumo MCA"></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-hmcp zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Horas MCP"><input type="text" class="input-mini hrtype hmcp" value="..."/></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-hmca zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Horas MCA"><input type="text" class="input-mini hrtype hmca" value="..."/></td>' +
				'<td class="control-group"><input type="text" class="input-mini inpt-lh zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Litro/H"></td>' +


				/**
	  		 * 
Apoio Portuário
Viajando
Docando
Saída da Base
Teste
Teste de Máquina
Manutenção
	  		 */
				'<td><div class="btn-group">' +
				'<a class="btn btn-primary tipoConsumoDrop btn-warning"><span>Apoio Portuario</span></a>' +
				'<a class="btn btn-primary dropdown-toggle btn-warning" data-toggle="dropdown" href="#"><span class="caret"></span></a>' +
				'<ul class="dropdown-menu tipoconsumo-dropdown">' +
				'<li><a href="#Apoio Portuario">Apoio Portuario</a></li>' +
				'<li><a href="#Viajando">Viajando</a></li>' +
				'<li><a href="#Docando">Docando</a></li>' +
				'<li><a href="#Saida da Base">Saida da Base</a></li>' +
				'<li><a href="#Retorno a Base">Retorno a Base</a></li>' +
				'<li><a href="#Teste">Teste</a></li>' +
				'<li><a href="#Teste de Maquina">Teste de Maquina</a></li>' +
				'<li><a href="#Manutencao">Manutencao</a></li>' +
				'<li><a href="#Operacao Especial">Operacao Especial</a></li>' +
				'<li><a href="#Reboque">Reboque</a></li>' +
				'</ul>' +
				'</div></td>' +

				'<td><div class="btn-group">' +
				'<a class="btn btn-primary comandantesDrop btn-primary"><span>?</span></a>' +
				'<a class="btn btn-primary dropdown-toggle btn-primary" data-toggle="dropdown" href="#"><span class="caret"></span></a>' +
				'<ul class="dropdown-menu comandantes-dropdown">' +
				comandantesLista +
				'</ul>' +
				'</div></td>' +

				'<td><input type="text" class="input-mini inpt-saldofinal zz" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Saldo Final" disabled="true"></td>' +

				+'</tr>';

			$('.valores-table').append(tr);
		}
		$(allInput).keypress(function () {
			$('.inpt-saldo').each(function () {
				onKeyPressRow(null, $(this));
			});
			validaConsumo($(this));
			calculaLitroHoraDoInput($(this));
		});

		$('.hrtype').keyup(_keyDownHourInput);


		$('.tipoconsumo-dropdown li a').bind('click', _onClickTipoConsumo);

		$('.comandantes-dropdown li a').bind('click', _onClickComandantes);

		$(allInput).keyup(function () {
			var val = $(this).val();
			$(this).val(val.replace(/[A-Za-z\.:]/g, ""));
			if (val.indexOf(':') != -1) {
				alert("Por favor utilize ponto (,) ao invés de dois pontos (:), \n" +
					"Por ex: 1:30 -> 1,5");
			}
			sumAllInput();
		});
	};

	var v = null;
	var existeError = false;
	var dtaError = null;

	function emiteErrorDeCelula(input) {
		var td = input.parent('td');
		td.addClass('error');
		console.log(input);
		clearTimeout(v);
		/*v = setTimeout(function()
				{
					input.focus();
				},1000);*/
		existeError = true;
		dtaError = input.parent('td').parent('tr').find('.inpt-data').val();

		$('.item_fx').show();
		$('.item_fx').attr('dest', dtaError.replace(/\//g, "-"));
		TweenMax.to($('.item_fx'), 1, {
			opacity: 1
		});
	}

	function validaConsumo(input, fromAll) {
		var td = input.parent('td');
		var tr = td.parent('tr');
		var val = parseFloat((input.val() || "0").replace(',', '.') || 0);
		var aux = null;
		/*
		if(val < 0)
			emiteErrorDeCelula(input);
		*/
		if (input.hasClass('inpt-cmcp')) {
			var hmcp = tr.find('.inpt-hmcp');
			var v_hmcp = parseFloat((hmcp.val() || "0").replace(',', '.') || 0);
			aux = v_hmcp;
			if ((v_hmcp == 0 && val > 0)) {
				emiteErrorDeCelula(hmcp);
			} else if (v_hmcp > 0 && val == 0) {
				emiteErrorDeCelula(input);
			}
		} else if (input.hasClass('inpt-cmca')) {
			var hmca = tr.find('.inpt-hmca');
			var v_hmca = hmca.val();
			aux = parseFloat(v_hmca || 0);

			if ((v_hmca == 0 && val > 0)) {
				emiteErrorDeCelula(hmca);
			} else if (v_hmca > 0 && val == 0) {
				emiteErrorDeCelula(input);
			}
		} else if (input.hasClass('inpt-hmca')) {
			var cmca = tr.find('.inpt-cmca');
			var v_cmca = parseFloat(cmca.val() || 0);
			aux = v_cmca;

			if ((v_cmca == 0 && val > 0)) {
				emiteErrorDeCelula(cmca);
			} else if (v_cmca > 0 && val == 0) {
				emiteErrorDeCelula(input);
			}
		} else if (input.hasClass('inpt-hmcp')) {
			var cmcp = tr.find('.inpt-cmcp');
			var v_cmcp = parseFloat(cmcp.val() || 0);
			aux = v_cmcp;

			if ((v_cmcp == 0 && val > 0)) {
				emiteErrorDeCelula(cmcp);
			} else if (v_cmcp > 0 && val == 0) {
				emiteErrorDeCelula(input);
			}
		}
		if (((val > 0 && aux > 0) || (!val && !aux)) && !fromAll) {
			td.removeClass('error');
			existeError = false;
			dtaError = null;
			TweenMax.to($('.item_fx'), 1, {
				opacity: 0,
				onComplete: function () {
					$('.item_fx').hide();
				}
			});
		}
		return;
	}

	function onKeyPressRow(e, obj) {
		documentIsSaved = false;
		var inp = obj || $(this);
		var val = inp.val() + "";
		var parentTr = inp.parent('td').parent('tr');
		var nextTr = inp.parent('td').parent('tr').next('tr');

		var inptSaldoFinal = parentTr.find('.inpt-saldofinal');

		var saldo = parseFloat(parentTr.find('.inpt-saldo').val().replace(',', '.')) || 0;
		var compra = parseFloat(parentTr.find('.inpt-compra').val().replace(',', '.')) || 0;
		var acd = parseFloat(parentTr.find('.inpt-acd').val().replace(',', '.')) || 0;
		var recebido = parseFloat(parentTr.find('.inpt-recebido').val().replace(',', '.')) || 0;
		var fornecido = parseFloat(parentTr.find('.inpt-fornecido').val().replace(',', '.')) || 0;
		var cmcp = parseFloat(parentTr.find('.inpt-cmcp').val().replace(',', '.')) || 0;
		var cmca = parseFloat(parentTr.find('.inpt-cmca').val().replace(',', '.')) || 0;
		//var hmcp = parseFloat(parentTr.find('.inpt-hmcp').val()) || 0;
		//var hmca = parseFloat(parentTr.find('.inpt-hmca').val()) || 0;

		var saldoFinal = saldo + compra + acd + recebido - fornecido - cmcp - cmca;
		if (saldoFinal < 0) saldoFinal = 0;
		inptSaldoFinal.val(saldoFinal);

		nextTr.find('.inpt-saldo').val(saldoFinal);
		_setTitleDocument();
	}

	function formatDate(str, d) {
		var z = str.split(" ");
		var mes = (d.getMonth() + 1) + "";
		var dia = z[2];
		var ano = z[3];

		if (mes.length == 1) {
			mes = "0" + mes;
		}

		return dia + "/" + mes + "/" + ano;
	}
	/*
	 * com_getHistoricoRebocadoresByFilialAndRebocador&idFilial=1&idRebocador=37
	 */
	function getHistorico(limited) {

		$('.blocker').fadeIn();
		$('.loadingZ').fadeIn();
		$('.salvarRegistros').hide();

		_getIt("com_getHistoricoRebocadoresByFilialAndRebocador&idFilial=" + (limited ? '' : idFilial) + "&idRebocador=" + idRebocador + '&idMes=' + (limited ? (parseFloat(idMes) - 1 == 0 ? 12 : parseFloat(idMes) - 1) : idMes) + "&idAno=" + (parseFloat(idMes) - 1 == 0 && limited ? parseFloat(idAno) - 1 : idAno) + (limited ? '&limited=true' : ''), 'hHTML', function () {
			_setRowsFromMounth();
			_loadRowsFromDB(limited);
			$('.zz').tooltip();
			sumAllInput();
			$('.blocker').fadeOut();
			$('.loadingZ').fadeOut();
			$('.salvarRegistros').fadeIn();
		});
	}

	function buildJson() {
		if (existeError) {
			alert("Por favor corrija os erros de lançamento de " + dtaError);
			return;
		}
		var rows = $('.rowValue');
		var jsonObject = [];


		var urlSTR = APP_CONFIG.URL + "insertRegistros&show_error&idFilial=" +
			idFilial + '&idRebocador=' +
			idRebocador + '&idAno=' + idAno + '&idMes=' + (idMes + "".length == 1 ? "0" + idMes : idMes);

		rows.each(function () {
			jsonObject.push({
					data: $(this).find('.inpt-data').val(),
					saldo: $(this).find('.inpt-saldo').val(),
					compra: $(this).find('.inpt-compra').val(),
					acerto_perda_descarte: $(this).find('.inpt-acd').val(),
					trans_receb: $(this).find('.inpt-recebido').val(),
					trans_fornec: $(this).find('.inpt-fornecido').val(),
					cons_mcp: $(this).find('.inpt-cmcp').val(),
					cons_mca: $(this).find('.inpt-cmca').val(),
					tp_consumo: $(this).find('.tipoConsumoDrop span').text(),
					comandante: $(this).find('.comandantesDrop span').text(),
					hr_mcp: $(this).find('.inpt-hmcp').val(),
					hr_mca: $(this).find('.inpt-hmca').val(),
					saldo_final: $(this).find('.inpt-saldofinal').val()
				} //TODO
			);
		});
		console.log(jsonObject)
		var jsonString = JSON.stringify(jsonObject);

		$('.loadingZ').fadeIn();
		$('.blocker').fadeIn();
		$('.salvarRegistros').fadeOut();

		if (enableSave) {
			$.ajax(urlSTR, {
				type: "POST",
				data: {
					json: jsonString
				},
				success: function () {

					$('.salvarRegistros').fadeIn();
					$('.blocker').fadeOut();
					documentIsSaved = true;
					_setTitleDocument();
					$('.loadingZ').fadeOut();
					enableSave = true;
					setTimeout(_callHistorico, 1000);
				}
			});
		}
		enableSave = false

	}
	var enableSave = true;

	function _loadRowsFromDB(l) {
		var rows = $(hHTML).find('row');
		var codigo = $(hHTML).find('codigo').text();
		var saldo = $(rows[i]).find('SALDO').html();
		var removeu = false;

		if ((codigo == "-1" && !l) || (saldo == "0" || saldo == "")) {
			getHistorico(true);
			return;
		}

		for (var i = 0; i < rows.length; i++) { //CONS_MCP

			if (rows.length == 2 && i == 0) {
				$(rows[0]).find('data').html("<!--[CDATA[01/" + ("" + idMes.length == 1 ? "0" + idMes : idMes) + "/" + idAno + "]]-->");
			}

			var tr = getTRFromThisRow($(rows[i]), codigo).parent('td').parent('tr');
			//var DATA = $(rows[i]).find('DATA').html();
			var SALDO = $(rows[i]).find('SALDO').html();
			var COMPRA = $(rows[i]).find('COMPRA').html();
			var ACERTO_PERDA_DESCARTE = $(rows[i]).find('ACERTO_PERDA_DESCARTE').html();
			var TRANS_RECEB = $(rows[i]).find('TRANS_RECEB').html();
			var TRANS_FORNEC = $(rows[i]).find('TRANS_FORNEC').html();
			var CONS_MCP = $(rows[i]).find('CONS_MCP').html();
			var CONS_MCA = $(rows[i]).find('CONS_MCA').html();
			var HR_MCP = $(rows[i]).find('HR_MCP').html();
			var HR_MCA = $(rows[i]).find('HR_MCA').html();
			var SALDO_FINAL = $(rows[i]).find('SALDO_FINAL').html();
			var TP_CONSUMO = $(rows[i]).find('TP_CONSUMO').html();
			var COMANDANTE = $(rows[i]).find('COMANDANTE').html();

			if (_getFromCDATA(SALDO, null, true))
				tr.find('.inpt-saldo').val(_getFromCDATA(SALDO));



			if (_getFromCDATA(COMPRA, null, true))
				tr.find('.inpt-compra').val(_getFromCDATA(COMPRA));
			if (_getFromCDATA(ACERTO_PERDA_DESCARTE, null, true))
				tr.find('.inpt-acd').val(_getFromCDATA(ACERTO_PERDA_DESCARTE));
			if (_getFromCDATA(TRANS_RECEB, null, true))
				tr.find('.inpt-recebido').val(_getFromCDATA(TRANS_RECEB));
			if (_getFromCDATA(TRANS_FORNEC, null, true))
				tr.find('.inpt-fornecido').val(_getFromCDATA(TRANS_FORNEC));
			if (_getFromCDATA(CONS_MCP, null, true))
				tr.find('.inpt-cmcp').val(_getFromCDATA(CONS_MCP));
			if (_getFromCDATA(CONS_MCA, null, true))
				tr.find('.inpt-cmca').val(_getFromCDATA(CONS_MCA));
			if (_getFromCDATA(HR_MCP, null, true))
				tr.find('.inpt-hmcp').val(_getFromCDATA(HR_MCP));
			if (_getFromCDATA(HR_MCA, null, true))
				tr.find('.inpt-hmca').val(_getFromCDATA(HR_MCA));
			if (_getFromCDATA(SALDO_FINAL, null, true))
				tr.find('.inpt-saldofinal').val(_getFromCDATA(SALDO_FINAL));


			if (COMANDANTE == "<!--[CDATA[]]-->" || COMANDANTE == "") {
				COMANDANTE = "?";
			} else {
				COMANDANTE = _getFromCDATA(COMANDANTE);
			}

			if (TP_CONSUMO == "<!--[CDATA[]]-->" || TP_CONSUMO == "") {
				TP_CONSUMO = "Apoio Portuario";
			} else {
				TP_CONSUMO = _getFromCDATA(TP_CONSUMO);
			}
			if (TP_CONSUMO == "Apoio PortuÃ¡rio") {
				TP_CONSUMO = "Apoio Portuario";
			}
			tr.find('.tipoConsumoDrop span').text(TP_CONSUMO);
			tr.find('.comandantesDrop span').text(COMANDANTE);
			/**
				segunda tem que estar aberto sexta , sabado e domingo
			*/
			/*
			var inptData = tr.find('.inpt-data');
			var v = inptData.val();
			var dia = v ? v.match(/(\d{2})\/\d{2}\/\d{4}/) : 1;
			var dataAtual = new Date();
			var dataDaLinha = new Date();
			
			dataDaLinha.setMonth(idMes-1);
			dataDaLinha.setYear(idAno);
			dataDaLinha.setDate(parseInt(dia));
			dataAtual.setDate(dataAtual.getDate()-1);
			*/
			var inptData = tr.find('.inpt-data');
			var v = inptData.val();
			var dia = v ? v.match(/(\d{2})\/\d{2}\/\d{4}/) : 1;
			var dataAtual = new Date();
			var dataDaLinha = parseInt(dia);
			var _dataAtual = dataAtual;
			dataAtual = dataAtual.getDate() - 1;

			/*
			5	PNG
			6	SDR
			7	MAC
			8	RGD
			9	SEP
			1	VIT
			2	RJ1
			3	STR
			22	GUA
			99	TMD
			*/
			if (idFilial == 77 || (((dataDaLinha < dataAtual) || (dataDaLinha > dataAtual)) || parseInt(idMes) != _dataAtual.getMonth() + 1)) {
				if (tr.find(allInput).attr('prevent-disabled') === undefined) {
					var dataDaLinhaDT = new Date(idAno + "-" + idMes + "-" + dataDaLinha);
					/** 
				o primeiro que vale de fato 16/09 
				**/
					var exceptions = {
						//'1':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},//VIT
						//'2':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},//rj1
						'3': {
							dtInicio: new Date("2015-06-15"),
							dtFim: new Date("2015-06-17")
						}, //STR
						//'4':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},
						//'5':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},//PNG
						//'6':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},//SDR
						//'7':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},//MAC
						//'8':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},//RGD
						//'9':{dtInicio:new Date("2015-06-01"),dtFim:new Date("2015-06-31")},//SEP
					};

					if (!(exceptions[idFilial] && (dataDaLinhaDT >= exceptions[idFilial].dtInicio && dataDaLinhaDT <= exceptions[idFilial].dtFim))) {
						tr.find('input').attr('disabled', true);
						if (!removeu)
							$('.salvarRegistros button').attr('disabled', true);
					} else {
						removeu = true;
					}
				}
			} else {

				var dataAtual = new Date();
				dataAtual.setDate(dataAtual.getDate() - 1);
				var diaSemana = dataAtual.getDay();

				if (diaSemana == 0) {
					function getTR(r) {
						return getTRFromThisRow($(r), codigo).parent('td').parent('tr').find(allInput);
					}

					var sexta = getTR(rows[i + 1]);
					var sabado = getTR(rows[i + 2]);
					var domingo = getTR(rows[i + 3]);
					var segunda = getTR(rows[i + 4]);

					sexta.attr('prevent-disabled', '');
					sabado.attr('prevent-disabled', '');
					//domingo.attr("prevent-disabled",'');
					//segunda.attr('prevent-disabled','');

					removeu = true;
				}
				tr.addClass("success");
			}

		}

		if (idMes >= 7) {
			$('input').attr('disabled', '');
		} else
			$('.salvarRegistros button').removeAttr('disabled');

	}

	function getTRFromThisRow(r, codigo, safe) {
		var z = $(r).find('DATA').html();
		if (!(r && z)) return $();
		var o = $('#' + _getFromCDATA(z.replace(/\//g, "-"))) || $();
		return o.length == 0 && safe ? null : o;
	}
	/*
	 * com_rebocadoresByFilial&idFilial=1
	 */
	function getRebocadoresByFilial() {
		_getIt("com_rebocadoresByFilial&idFilial=" + idFilial,
			'rHTML',
			function () {
				_setRebInDropDown();
			});
	}

	function _setRebInDropDown() {
		var ul = $('.rebocador-dropdown');
		var rows = $(rHTML).find('row');
		rebocadoresRow = rows;
		for (var i = 0; i < rows.length; i++) {
			var d = _getFromCDATA($(rows[i]).find('DESCRICAO').html());
			var z = _getFromCDATA($(rows[i]).find('ID_REBOCADOR').html());
			ul.append('<li><a href="#' + z + '">' + d + '</a></li>');
		}
		$('.rebocador-dropdown li a').bind('click', _onClickRebocador);
	}


	function getComandantesByFilial() {
		_getIt("com_comandantesByFilial&idFilial=" + loginUsuario,
			'comandantesHTML',
			function () {
				_setComandantesInScope();
			});
		_getIt("com_maritimosByFilial&idFilial=" + loginUsuario,
			'maritimosHTML',
			function () {
				_setMaritimosInScope();
			});
	}

	function _setMaritimosInScope() {
		maritimos = [];
		var rows = $(maritimosHTML).find('row');
		for (var i = 0; i < rows.length; i++) {
			var a = _getFromCDATA($(rows[i]).find('nome').html());
			var b = _getFromCDATA($(rows[i]).find('funcao').html());
			var c = _getFromCDATA($(rows[i]).find('categoria').html());
			var g = _getFromCDATA($(rows[i]).find('situacao').html());
			var h = _getFromCDATA($(rows[i]).find('matricula').html());
			maritimos.push({
				nome: a,
				funcao: b,
				categoria: c,
				situacao: g,
				matricula: h
			});
		}

		var tableMaritimos = $('.maritimos-table');
		tableMaritimos.empty()

		for (var i = 0; i < maritimos.length - 1; i++) {
			var m = maritimos[i];
			var tr = "<tr>";
			var btnType = (m.situacao === 'A' ? 'btn-danger' : 'btn-success')
			tr += "<td>" + m.nome + "</td>";
			tr += "<td>" + m.funcao + "</td>";

			tr += "<td>" +
				'<button onclick="tableController.swapStatusMaritimo(' + i + ')" class="btn ' + btnType + '">' +
				(m.situacao === 'A' ? 'Esperar!' : 'Comandar!')
			'</button>' + "</td>";



			tr += "</tr>";
			tableMaritimos.append(tr);
		}


	}

	function swapStatusMaritimo(i) {
		var matricula = maritimos[i].matricula;
		var situacao = maritimos[i].situacao == "A" ? "W" : "A";
		_getIt("com_swapStatusMaritmo&situacao=" + situacao + "&matricula=" + matricula,
			'none',
			function () {
				getComandantesByFilial();
				buildJson();
			});
	}

	function _setComandantesInScope() {
		comandantes = [];
		var rows = $(comandantesHTML).find('row');
		for (var i = 0; i < rows.length; i++) {
			var a = _getFromCDATA($(rows[i]).find('nome').html());
			var b = _getFromCDATA($(rows[i]).find('funcao').html());
			var c = _getFromCDATA($(rows[i]).find('categoria').html());
			var g = _getFromCDATA($(rows[i]).find('situacao').html());

			comandantes.push({
				nome: a,
				funcao: b,
				categoria: c,
				situacao: g
			})
		}
	}


	function _getIt(s, o, onComplete) {
		$.ajax(APP_CONFIG.URL + s + "&_" + (Math.random() + "e" + Math.random()), {
			success: function (a, b, c) {
				var resp = $(c.responseText);
				var rebcHTML = $($(resp));
				eval(o + ' = rebcHTML');
				onComplete();
			}
		});
	}

	function _getFromCDATA(s) {
		if (s == undefined) return '';
		var v = s.match(/<!--\[CDATA\[(.+)\]\]-->/);
		return v ? v[1] : "";
	}

	function sumInputs(selector) {
		var s = 0;
		$(selector).each(function () {
			s += parseFloat($(this).val().replace(",", ".") || 0);
		});
		return s;
	};
	_init();
	$(document).ready(function () {
		$('.item_fx').mouseenter(function () {
			var b = 0x9e0101;

			TweenMax.to(this, .3, {
				css: {
					backgroundColor: 0xb40101,
					borderBottomColor: b,
					borderLeftColor: b,
					borderRightColor: b,
					borderTopColor: b
				}
			});
		});
		$('.item_fx').mouseleave(function () {
			var b = 0xCF0000;
			var bt = 0xD61616;
			TweenMax.to(this, 1, {
				css: {
					backgroundColor: b,
					borderBottomColor: b,
					borderLeftColor: b,
					borderRightColor: b,
					borderTopColor: bt
				}
			});
		});
		$('.item_fx').click(function () {
			var t = $(this);
			if (t.hasClass('item_fx')) {
				TweenMax.to(t, .3, {
					scale: 1.1,
					onComplete: function () {
						TweenMax.to(t, .3, {
							scale: 1,
							onComplete: function () {
								scroller(t.attr('dest'));
							}
						});
					},
					ease: Bounce.easeIn
				});
			}

		});
	});

	function scroller(id) {
		var scrollYPos = $("#" + id).offset().top;
		//event.preventDefault();
		TweenLite.to(window, 2, {
			scrollTo: {
				y: scrollYPos - 50,
				x: 0
			},
			ease: Power4.easeOut
		});
	}

	function checkIfFirstFithBusinessDay(day, month, year) {
		// check that it is a valid date
		if (!isNaN(day) && !isNaN(month) && !isNaN(year) && month > 0 && month < 13 && day > 0 && day < 32) {

			var d = new Date(year, month - 1, day);

			// If the day does not much, that means that the original date was incorrect (e.g.: Feb 30)
			if (d.getDate() != day) {
				return false;
			}

			// month - 1, because months in JavaScript go from 0 (January) to 11 (December)
			var auxDate = new Date(year, month - 1, 1);
			var first, fifth;

			switch (d.getDay()) {
			case 0: // the 1st of the month is Sunday --> first business day is the 2nd (Monday)
				first = 2;
				fifth = 6;
				break;
			case 6: // the 1st of the month is Saturday --> first business day is the 3rd (Monday)
				first = 3;
				fifth = 7;
				break;
			default:
				first = 1;
				fifth = 5;
				if ((d.getDay() + fifth + 6) % 7 == 0) {
					fifth = 6;
				} // sunday
				else if ((d.getDay() + fifth + 6) % 7 == 6) {
					fifth = 7;
				} // saturday
				break;
			}

			var firstBusinessDay = new Date(year, month - 1, first);
			var fifthBusinessDay = new Date(year, month - 1, fifth);

			return (d.getTime() === firstBusinessDay.getTime() || d.getTime() === fifthBusinessDay.getTime());

		} else {

			return false;

		}

	}
	var blockIsFirstDay = function () {
		var dtAtual = new Date();
		if (
			checkIfFirstFithBusinessDay(dtAtual.getDate(), dtAtual.getMonth() + 1, dtAtual.getYear()) && idMes < (dtAtual.getMonth() + 1) && dtAtual.getHours() >= 11
		) {
			var enableds = $('input:enabled');
			if (enableds.length > 0) {
				enableds.attr('disabled', '');
				alert('Lançamentos bloqueados!');
			}
		}
	};
	//setInterval(blockIsFirstDay,300);
	return {
		swapStatusMaritimo: swapStatusMaritimo,
		getComandantesByFilial: getComandantesByFilial
	}
};
Number.prototype.formatMoney = function (c, d, t) {
	var n = this,
		c = isNaN(c = Math.abs(c)) ? 2 : c,
		d = d == undefined ? "," : d,
		t = t == undefined ? "." : t,
		s = n < 0 ? "-" : "",
		i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "",
		j = (j = i.length) > 3 ? j % 3 : 0;
	return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};
//var txt = '' + 'navigator.appName = ' + navigator.appName + '<br>' + 'navigator.userAgent = ' + navigator.userAgent + '<br><br><br>' + 'jQuery.browser.name  = ' + jQuery.browser.name + '<br>' + 'jQuery.browser.fullVersion  = ' + jQuery.browser.fullVersion + '<br>' + 'jQuery.browser.version = ' + jQuery.browser.version + '<br>' + 'jQuery.browser.majorVersion = ' + jQuery.browser.majorVersion + '<br><br><br>' + 'jQuery.browser.msie = ' + jQuery.browser.msie + '<br>' + 'jQuery.browser.mozilla = ' + jQuery.browser.mozilla + '<br>' + 'jQuery.browser.opera = ' + jQuery.browser.opera + '<br>' + 'jQuery.browser.webkit = ' + jQuery.browser.webkit + '<br>';