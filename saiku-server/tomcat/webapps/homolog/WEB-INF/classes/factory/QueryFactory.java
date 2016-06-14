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
		String locFilial = null;
		if(options.filial != null && options.filial.contains("%2B") )
		{
			String[] filiais = options.filial.split("%2B");
			locFilial = "'"+filiais[0].trim()+"','"+filiais[1].trim()+"'";
		}
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
				(options.isConsolidado ? "": "		sigla_filial,\n") +
				(options.isConsolidado ? "":"		ordering,\n") +
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
				(options.isConsolidado ? "": "				sigla_filial,\n") +
				(options.isConsolidado ? "": "				ordering,\n") +
				"				SUM (ORCADO) ORCADO,\n" +
				"				SUM (VLR_REALIZADO) VLR_REALIZADO,\n" +
				"				SUM ( DIFERENCA) DIFERENCA\n" +
				" from(" +
					DetalhadoFactory.getInnerBlock(options)+
					
							(options.isConsolidado ?
							" and grupomaster.id_grupomaster =  "+options.idGrupoMaster
							:"")+
							
				")" +
				"			" +
				"where ID_ANO = "+options.ano+" AND ID_MES BETWEEN  "+
				options.mesInicial+" AND "+options.mesFinal+"\n" +


				
				
				"			GROUP BY\n" +
				(options.isEvolucao ?"				id_mes,\n":"") +
				"				id_ultragrupo\n" +
				"				,nome_ultragrupo\n" +
				(options.isConsolidado ? "": "				,sigla_filial\n") +
				(options.isConsolidado ? "": "				,ordering\n") +
				"			ORDER BY\n" +
				"				id_ultragrupo\n" +
				"				,nome_ultragrupo\n" +
				(options.isConsolidado ? "": "				,ordering\n") +
				"		)\n" +
				"	ORDER BY\n" +
				(options.isEvolucao ?"		1,\n":"") +
				"		id_grupomaster\n" +
				(options.isConsolidado ? "": "		,ordering\n") +
				") t "+	
				(options.filial != null ?" where " +
						"" +
						(locFilial != null ?
								"t.sigla_filial in("+locFilial+") ":"t.sigla_filial = '"+options.filial+"'") : "");
		
			
		
		System.out.println(sql+"\n===========");
		PreparedStatement ps = null;
		try 
		{
			ps = OracleFactory.getConnection().prepareStatement(sql);
			
				

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			OracleFactory.createNewConnection();
			return QueryFactory.getAll(options);
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
			return QueryFactory.getDisponibilidade(mesFinal, ano);
		}
		
		return ps;
	}
	
	public static class LocalOptions
	{
		String groupIdAux = null;
		public boolean isTotalPessoal = false;
		public boolean isDGATotal = false;
		String locFilial = null;
	}
	public static void parseOptions(Options options,LocalOptions localOptions)
	{
		if(options.idGrupo != null && options.idGrupo.equals("99"))
		{
			options.idGrupo = null;
			localOptions.groupIdAux = "in(1,2,3,81,82)";
			localOptions.isTotalPessoal = true;
			options.idGrupoMaster = null;
		}
		else if(options.idGrupo != null && options.idGrupo.equals("98"))
		{
			options.idGrupo = null;
			localOptions.groupIdAux = "in(17,83)";
			localOptions.isDGATotal = true;
			options.idGrupoMaster = null;
		}
		
		if(options.isConsolidado && options.idGrupo != null && options.idGrupoMaster != null &&
				(options.idGrupoMaster.equals("13") ||options.idGrupoMaster.equals("3") ||options.idGrupoMaster.equals("0")))
		{
			if(options.idGrupo.equals("3"))
			{
				localOptions.groupIdAux = "= 81";
			}
			if(options.idGrupo.equals("2"))
			{
				localOptions.groupIdAux = "= 82";
			}
			if(options.idGrupo.equals("17"))
			{
				localOptions.groupIdAux = "= 83";
				localOptions.isDGATotal = true;
			}
			
			options.idGrupoMaster = null;
		}
		if(options.filial != null && options.filial.equals("CONSOLIDE") &&
				options.isConsolidado && 
				options.idGrupo == null && 
				options.idGrupoMaster != null)
		{
			
			if(options.idGrupoMaster.equals("3"))
				localOptions.groupIdAux = "in(1,2,3,81,82)";
			else if(options.idGrupoMaster.equals("13"))
				localOptions.groupIdAux = "in(17,83)";
				
			options.idGrupoMaster = null;
		}
		
		if(options.filial != null && options.filial.contains("%2B") )
		{
			String[] filiais = options.filial.split("%2B");
			localOptions.locFilial = "'"+filiais[0].trim()+"','"+filiais[1].trim()+"'";
		}
	}
	public static PreparedStatement getDetalhado(Options options)
	{
		System.out.println("=== INICIO DETALHADO ==");

		LocalOptions localOptions = new LocalOptions();
		
		parseOptions(options, localOptions);
		String sql = "SELECT\n" +
				(options.filial != null && options.filial.equals("LISTAR") ? 
						" sigla_filial, \n" : 
					"") +
				(
				" id_grupomaster,\n" +
				" name_grupomaster,\n") +
				(options.filial!= null && options.filial.equals("LISTAR") 
						&& (localOptions.isDGATotal || localOptions.isTotalPessoal)?
						"":
				" id_grupo,\n" +
				" NOME_GRUPO,\n") +
				
			(options.filial!= null && options.filial.equals("CONSOLIDE") ?
				"":"	id_conta_contabil,\n" +
				"	nome_conta_contabil,\n")+
				
				" SUM (case when nome_grupo like '(_)%' " +
					"then VLR_ORCADO  else VLR_ORCADO end)*-1 " +
					"ORCADO,\n" +
				" SUM (case when nome_grupo like '(_)%' then VLR_REALIZADO*-1  else vlr_realizado end) VLR_REALIZADO, \n"+
				"\n SUM(VLR_ORCADONOVO) VLR_ORCADONOVO \n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n " +
				(options.filial != null && options.filial.equals("LISTAR") 
				 ? 
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
				 
				( 
						((localOptions.isTotalPessoal||localOptions.isDGATotal) ?
									"REGEXP_REPLACE(id_conta_contabil, '^\\d+', "+
								(localOptions.isDGATotal ? "'(Dga.MTZ+Dga.Fls) '":"'(Emb+Man+Adm) '")+"||nome_conta_contabil) id_conta_contabil,\n" 
									:" id_conta_contabil,\n")
						+
							
						" nome_conta_contabil,\n" 
			)+
				"		FATO.VLR_ORCADO,\n" +
				"		FATO.VLR_REALIZADO, \n"+
				"FATO.VLR_ORCADONOVO \n" +
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
				
				((options.idGrupo != null && options.idGrupo.length() > 0)||
						options.filial!= null && (options.filial.equals("CONSOLIDE") ||
								localOptions.isTotalPessoal||
								localOptions.isDGATotal) && localOptions.groupIdAux != null  ?  
						"	and	(grupo.ID_GRUPO = ? \n" +
						(localOptions.groupIdAux != null?" or grupo.id_grupo  "+localOptions.groupIdAux+"\n":"")+
					
						")\n" : " and ? is null ") +
						
						
				(options.idGrupoMaster != null && options.idGrupoMaster.length() > 0 ? 
						"	and	grupomaster.id_grupomaster = ?\n": " and ?  is null ") +
				(options.filial != null && !options.filial.equals("LISTAR") 
					&& !options.filial.equals("CONSOLIDE") ?  
						"	AND ( FILIAL.sigla_filial = ? "+(localOptions.locFilial != null ? "OR FILIAL.sigla_filial in ("+localOptions.locFilial+")":"")+") \n" :"" )+
						
				"	AND FATO.SISTEMA_ORIGEM = 'MXM' )\n" +
				" GROUP BY\n" +
				(options.filial != null 
				&& options.filial.equals("LISTAR") ? 
				"sigla_filial, \n" : "") +
				"	id_grupomaster\n" +
				"	,name_grupomaster\n" +

				(options.filial != null && options.filial.equals("LISTAR") && (localOptions.isDGATotal || localOptions.isTotalPessoal)?
						"":
				" ,id_grupo\n" +
				" ,NOME_GRUPO\n") +

				(options.filial!= null && options.filial.equals("CONSOLIDE") ?
				" ":"	,id_conta_contabil\n" +
				"	,nome_conta_contabil")+
				" ORDER BY\n" +
				
				(options.filial != null && options.filial.equals("LISTAR") ?
						"sigla_filial, \n" : "") +
				"	id_grupomaster\n" +
				"	,name_grupomaster\n" +

				(options.filial!= null && options.filial.equals("LISTAR") && (localOptions.isDGATotal || localOptions.isTotalPessoal)?
						"":
				" ,id_grupo\n" +
				" ,NOME_GRUPO\n") +
				
				(options.filial!= null && options.filial.equals("CONSOLIDE") ?
				"":"	,id_conta_contabil\n" +
				"	,nome_conta_contabil");
		options.localOptions = localOptions;
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
			return QueryFactory.getDetalhado(options);
		}
		return ps;
	}
	public static PreparedStatement getPorCentroDeCusto(Options options)
	{
		String locFilial = null;
		if(options.filial != null && options.filial.contains("%2B") )
		{
			String[] filiais = options.filial.split("%2B");
			locFilial = "'"+filiais[0].trim()+"','"+filiais[1].trim()+"'";
		}
		String r = "*-1";
		LocalOptions localOptions = new LocalOptions();
		String nomeContaContabil = null;
		if(options.idGrupo == null)
			options.idGrupo = "";
		if(options.idContaContabil != null && options.idContaContabil.startsWith("99999999999"))
		{
			options.idContaContabil = null;
		}
		/** (Emb+Man+Adm) **/
		if(options.idContaContabil!= null && options.idContaContabil.startsWith("(Emb+Man+Adm)"))
		{
			localOptions.groupIdAux = "in(1,2,3,81,82)";
			nomeContaContabil = options.idContaContabil
					.replace("(Emb+Man+Adm) ", "");
			options.idContaContabil = null;
			options.idGrupo = "";
		}
		//(Dga.MTZ+Dga.Fls)
		else if(options.idContaContabil!= null && options.idContaContabil.startsWith("(Dga.MTZ+Dga.Fls)"))
		{
			localOptions.groupIdAux = "in(17,83)";
			nomeContaContabil = options.idContaContabil
					.replace("(Dga.MTZ+Dga.Fls) ", "");
			options.idContaContabil = null;
			options.idGrupo = "";
		}
		else
		{
			localOptions.groupIdAux = " is null ";
		}
		//else if(options.idGrupoMaster.equals("13"))
		//	localOptions.groupIdAux = "in(17,83)";
		parseOptions(options, localOptions);
		String filialString = null;
		/*
		if(options.filial != null && !options.filial.equals("'TMD"))
			filialString = "			" +
					"case FILIAL_ALOCACAO when 'TMD' then 'SDR' else FILIAL_ALOCACAO end FILIAL_ALOCACAO,\n";
		else
			*/
			filialString = "			" +
					"FILIAL_ALOCACAO,\n";
		
		if(options.filial != null && options.filial.equals("SDR %2B TMD"))
		{
			options.filial = "SDR";
		}
		
		String sql = "SELECT\n" +
				"	ano,\n" +
				"	mes,\n" +
				"	ID_GRUPO,\n" +
				"	FILIAL_ALOCACAO,\n" +
				"	NOME_GRUPO,\n" +
				"	COD_CENTRO_CUSTO,\n" +
				"	NOME_CENTRO_CUSTO,\n" +
				"	(SUM (REALIZADO) "+r+") REALIZADO\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			ano,\n" +
				"			mes,\n" +
				filialString +
				"			case \n" +
				"				when FILIAL_ALOCACAO = 'MTZ' and ID_GRUPO = 3 then 81\n" +
				"				when FILIAL_ALOCACAO = 'MTZ' and ID_GRUPO = 2 then 82\n" +
				"				when FILIAL_ALOCACAO = 'MTZ' and ID_GRUPO = 17 then 83\n" +
				"				when FILIAL_ALOCACAO <> 'MTZ' and ID_GRUPO = 81 then 3\n" +
				"				when FILIAL_ALOCACAO <> 'MTZ' and ID_GRUPO = 82 then 2\n" +
				"				when FILIAL_ALOCACAO <> 'MTZ' and ID_GRUPO = 83 then 17\n" +
				"				ELSE ID_GRUPO\n" +
				"			end ID_GRUPO,\n" +
				"			/*\n" +
				"					81	Pessoal Adm. Matriz\n" +
				"					82	Pessoal Manut. Matriz\n" +
				"					83	Outras DGA Adm. Matriz\n" +
				"					2	Manutenção\n" +
				"					3	Administrativo\n" +
				"					17	Outras DGA Filial\n" +
				"			*/\n" +
				"			case \n" +
				"				when FILIAL_ALOCACAO = 'MTZ' and ID_GRUPO = 3 then 'Pessoal Adm. Matriz'\n" +
				"				when FILIAL_ALOCACAO = 'MTZ' and ID_GRUPO = 2 then 'Pessoal Manut. Matriz'\n" +
				"				when FILIAL_ALOCACAO = 'MTZ' and ID_GRUPO = 17 then 'Outras DGA Adm. Matriz'\n" +
				"				when FILIAL_ALOCACAO <> 'MTZ' and ID_GRUPO = 81 then 'Administrativo'\n" +
				"				when FILIAL_ALOCACAO <> 'MTZ' and ID_GRUPO = 82 then 'Manutenção'\n" +
				"				when FILIAL_ALOCACAO <> 'MTZ' and ID_GRUPO = 83 then 'Outras DGA Filial'\n" +
				"				ELSE NOME_GRUPO\n" +
				"			end NOME_GRUPO,\n" +
				"			ID_CONTA_CONTABIL,\n" +
				"			NOME_CONTA_CONTABIL,\n" +
				"			COD_CENTRO_CUSTO,\n" +
				"			NOME_CENTRO_CUSTO,\n" +
				"			REALIZADO\n" +
				"		FROM\n" +
				"			PRD_DATA_COM_TEMADRE,\n" +
				"			TB_PRD_GRUPO_CONTA,\n" +
				"			TB_PRD_GRUPO\n" +
				"		WHERE\n" +
				"			ID_CONTA_CONTABIL = TB_PRD_GRUPO_CONTA.ID_CONTA\n" +
				"		AND TB_PRD_GRUPO.ID_GRUPO not in(81,82,83)\n"+
				"		AND TB_PRD_GRUPO.ID_GRUPO = TB_PRD_GRUPO_CONTA.ID_GRUPO_CONTAS\n" +
				"		AND TB_PRD_GRUPO.TIPO_GRUPO <> 'P'\n" +
				"	) x\n" +
				"WHERE\n 1=1 \n" +
				"	and	(x.ID_GRUPO = ? \n or x.id_grupo "+localOptions.groupIdAux+")" +
						
				"and ano = ?\n" +
				"AND mes BETWEEN ? " +
				"AND ?\n" +
				(nomeContaContabil != null ? "AND NOME_CONTA_CONTABIL = ?\n":"") +
				(options.idContaContabil != null ? "AND ID_CONTA_CONTABIL = ?\n":"") +
				(locFilial != null ? " AND FILIAL_ALOCACAO in("+locFilial+") " :
					
						
					(options.filial != null ? "AND FILIAL_ALOCACAO = ?\n" : "")
					
						)+
				"GROUP BY\n" +
				"	ano,\n" +
				"	mes,ID_GRUPO,\n" +
				"	FILIAL_ALOCACAO,\n" +
				"	NOME_GRUPO,\n" +
				"	COD_CENTRO_CUSTO,\n" +
				"	NOME_CENTRO_CUSTO\n" +
				"ORDER BY" +
				"	REALIZADO "+(options.idContaContabil != null && options.idContaContabil.charAt(0) == '3' ? "desc" : "asc")+"\n";
		System.out.println(sql);

		PreparedStatement ps = null;
		try 
		{
			ps = OracleFactory.getConnection().prepareStatement(sql);
			int i = 1;
			ps.setString(i++, String.valueOf(options.idGrupo));
			ps.setInt(i++, Integer.valueOf(options.ano));
			ps.setInt(i++, Integer.valueOf(options.mesInicial));
			ps.setInt(i++, Integer.valueOf(options.mesFinal));
			
			if(options.idContaContabil != null && nomeContaContabil == null)
				ps.setString(i++, String.valueOf(options.idContaContabil));
			else if(nomeContaContabil != null)
				ps.setString(i++, nomeContaContabil);
			
			if(locFilial == null)
			{
				if(options.filial != null && options.idContaContabil != null)
					ps.setString(i++, String.valueOf(options.filial));
				else if(options.filial != null && options.idContaContabil == null)
					ps.setString(i++, String.valueOf(options.filial));
			}
				
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			OracleFactory.createNewConnection();
			return QueryFactory.getPorCentroDeCusto(options);
		}
			
		return ps;
	}
	public static PreparedStatement getEvolucaoGrafico(Options options)
	{
		String locFilial = null;
		if(options.filial != null && options.filial.contains("%2B") )
		{
			String[] filiais = options.filial.split("%2B");
			locFilial= "'"+filiais[0].trim()+"','"+filiais[1].trim()+"'";
		}
		String sql = "SELECT\n" +
				" id_mes,\n" +
				
				/**
				 * EBITDA
				 */
				 //if
				(options.idGrupoMaster.equals("EBT")?
				" 'EBT' id_grupomaster,\n" +
				" 'EBITDA' name_grupomaster,\n"
				://else
				" id_grupomaster,\n" +
				" name_grupomaster,\n") +
				//if idGrupo
				(options.idGrupo!=null?
					" id_grupo,\n" +
					" NOME_GRUPO,"
						:"")+
				
				" SUM (case when nome_grupo like '(_)%' then VLR_ORCADO  else VLR_ORCADO end)*-1 ORCADO,\n" +
				" SUM (case when nome_grupo like '(_)%' then VLR_REALIZADO*-1  else vlr_realizado end) VLR_REALIZADO \n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"		FATO.id_mes,\n" +
				" 		filial.sigla_filial , \n" +
				"		grupomaster.id_grupomaster,\n" +
				"		grupomaster.name_grupomaster,\n" +
				"		grupo.id_grupo,\n" +
				"		grupo.NOME_GRUPO,\n" +
				" id_conta_contabil,\n" +
				" nome_conta_contabil,\n" +
				"		FATO.VLR_ORCADO,\n" +
				"		FATO.VLR_REALIZADO\n" +
				"	FROM\n" +
				"		\"DW_sn_afretamento_fato\" fato\n" +
				"	INNER JOIN TB_PRD_GRUPO grupo ON FATO.ID_GRUPOCONTA = GRUPO.ID_GRUPO\n" +
				"	INNER JOIN TB_GRUPOMASTER grupomaster ON FATO.ID_GRUPOMASTER = grupomaster.id_grupomaster\n" +
				"	INNER JOIN \"DW_filial\" filial ON fato.ID_FILIAL = filial.ID_FILIAL\n" +
				"	INNER JOIN \"DW_contacontabil\" conta ON conta.id_conta_contabil = fato.id_contacontabil\n" +
				"	WHERE\n" +
				"	filial.sigla_filial NOT IN ('ITA', 'GUA', 'APM')\n" +
				
				(locFilial == null ?
					(options.filial != null ? " and filial.sigla_filial = '"+options.filial+"'":" ")
					:
						" and filial.sigla_filial in("+locFilial+")"
				)+
				" and	fato.ID_ULTRAGRUPO = 0\n" +
				"	AND fato.ID_ANO = "+options.ano+"\n" +
				"	AND FATO.ID_MES BETWEEN 1 AND 12 " +
				" and null is null 	and	" +
				

				/**
				 * EBITDA
				 */
				 //if
				(options.idGrupoMaster.equals("EBT")?
						
						"grupomaster.name_grupomaster IN (\n" +
						"	'RECEITA LÍQUIDA',\n" +
						"	'PESSOAL OPERACIONAL',\n" +
						"	'CUSTOS DE AFRETAMENTO',\n" +
						"	'CUSTO DE COMBUSTÍVEL',\n" +
						"'DESP TÉCN E MANUTENÇÃO',\n" +
						"'SEGUROS E P&I',\n" +
						"'PROVISÕES PARA DOCAGEM',\n" +
						"'OUTRAS DESPESAS OPERACIONAIS',\n" +
						"'DESPESAS ADMINISTRATIVAS',\n" +
						"'DESP. COMERCIAIS E COMISSÕES',\n" +
						"'DESPESAS EXTRAORDINÁRIAS')"
						//else
						:"grupomaster.id_grupomaster = "+options.idGrupoMaster+"\n" 
				)+
								(options.idGrupo!=null?
						"and grupo.id_grupo = "+options.idGrupo+"\n"
						:"")+
				"	AND FATO.SISTEMA_ORIGEM = 'MXM' )\n" +
				" GROUP BY\n" +
				"	id_mes\n" +
				(options.idGrupoMaster.equals("EBT")?
						"":
				"	,id_grupomaster\n" +
				"	,name_grupomaster\n") +
				
				//if idGrupo
				(options.idGrupo!=null?
				"	,id_grupo\n" +
				"	,NOME_GRUPO \n"
						:"\n")+
				
				" ORDER BY\n" +
				"	id_mes,id_grupomaster\n" +
				"	,name_grupomaster\n" ;
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
			return getEvolucaoGrafico(options);
		}
		
		return ps;
	}
}
