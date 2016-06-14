
RegistroCombustivel = function()
{
	this.idMes;
	this.idDia;
	this.idAno;
	this.vlrSaldo;
	this.vlrCompra;
	this.nrNotaFiscal;
	
	this.acertoPerdaDesgaste;
	
	this.transferencia = {};
	this.transferencia.recebido;
	this.transferencia.fornecido;
	
	this.consumo = {};
	this.consumo.MCP;
	this.consumo.MCA;
	
	this.horas = {};
	this.horas.MCP;
	this.horas.MCA;
	this.vlrSaldoAtual;
};