package factory.string;

import model.Options;

public class DetalhadoFactory 
{
	public static String getDespExtraordinarias(Options options)
	{
		return "SELECT\n" +
				(options.isEvolucao ?"	cast(mes as int) mes,\n": "") +
				"	9898989 id_grupomaster,\n" +
				"	'DESPESAS EXTRAORDINÁRIAS' name_grupomaster,\n" +
				(options.isConsolidado ? "":"	'MTZ' sigla_filial,\n") +
				(options.isConsolidado ? "":"	0 ordering,\n") +
				"	0 ORCADO,\n" +
				"	SUM (vlr_realizado) VLR_REALIZADO,0 DIFERENCA\n" +
				"FROM\n" +
				"	DIREX\n" +
				"WHERE\n" +
				"	ano = "+options.ano+"\n" +
				"AND MES between "+options.mesInicial+" and "+options.mesFinal+" \n" +
				(options.isEvolucao ?"GROUP BY mes\n":"") ;
	}

	public static String getMainQuery(Options options) {
		return "SELECT\n" +
				(options.isEvolucao ?"	ID_MES,\n":"") +
				"	id_grupomaster,\n" +
				"	name_grupomaster,\n" +
				(options.isConsolidado ? "":"	sigla_filial,\n") +
				(options.isConsolidado ? "":"	ordering,\n") +
				"	SUM (ORCADO) ORCADO,\n" +
				"	SUM (VLR_REALIZADO) VLR_REALIZADO,\n" +
				"	SUM (DIFERENCA) DIFERENCA\n" +
				" FROM \n" +
				"(" +
					"SELECT\n" +
					"	ID_ANO," +
					"	ID_MES,\n" +
					"	grupomaster.id_grupomaster,\n" +
					"	grupomaster.name_grupomaster,\n" +
					"	filial.sigla_filial,\n" +
					"	filial.ordering,\n" +
					"	(CASE\n" +
					"					WHEN grupo.NOME_GRUPO LIKE '(_)%' THEN FATO.VLR_ORCADO *-1 else fato.vlr_orcado end )*-1 ORCADO,\n" +
					"	(CASE\n" +
					"					WHEN grupo.NOME_GRUPO LIKE '(_)%' THEN FATO.VLR_REALIZADO *-1 else fato.vlr_realizado end) VLR_REALIZADO,\n" +
					"	(\n" +
					"		FATO.VLR_ORCADO - FATO.VLR_REALIZADO \n" +
					"	) DIFERENCA\n" +
					" from " +
					"	\"DW_sn_afretamento_fato\" fato\n" +
					"INNER JOIN TB_PRD_GRUPO grupo ON FATO.ID_GRUPOCONTA = GRUPO.ID_GRUPO\n" +
					"INNER JOIN TB_GRUPOMASTER grupomaster ON FATO.ID_GRUPOMASTER = grupomaster.id_grupomaster\n" +
					"inner join TB_SUPERGRUPO supergrupo on fato.id_supergrupo = supergrupo.id_supergroupo\n" +
					"INNER JOIN \"DW_filial\" filial ON fato.ID_FILIAL = filial.ID_FILIAL\n" +
					"WHERE\n" +
					"	filial.sigla_filial NOT IN ('ITA', 'GUA', 'APM')\n" +
					"AND FATO.SISTEMA_ORIGEM = 'MXM'\n" +
					"AND fato.ID_ULTRAGRUPO = 0\n" +
					
		"\n union all \n" +
					
					"SELECT\n" +
					"	ID_ANO," +
					"	ID_MES,\n" +
					"	grupomaster.id_grupomaster,\n" +
					"	grupomaster.name_grupomaster,\n" +
					"				case \n" +
					"					when filial.sigla_filial in('RJ1','SEP') then 'RJ1 + SEP' \n" +
					"					when filial.sigla_filial in('SDR','TMD') then 'SDR + TMD' \n" +
					"				" +
					"				end sigla_filial,\n" +
					"				case \n" +
					"					when filial.sigla_filial in('RJ1','SEP') then 6.5 \n" +
					"					when filial.sigla_filial in('SDR','TMD') then 9.5 \n" +
					"				" +
					"				end ordering,\n" +
					
					"	(CASE\n" +
					"					WHEN grupo.NOME_GRUPO LIKE '(_)%' THEN FATO.VLR_ORCADO *-1 else fato.vlr_orcado end )*-1 ORCADO,\n" +
					"	(CASE\n" +
					"					WHEN grupo.NOME_GRUPO LIKE '(_)%' THEN FATO.VLR_REALIZADO *-1 else fato.vlr_realizado end) VLR_REALIZADO,\n" +
					"	(\n" +
					"		FATO.VLR_ORCADO - FATO.VLR_REALIZADO \n" +
					"	) DIFERENCA\n" +
					" from " +
					"	\"DW_sn_afretamento_fato\" fato\n" +
					"INNER JOIN TB_PRD_GRUPO grupo ON FATO.ID_GRUPOCONTA = GRUPO.ID_GRUPO\n" +
					"INNER JOIN TB_GRUPOMASTER grupomaster ON FATO.ID_GRUPOMASTER = grupomaster.id_grupomaster\n" +
					"inner join TB_SUPERGRUPO supergrupo on fato.id_supergrupo = supergrupo.id_supergroupo\n" +
					"INNER JOIN \"DW_filial\" filial ON fato.ID_FILIAL = filial.ID_FILIAL\n" +
					"WHERE\n" +
					"	filial.sigla_filial IN ('RJ1', 'SEP', 'SDR','TMD')\n" +
					"AND FATO.SISTEMA_ORIGEM = 'MXM'\n" +
					"AND fato.ID_ULTRAGRUPO = 0\n" +
					
				")" +
				" where ID_ANO = "+options.ano+" AND ID_MES BETWEEN "+options.mesInicial+" AND "+options.mesFinal+" " +
				"\n" +
				"GROUP BY\n" +
				(options.isEvolucao ?"	id_mes,\n":"") +
				"	id_grupomaster,\n" +
				"	name_grupomaster,\n" +
				"	sigla_filial,\n" +
				"	ordering\n";
	}
	public static String getInnerBlock(Options options)
	{
		return 
		"			SELECT\n" +
		"				ID_ANO,\n"+
		("				ID_MES,\n") +
		"				ultragrupo.id_ultragrupo,\n" +
		"				ultragrupo.nome_ultragrupo,\n" +
		"				filial.sigla_filial,\n" +
		"				filial.ordering,\n" +
		"				(\n" +
		"					CASE\n" +
		"					WHEN supergrupo.nome_supergrupo LIKE '(_)%' THEN\n" +
		"						FATO.VLR_ORCADO *-1\n" +
		"					ELSE\n" +
		"						FATO.VLR_ORCADO \n" +
		"					END\n" +
		"				) ORCADO,\n" +
		"				(\n" +
		"					CASE\n" +
		"					WHEN supergrupo.nome_supergrupo LIKE '(_)%' THEN\n" +
		"						FATO.VLR_REALIZADO *-1\n" +
		"					ELSE\n" +
		"						FATO.VLR_REALIZADO \n" +
		"					END\n" +
		"				) VLR_REALIZADO,\n" +
		"				(\n" +
		"					FATO.VLR_ORCADO - FATO.VLR_REALIZADO \n" +
		"				) DIFERENCA\n" +
		"			FROM\n" +
		"				\"DW_sn_afretamento_fato\" fato\n" +
		"			INNER JOIN TB_PRD_GRUPO grupo ON FATO.ID_GRUPOCONTA = GRUPO.ID_GRUPO\n" +
		"			INNER JOIN TB_GRUPOMASTER grupomaster ON FATO.ID_GRUPOMASTER = grupomaster.id_grupomaster\n" +
		"			INNER JOIN \"DW_filial\" filial ON fato.ID_FILIAL = filial.ID_FILIAL\n" +
		"			INNER JOIN TB_SUPERGRUPO supergrupo ON fato.id_supergrupo = supergrupo.id_supergroupo\n" +
		"			INNER JOIN TB_ULTRAGRUPO ultragrupo ON fato.id_ultragrupo = ultragrupo.id_ultragrupo\n" +
		"			WHERE\n" +
		"				filial.sigla_filial NOT IN ('ITA', 'GUA', 'APM')\n" +
		"			AND FATO.SISTEMA_ORIGEM = 'MXM'\n" +
		"			AND fato.ID_ULTRAGRUPO IN (1, 2)\n" +
		
"\n union all \n" +
"			SELECT\n" +
"				ID_ANO,\n"+
("				ID_MES,\n") +
"				ultragrupo.id_ultragrupo,\n" +
"				ultragrupo.nome_ultragrupo,\n" +
"				case \n" +
"					when filial.sigla_filial in('RJ1','SEP') then 'RJ1 + SEP' \n" +
"					when filial.sigla_filial in('SDR','TMD') then 'SDR + TMD' \n" +
"				" +
"				end sigla_filial,\n" +
"				filial.ordering,\n" +
"				(\n" +
"					CASE\n" +
"					WHEN supergrupo.nome_supergrupo LIKE '(_)%' THEN\n" +
"						FATO.VLR_ORCADO *-1\n" +
"					ELSE\n" +
"						FATO.VLR_ORCADO \n" +
"					END\n" +
"				) ORCADO,\n" +
"				(\n" +
"					CASE\n" +
"					WHEN supergrupo.nome_supergrupo LIKE '(_)%' THEN\n" +
"						FATO.VLR_REALIZADO *-1\n" +
"					ELSE\n" +
"						FATO.VLR_REALIZADO \n" +
"					END\n" +
"				) VLR_REALIZADO,\n" +
"				(\n" +
"					FATO.VLR_ORCADO - FATO.VLR_REALIZADO \n" +
"				) DIFERENCA\n" +
"			FROM\n" +
"				\"DW_sn_afretamento_fato\" fato\n" +
"			INNER JOIN TB_PRD_GRUPO grupo ON FATO.ID_GRUPOCONTA = GRUPO.ID_GRUPO\n" +
"			INNER JOIN TB_GRUPOMASTER grupomaster ON FATO.ID_GRUPOMASTER = grupomaster.id_grupomaster\n" +
"			INNER JOIN \"DW_filial\" filial ON fato.ID_FILIAL = filial.ID_FILIAL\n" +
"			INNER JOIN TB_SUPERGRUPO supergrupo ON fato.id_supergrupo = supergrupo.id_supergroupo\n" +
"			INNER JOIN TB_ULTRAGRUPO ultragrupo ON fato.id_ultragrupo = ultragrupo.id_ultragrupo\n" +
"			WHERE\n" +
"				filial.sigla_filial IN ('RJ1', 'SEP', 'SDR','TMD')\n" +
"			AND FATO.SISTEMA_ORIGEM = 'MXM'\n" +
"			AND fato.ID_ULTRAGRUPO IN (1, 2)\n";
	}
}
