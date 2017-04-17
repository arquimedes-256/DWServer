CREATE OR REPLACE PROCEDURE TOP."CARGA_SIG_MXM" AS 
BEGIN
--- STABLE 
DECLARE 
P_IDREBOCADOR NUMBER;
DEBUG_MODE BOOLEAN := false;
BEGIN 
REMOVE_ORC_DUPL;
REMOVE_ORC_DUPL;
ADD_TMD_PROC;
	delete from FULL_TABLE;
	IF NOT DEBUG_MODE THEN
		DELETE FROM "DW_sn_afretamento_fato" WHERE SISTEMA_ORIGEM = 'MXM';
	END IF;
	DECLARE
		MES NUMBER;
	BEGIN
		FOR ANO_ IN 2014..2017
		--FOR ANO_ IN 2016..2016
			LOOP
				FOR I IN 1..12 LOOP
					FOR X IN (SELECT ID_CONTA_CONTABIL FROM "DW_contacontabil")
					LOOP
							FOR F IN(SELECT SIGLA_FILIAL FROM "DW_filial")
							LOOP
								INSERT INTO FULL_TABLE(FILIAL_ALOCACAO,MES,VLR_REALIZADO,ID_CONTA_CONTABIL,ANO)
								VALUES(F.SIGLA_FILIAL,I,0,X.ID_CONTA_CONTABIL,ANO_);
								
								declare orcount number := 0;
								BEGIN
										select count(*) into orcount from TB_PRD_ORCAMENTO 
											where ID_CONTA_CONTABIL = X.ID_CONTA_CONTABIL
											and MES = I
											and ANO = ANO_
											and FILIAL_ORIGEM = F.SIGLA_FILIAL;

											if orcount = 0 THEN
													insert into TB_PRD_ORCAMENTO(ID_CONTA_CONTABIL,MES,ANO,FILIAL_ORIGEM,VLR_ORCADO)
													VALUES(x.ID_CONTA_CONTABIL,I,ANO_,f.SIGLA_FILIAL,0);
											end if;
								end;
							END LOOP;
					END LOOP;
			END LOOP ;
		END LOOP;
	END ;
	DECLARE N_CONTAS NUMBER;
	BEGIN
		for JJ_K in (select ANO,FILIAL_ALOCACAO,MES,REALIZADO,ID_CONTA_CONTABIL,NOME_CONTA_CONTABIL from PRD_JJ_CARGA)
		LOOP

			SELECT COUNT(*) INTO N_CONTAS 
			FROM "DW_contacontabil" WHERE ID_CONTA_CONTABIL = JJ_K.ID_CONTA_CONTABIL;

			IF (N_CONTAS = 0) THEN
				INSERT INTO "DW_contacontabil"(ID_CONTA_CONTABIL,NOME_CONTA_CONTABIL)
				VALUES(JJ_K.ID_CONTA_CONTABIL,JJ_K.NOME_CONTA_CONTABIL);
			END IF;
			INSERT INTO FULL_TABLE(FILIAL_ALOCACAO,MES,VLR_REALIZADO,ID_CONTA_CONTABIL,ANO)
			values(JJ_K.FILIAL_ALOCACAO,JJ_K.MES,JJ_K.REALIZADO,JJ_K.ID_CONTA_CONTABIL,JJ_K.ANO);
		END LOOP;
	END;
	DECLARE
		J_ID_GRUPO NUMBER := 0;
		P_ID_GRUPOMASTER NUMBER := 0;
		P_ID_SUPERGRUPO number := 0;
		P_ID_GRUPO NUMBER := 0;
		J_ID_FILIAL NUMBER;
		COUNTER number := 1;
	BEGIN
		--
--===========================================================================================
		FOR J IN
				(
			SELECT
				"ANO",
				"MES",
				"ID_ULTRAGRUPO",
				"NOME_ULTRAGRUPO",
				"ID_SUPERGROUPO",
				"NOME_SUPERGRUPO",
				"ID_GRUPOMASTER",
				"NAME_GRUPOMASTER",
				"ID_GRUPO",
				"NOME_GRUPO",
				"FILIAL_ALOCACAO",
				"ID_CONTA_CONTABIL",
				"NOME_CONTA_CONTABIL",
				"PREV",
				"REALIZADO","ORCADO2014"
			from DEBUG_STABLE_GRUPOMASTER---DEBUG_STABLE_WPISCCONFIS--DEBUG_STABLE_WPISCCONFIS DEBUG_STABLE_GRUPOMASTER--PRD_JJ_CARGA
				)
				LOOP

					IF J.ID_CONTA_CONTABIL = 310201009000000 THEN
						J_ID_GRUPO := -1;
					ELSIF J.ID_CONTA_CONTABIL like '3%' THEN
						J_ID_GRUPO := 0;
					END IF;

					BEGIN
						SELECT ID_FILIAL INTO J_ID_FILIAL
						FROM "DW_filial"
						WHERE SIGLA_FILIAL = J.FILIAL_ALOCACAO ; 
						EXCEPTION WHEN no_data_found THEN
							NULL ;
					END ;
				BEGIN 
/*
2	Manutenção	C		
3	Administrativo	C		

907	MTZ	11
13	DESPESAS ADMINISTRATIVAS
81	Pessoal Adm. Matriz	C	MTZ	
82	Pessoal Manut. Matriz	C	MTZ	
83	Outras DGA Adm. Matriz	C	MTZ	
*/
				P_ID_GRUPO := j.id_grupo;
				P_ID_GRUPOMASTER := j.ID_GRUPOMASTER;
				P_ID_SUPERGRUPO := j.ID_SUPERGROUPO;
				/* outras desp gerais e pessoal */
				if p_id_grupo = 16 and J_ID_FILIAL = 907 THEN
					 /* Combustíveis Agua e Esgoto para outras DGA Matriz*/
						if j.ID_CONTA_CONTABIL in(425010001000000,428010004000000) THEN
							P_ID_GRUPO := 83;
							-- despesas administrativas
							P_ID_GRUPOMASTER := 13;
							-- precisa setar grupo nvl 3(super grupo)
							P_ID_SUPERGRUPO := 22;
						/*
							Viagens e Estadias
							Condução / Alimentação
							Brindes
							Auxílios e Donativos
							Revistas, Pub. e Assin.de Periódicos
							Energia Elétrica e Força
							Correios e Malotes
						*/
					elsif j.ID_CONTA_CONTABIL = 427010012000000 THEN
						goto end_loop;
					elsif j.ID_CONTA_CONTABIL in (427010001000000,
						427010006000000,
						427010011000000,
						427010013000000,
						428010001000000,
						428010003000000
						) THEN
							P_ID_GRUPO := 83;
							-- despesas administrativas
							P_ID_GRUPOMASTER := 13;
							-- precisa setar grupo nvl 3(super grupo)
							P_ID_SUPERGRUPO := 22;
						end if;
				end if;
				IF P_ID_GRUPO IN (2,3,17) AND J_ID_FILIAL = 907 THEN 
						-- despesas administrativas
						P_ID_GRUPOMASTER := 13;
						-- precisa setar grupo nvl 3(super grupo)
						P_ID_SUPERGRUPO := 22;

						IF j.id_grupo = 2 THEN
							P_ID_GRUPO := 82;
						ELSIF j.id_grupo = 3 THEN
							P_ID_GRUPO := 81;
						ELSIF j.id_grupo = 17 THEN
							P_ID_GRUPO := 83;
						END IF;
				END IF;
				-- ajuste conta operacional pertencer a tecnica a partir de 2016 para cond alimentacao

				IF NOT DEBUG_MODE THEN
					BEGIN
					if P_ID_GRUPO = 75 then
						J.PREV := 0;
					end if;
						INSERT INTO "DW_sn_afretamento_fato" (
															ID_FATO,
															ID_CONTACONTABIL,
															ID_FILIAL,
															ID_ANO,
															ID_MES,
															ID_GRUPOCONTA,
															VLR_ORCADO,
															VLR_REALIZADO,
															VLR_DIFERENCA_ORCADO_REALIZADO,
															ID_GRUPOMASTER,
															ID_SUPERGRUPO,
															ID_ULTRAGRUPO,
															VLR_ORCADONOVO,
															SISTEMA_ORIGEM
														)
														VALUES
															(
																COUNTER,
																J.ID_CONTA_CONTABIL,
																J_ID_FILIAL,
																J.ANO,
																J.MES,
																P_ID_GRUPO,
																J.PREV,
																J.REALIZADO *- 1,
																J.PREV - j.REALIZADO,
																P_ID_GRUPOMASTER,
																P_ID_SUPERGRUPO,
																j.ID_ULTRAGRUPO,
																j.orcado2014,
																'MXM'
															) ;
					end;
								
				END IF;
				END ;
				<<end_loop>> null;
				END LOOP;		
	END;
END;
"sig_carga_ajuste_corporativo";
CARGA_MSUM_ORCAMENTONOVO; --CARGA_MEDIA_ORCAMENTONOVO;

--corrige juridico do comparatrivo
begin
for x in (
select ID_FILIAL,ID_ULTRAGRUPO,ID_MES,VLR_ORCADO from "DW_sn_afretamento_fato" 
where SISTEMA_ORIGEM = 'MXM' 
and ID_CONTACONTABIL = 452010003000000 and id_ano in(2016)
and ID_FILIAL = 907
and ID_ULTRAGRUPO = 0
and vlr_orcado is not null
)
loop
	update "DW_sn_afretamento_fato" set VLR_ORCADO = x.VLR_ORCADO
--select count(*) from "DW_sn_afretamento_fato" 
 where SISTEMA_ORIGEM = 'MXM' 
and ID_CONTACONTABIL = 452010003000000
and ID_ano=2017
and ID_FILIAL = 907
and VLR_ORCADO is not null
and ID_ULTRAGRUPO = 0
and ID_MES = x.id_mes;

end loop;
end;

			

UPDATE "DW_sn_afretamento_fato" 
SET ID_GRUPOCONTA = 10, ID_GRUPOMASTER = 6, ID_SUPERGRUPO = 16
  WHERE ID_ANO >= 2016 AND ID_CONTACONTABIL = 427010006000000 and ID_GRUPOCONTA <> 83;
CARGAMXM_TMDSDR_5050;

/* Pis 320201005000000 */
/* Cofins 320201004000000 */
begin

	/*1. Defina percent para cada mês para TMD */
	for X in (

		select 2016 ano,1 mes,.99 percent from dual union all
		select 2016 ano,2 mes,.99 percent from dual union all
		select 2016 ano,3 mes,.94 percent from dual union all
		select 2016 ano,4 mes,.97 percent from dual

	)
	loop
		dbms_output.put_line(X.mes);
		for Y in (
			select 320201005000000 cc from dual union all 
			select 320201004000000 cc from dual)
		loop
			declare
				vlr_cheio_real number;
				vlr_cheio_orcado number;
			begin

				select (vlr_orcado),(vlr_realizado) into vlr_cheio_orcado , vlr_cheio_real 
				from "DW_sn_afretamento_fato" 
					where id_contacontabil = Y.CC 
					and id_mes = X.mes 
					and id_ano = X.ano 
					and id_filial = 6 
					and id_ultragrupo = 0;

				update "DW_sn_afretamento_fato" set vlr_orcado = vlr_cheio_orcado*(1-X.percent)  
					where id_contacontabil = Y.CC 
					and id_mes = X.mes 
					and id_ano = X.ano 
					and id_filial = 6 
					and id_ultragrupo = 0;

				update "DW_sn_afretamento_fato"  set vlr_orcado = vlr_cheio_orcado*(X.percent) 
					where id_contacontabil = Y.CC 
					and id_mes = X.mes 
					and id_ano = X.ano 
					and id_filial = 999 
					and id_ultragrupo = 0;

				update "DW_sn_afretamento_fato" set vlr_realizado = vlr_cheio_real*(1-X.percent)  
					where id_contacontabil = Y.CC 
					and id_mes = X.mes 
					and id_ano = X.ano 
					and id_filial = 6 
					and id_ultragrupo = 0;

				update "DW_sn_afretamento_fato"  set vlr_realizado = vlr_cheio_real*(X.percent) 
					where id_contacontabil = Y.CC 
					and id_mes = X.mes 
					and id_ano = X.ano 
					and id_filial = 999 
					and id_ultragrupo = 0;

				dbms_output.put_line(Y.cc);
				dbms_output.put_line('Orc:'||vlr_cheio_orcado||' ; Real: '||vlr_cheio_real);
			end;
		end loop;
	end loop;
	commit;
end;
END;
/
