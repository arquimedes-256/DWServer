package model;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Row 
{
	public String getNomeGrupoMaster() {
		return nomeGrupoMaster;
	}
	public void setNomeGrupoMaster(String nomeGrupoMaster) {
		this.nomeGrupoMaster = nomeGrupoMaster;
	}
	public String getNomeGrupo() {
		return nomeGrupo;
	}
	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}
	public String getFilial() {
		return filial;
	}
	public void setFilial(String filial) {
		this.filial = filial;
	}
	public double getVlrRealizado() {
		return vlrRealizado;
	}
	public void setVlrRealizado(double vlrRealizado) {
		this.vlrRealizado = vlrRealizado;
	}
	public double getVlrOrcado() {
		return vlrOrcado;
	}
	public String getIdContaContabil() {
		return idContaContabil;
	}
	public void setIdContaContabil(String idContaContabil) {
		this.idContaContabil = idContaContabil;
	}
	public void setVlrOrcado(double vlrOrcado) {
		this.vlrOrcado = vlrOrcado;
	}
	public double getDiferenca() {
		return diferenca;
	}
	public String getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	public void setDiferenca(double diferenca) {
		this.diferenca = diferenca;
	}
	private String idGrupoMaster;
	public String getIdGrupoMaster() {
		return idGrupoMaster;
	}
	public void setIdGrupoMaster(String idGrupoMaster) {
		this.idGrupoMaster = idGrupoMaster;
	}
	private String nomeGrupoMaster;
	private String nomeGrupo;
	private String filial;
	
	private String centroDeCusto;
	
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	private int mes;
	private double vlrRealizado;

	private double vlrOrcado;
	private double diferenca;
	public String getNomeContaContabil() {
		return nomeContaContabil;
	}
	public void setNomeContaContabil(String nomeContaContabil) {
		this.nomeContaContabil = nomeContaContabil;
	}
	public String getDataFechamento() {
		return dataFechamento;
	}
	public void setDataFechamento(String dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
	private String dataFechamento;
	
	private String idContaContabil;
	private String nomeContaContabil;
	private String idGrupo;
	
	@JsonIgnore
	public Row getClone() 
	{
		Row clone = new Row();
		clone.dataFechamento = this.dataFechamento  ;
		clone.diferenca = this.diferenca; 
		clone.filial = this.filial ;
		clone.idContaContabil = this.idContaContabil ;
		clone.idGrupo = this.idGrupo ;
		clone.idGrupoMaster = this.idGrupoMaster ;
		clone.mes = this.mes ;
		clone.nomeContaContabil = this.nomeContaContabil ;
		clone.nomeGrupo = this.nomeGrupo ;
		clone.nomeGrupoMaster = this.nomeGrupoMaster ;
		clone.vlrOrcado = this.vlrOrcado ;
		clone.vlrRealizado = this.vlrRealizado ;
		
		return clone;
		
	}
	public String getCentroDeCusto() {
		return centroDeCusto;
	}
	public void setCentroDeCusto(String centroDeCusto) {
		this.centroDeCusto = centroDeCusto;
	};
	
}
