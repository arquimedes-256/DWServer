package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import bus.email.SendMailTLS;


import factory.QueryFactory;

import model.Options;
import model.Row;

public class SIGDAO {

	public ArrayList<Row> orcadorealizado(Options options) throws SQLException
	{
		ArrayList<Row> rows = new ArrayList<Row>();
		PreparedStatement ps = QueryFactory.getAll(options);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			Row row = new Row();
			row.setNomeGrupoMaster(rs.getString("NAME_GRUPOMASTER"));
			if(!options.isConsolidado)
				row.setFilial(rs.getString("SIGLA_FILIAL"));
			row.setIdGrupoMaster(String.valueOf(rs.getInt("id_grupomaster")));
			row.setVlrOrcado(rs.getDouble("ORCADO"));
			row.setVlrRealizado(rs.getDouble("VLR_REALIZADO"));
			rows.add(row);
			if(options.isEvolucao)
				row.setMes(rs.getInt("MES"));
		}
		return rows;
	}
	public ArrayList<Row> evolucao_grafico(Options options) throws SQLException
	{
		ArrayList<Row> rows = new ArrayList<Row>();
		PreparedStatement ps = QueryFactory.getEvolucaoGrafico(options);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			Row row = new Row();
			row.setMes(rs.getInt("ID_MES"));
			row.setIdGrupoMaster(rs.getString("ID_GRUPOMASTER"));
			row.setNomeGrupoMaster(rs.getString("NAME_GRUPOMASTER"));
			row.setVlrOrcado(rs.getDouble("ORCADO"));
			row.setVlrRealizado(rs.getDouble("VLR_REALIZADO"));
			rows.add(row);
		}
		return rows;
	}
	public ArrayList<Row> orcadorealizado_detalhado(Options options) throws SQLException
	{
		ArrayList<Row> rows = new ArrayList<Row>();
		PreparedStatement ps = QueryFactory.getDetalhado(options);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			Row row = new Row();
			row.setNomeGrupoMaster(rs.getString("NAME_GRUPOMASTER"));
			if(options.isKeepContaContabil)
			{
				row.setIdContaContabil(rs.getString("id_conta_contabil"));
				row.setNomeContaContabil(rs.getString("nome_conta_contabil"));
			}
			
			row.setVlrOrcado(rs.getDouble("ORCADO"));
			if(existColumn("sigla_filial",rs))
			{
				row.setFilial(rs.getString("sigla_filial"));
			}
			if(!(options.filial != null && options.filial.equals("LISTAR") && 
					(options.localOptions.isDGATotal || options.localOptions.isTotalPessoal)
					))
			{
				row.setIdGrupo(rs.getString("id_grupo"));
				row.setNomeGrupo(rs.getString("NOME_GRUPO"));
			}
			row.setIdGrupoMaster(String.valueOf(rs.getInt("id_grupomaster")));
			row.setVlrRealizado(rs.getDouble("VLR_REALIZADO"));
			rows.add(row);
		}
		return rows;
	}
	public ArrayList<Row> orcadorealizado_detalhado_intern(Options options) throws SQLException
	{
		ArrayList<Row> rows = this.orcadorealizado_detalhado(options);
		return rows;
	}
	public ArrayList<Row> disponibilidade_contabil(Options options) throws SQLException
	{
		ArrayList<Row> rows = new ArrayList<Row>();
		PreparedStatement ps = QueryFactory.getDisponibilidade(
				options.mesFinal, 
				options.ano);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			Row r = new Row();
			r.setDataFechamento(rs.getString("LIB_DATA"));
			rows.add(r);
		}
		return rows;
	}
	public ArrayList<Row> send_email(Options options) throws SQLException
	{
		SendMailTLS.sendEmail();
		return null;
	}
	public ArrayList<Row> centro_de_custo_mtz(Options options) throws SQLException
	{
		ArrayList<Row> rows = new ArrayList<>();
		PreparedStatement ps = QueryFactory.getPorCentroDeCusto(options);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			Row r = new Row();
			r.setFilial(rs.getString("FILIAL_ALOCACAO"));
			r.setCentroDeCusto(rs.getString("NOME_CENTRO_CUSTO"));
			r.setVlrRealizado(rs.getDouble("REALIZADO"));
			rows.add(r);
		}
		return rows;
	}
	private boolean existColumn(String col,ResultSet rs) throws SQLException
	{
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();

		for (int i = 1; i < numberOfColumns + 1; i++) 
		{
		    String columnName = rsMetaData.getColumnName(i);
		    if (col.equalsIgnoreCase(columnName)) {
		        return true;
		    }
		}
		return false;
	}
}
