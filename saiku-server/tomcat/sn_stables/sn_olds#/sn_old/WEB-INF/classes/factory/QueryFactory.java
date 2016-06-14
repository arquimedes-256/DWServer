package factory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import factory.string.DetalhadoFactory;

import model.Options;


public class QueryFactory {
	
	static
	{
		System.out.println("QueryFactory compilado!");
	}
	public static PreparedStatement getAll(Options options)
	{
		String sql = "SELECT * FROM \n" +
				"(\n" +
				DetalhadoFactory.getDespExtraordinarias(options) +			
			"UNION ALL \n" +		
				DetalhadoFactory.getMainQuery(options)+
			"UNION ALL\n" +
				
				"	SELECT\n" +
				(options.isEvolucao ?"		ID_MES,\n":"") +
				"		(id_ultragrupo + 1) * 9494 id_grupomaster,\n" +
				"		nome_ultragrupo name_grupomaster,\n" +
				"		sigla_filial,\n" +
				"		ordering,\n" +
				"		ORCADO,\n" +
				"		VLR_REALIZADO,\n" +
				"		DIFERENCA\n" +
				"	FROM\n" +
				"		(\n" +
				"select " +
				(options.isEvolucao ?"				\n" +
				"				ID_MES,\n":"") +
				"				id_ultragrupo,\n" +
				"				nome_ultragrupo,\n" +
				"				sigla_filial,\n" +
				"				ordering,\n" +
				"				SUM (ORCADO) ORCADO,\n" +
				"				SUM (VLR_REALIZADO) VLR_REALIZADO,\n" +
				"				SUM ( DIFERENCA) DIFERENCA\n" +
				" from(" +
					DetalhadoFactory.getInnerBlock(options)+
				")" +
				"			where ID_ANO = ? AND ID_MES BETWEEN  ? AND ?\n" +
				"			GROUP BY\n" +
				(options.isEvolucao ?"				id_mes,\n":"") +
				"				id_ultragrupo,\n" +
				"				nome_ultragrupo,\n" +
				"				sigla_filial,\n" +
				"				ordering\n" +
				"			ORDER BY\n" +
				"				id_ultragrupo,\n" +
				"				nome_ultragrupo,\n" +
				"				ordering\n" +
				"		)\n" +
				"	ORDER BY\n" +
				(options.isEvolucao ?"		1,\n":"") +
				"		id_grupomaster,\n" +
				"		ordering\n" +
				") t "+	
				(options.filial != null ?" where t.sigla_filial = ?" : "");
		
		System.out.println(sql+"\n===========");
		PreparedStatement ps = null;
		try 
		{
			ps = OracleFactory.getConnection().prepareStatement(sql);
		
			ps.setInt(1, Integer.valueOf(options.ano));
			ps.setInt(2, Integer.valueOf(options.mesInicial));
			ps.setInt(3, Integer.valueOf(options.mesFinal));
			
			ps.setInt(4, Integer.valueOf(options.ano));
			ps.setInt(5, Integer.valueOf(options.mesInicial));
			ps.setInt(6, Integer.valueOf(options.mesFinal));
			
			ps.setInt(7, Integer.valueOf(options.ano));
			ps.setInt(8, Integer.valueOf(options.mesInicial));
			ps.setInt(9, Integer.valueOf(options.mesFinal));
			if(options.filial != null)
				ps.setString(10, String.valueOf(options.filial));
				

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			OracleFactory.createNewConnection();
			QueryFactory.getAll(options);
		}
		return ps;
	}
	public static PreparedStatement getDisponibilidade(String mesFinal,String ano)
	{
		
		String sql = "SELECT * FROM LIBERACAO_LIB\n" +
				" WHERE LIB_CDEMPRESA = '004'\n" +
				"   AND TO_DATE (LIB_DATA,'DD/MM/RR') = TO_DATE('30/"+mesFinal+"/"+ano+"', 'DD/MM/RR')\n" +
				"   AND LIB_CDSISTEMA = 'SCP'";
		System.out.println(sql);
		PreparedStatement ps = null;
		try 
		{ 
			ps = OracleFactory.getConnection().prepareStatement(sql);
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			OracleFactory.createNewConnection();
			QueryFactory.getDisponibilidade(mesFinal, ano);
		}
		
		return ps;
	}
	public static PreparedStatement getDetalhado(Options options)
	{
		System.out.println("=== INICIO DETALHADO ==");
		String groupIdAux = null;
		
		boolean isTotalPessoal = false;
		boolean isDGATotal = false;
		String locFilial = null;
		
		if(options.idGrupo != null && options.idGrupo.equals("99"))
		{
			options.idGrupo = null;
			groupIdAux = "in(1,2,3,81,82)";
			isTotalPessoal = true;
			options.idGrupoMaster = null;
		}
		else if(options.idGrupo != null && options.idGrupo.equals("98"))
		{
			options.idGrupo = null;
			groupIdAux = "in(17,83)";
			isDGATotal = true;
			options.idGrupoMaster = null;
		}
		
		if(options.isConsolidado && options.idGrupo != null && options.idGrupoMaster != null &&
				(options.idGrupoMaster.equals("13") ||options.idGrupoMaster.equals("3")))
		{
			if(options.idGrupo.equals("3"))
			{
				groupIdAux = "= 81";
			}
			if(options.idGrupo.equals("2"))
			{
				groupIdAux = "= 82";
			}
			if(options.idGrupo.equals("17"))
			{
				groupIdAux = "= 83";
				isDGATotal = true;
			}
			
			options.idGrupoMaster = null;
		}
		if(options.filial != null && options.filial.equals("CONSOLIDE") &&
				options.isConsolidado && 
				options.idGrupo == null && 
				options.idGrupoMaster != null)
		{
			
			if(options.idGrupoMaster.equals("3"))
				groupIdAux = "in(1,2,3,81,82)";
			else if(options.idGrupoMaster.equals("13"))
				groupIdAux = "in(17,83)";
				
			options.idGrupoMaster = null;
		}
		
		if(options.filial != null && options.filial.contains("+") )
		{
			String[] filiais = options.filial.split("\\+");
			locFilial = "'"+filiais[0].trim()+"','"+filiais[1].trim()+"'";
		}
		
		String sql = "SELECT\n" +
				(options.filial != null && options.filial.equals("LISTAR") 
				&& 
				options.isConsolidado ? 
						" sigla_filial, \n" : 
					"") +
				" id_grupomaster,\n" +
				" name_grupomaster,\n" +
				" id_grupo,\n" +
				" NOME_GRUPO,\n" +
				(options.isKeepContaContabil ? 
							((isTotalPessoal||isDGATotal) ?
										"REGEXP_REPLACE(id_conta_contabil, '^\\d+', "+
									(isDGATotal ? "'(Dga.MTZ+Dga.Fls) '":"'(Emb+Man+Adm) '")+"||nome_conta_contabil) id_conta_contabil,\n" 
										:" id_conta_contabil,\n")
							+
								
							" nome_conta_contabil,\n" :""
				)+
				
				" SUM (case when nome_grupo like '(_)%' " +
					"then VLR_ORCADO  else VLR_ORCADO end)*-1 " +
					"ORCADO,\n" +
				" SUM (case when nome_grupo like '(_)%' then VLR_REALIZADO*-1  else vlr_realizado end) VLR_REALIZADO \n"+
				"FROM\n" +
				"	(\n" +
				"		SELECT\n " +
				(options.filial != null && options.filial.equals("LISTAR") 
				&& 
				options.isConsolidado ? 
						"		case when filial.sigla_filial = 'TMD' then 'SDR' else sigla_filial end sigla_filial, \n" : 
						"		filial.sigla_filial , \n") 
				+
				(options.isConsolidado ?
						options.idGrupoMaster+" id_grupomaster,'GRUPMASTER' name_grupomaster, "
				:"		grupomaster.id_grupomaster,\n" +
				"		grupomaster.name_grupomaster,\n") +
				(options.isConsolidado ? 
						
						"	case grupo.id_grupo \n" +
						"	when 81 then 3 \n" +
						"	when 82 then 2 \n" +
						"	when 83 then 17 \n" +
						"	else id_grupo \n" +
							"	end id_grupo, " 
						:
						"		grupo.id_grupo,\n")
							
				+
				(options.isConsolidado ? 
						
						"	case grupo.NOME_GRUPO \n" +
						"	when 'Pessoal Manut. Matriz' then 'Manutenção' \n" +
						"	when 'Pessoal Adm. Matriz' then 'Administrativo' \n" +
						"	else NOME_GRUPO \n" +
							"	end NOME_GRUPO, " 
						:"		grupo.NOME_GRUPO,\n")
				 +
				"		conta.id_conta_contabil,\n" +
				"		conta.nome_conta_contabil,\n" +
				"		FATO.VLR_ORCADO,\n" +
				"		FATO.VLR_REALIZADO\n" +
				"	FROM\n" +
				"		\"DW_sn_afretamento_fato\" fato\n" +
				"	INNER JOIN TB_PRD_GRUPO grupo ON FATO.ID_GRUPOCONTA = GRUPO.ID_GRUPO\n" +
				"	INNER JOIN TB_GRUPOMASTER grupomaster ON FATO.ID_GRUPOMASTER = grupomaster.id_grupomaster\n" +
				"	INNER JOIN \"DW_filial\" filial ON fato.ID_FILIAL = filial.ID_FILIAL\n" +
				"	INNER JOIN \"DW_contacontabil\" conta ON conta.id_conta_contabil = fato.id_contacontabil\n" +
				"	WHERE\n" +
				"	filial.sigla_filial NOT IN ('ITA', 'GUA', 'APM')\n and	fato.ID_ULTRAGRUPO = 0\n" +
				"	AND fato.ID_ANO = ?\n" +
				"	AND FATO.ID_MES BETWEEN ? AND ?\n" +
				
				((options.idGrupo != null && options.idGrupo.length() > 0)||options.filial!= null && (options.filial.equals("CONSOLIDE") ||isTotalPessoal||isDGATotal) && groupIdAux != null  ?  
						"	and	(grupo.ID_GRUPO = ? \n" +
						(groupIdAux != null?" or grupo.id_grupo  "+groupIdAux+"\n":"")+
					
						")\n" : " and ? is null ") +
						
						
				(options.idGrupoMaster != null && options.idGrupoMaster.length() > 0 ? 
						"	and	grupomaster.id_grupomaster = ?\n": " and ?  is null ") +
				(options.filial != null && !options.filial.equals("LISTAR") 
					&& !options.filial.equals("CONSOLIDE") ?  
						"	AND ( FILIAL.sigla_filial = ? "+(locFilial != null ? "OR FILIAL.sigla_filial in ("+locFilial+")":"")+") \n" :"" )+
						
				"	AND FATO.SISTEMA_ORIGEM = 'MXM' )\n" +
				"GROUP BY\n" +
				(options.filial != null && options.filial.equals("LISTAR") ? "sigla_filial, \n" : "") +
				"	id_grupomaster,\n" +
				"	name_grupomaster,\n" +
				"	id_grupo,\n" +
				"	NOME_GRUPO\n" +

				(options.isKeepContaContabil ? 
				"	,id_conta_contabil,\n" +
				"	nome_conta_contabil\n" :"")+
				"ORDER BY\n" +
				(options.filial != null && options.filial.equals("LISTAR") ? "sigla_filial, \n" : "") +
				"	id_grupomaster,\n" +
				"	name_grupomaster,\n" +
				"	id_grupo,\n" +
				"	NOME_GRUPO\n" +
				(options.isKeepContaContabil ? 
				"	,id_conta_contabil,\n" +
				"	nome_conta_contabil":"");
		System.out.println(sql);
		System.out.println("=== FIM DETALHADO ==");
		PreparedStatement ps = null;
		try 
		{
			ps = OracleFactory.getConnection().prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(options.ano));
			ps.setInt(2, Integer.valueOf(options.mesInicial));
			ps.setInt(3, Integer.valueOf(options.mesFinal));
			ps.setString(4, options.idGrupo);
			ps.setString(5, options.idGrupoMaster);
			
			if(options.filial != null && !options.filial.equals("LISTAR") && !options.filial.equals("CONSOLIDE"))
				ps.setString(6, options.filial);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			OracleFactory.createNewConnection();
			QueryFactory.getDetalhado(options);
		}
		return ps;
	}
}
