-- ANTAQ REF

/*
heuristica:
1) crie tabela mapeamento contrato porto: [num_contrato,id_cliente(sigla_carga)]
2) selecione contrato_x através de: filial,cliente
3) selecione valor da tarifa neste contrato_x através da manobra,terminal e faixa de dwt
4) crie as colunas na tabela fato para 
*/
declare 
vlr_interfat number;
P_ID_EMPRESA varchar(4) := 'SAV';
P_ID_OPERACAO number := 130200;--128832
w_cd_tipo_contrato varchar(255);
qtd_rebc_manobra number;
-- variavel usada para dividir o valor da tarifa dependendo do tipo de contrato
fator_tarifa NUMBER := 0;
begin
FOR X IN (
SELECT
	THIS.*, CALCULO.VL_TOTAL_MANOBRA_REAL,
	CALCULO.VL_ISS_REAL
FROM
	VW_OPERACAO_REBOCADOR THIS
	LEFT OUTER JOIN 
		TB_CALCULO_MANOBRA CALCULO
		on THIS.ID_REBOCADOR = CALCULO.ID_REBOCADOR AND CALCULO.ID_MANOBRA_OPERACAO = THIS.ID_MANOBRA_OPERACAO
WHERE
	THIS.ID_OPERACAO = P_ID_OPERACAO
ORDER BY
	ID_TIPO_MANOBRA,
	PROPRIO
)
			LOOP

-- copie aqui

declare 
op_sigla varchar(3);
op_tptarifa varchar(1);
op_dwt numeric;
s_num_contrato varchar(255);
begin
-- 0) quantidade rebocadores na "manobra"

SELECT
	count(*) into qtd_rebc_manobra
FROM
	VW_OPERACAO_REBOCADOR THIS
	LEFT OUTER JOIN TB_CALCULO_MANOBRA CALCULO on THIS.ID_REBOCADOR = CALCULO.ID_REBOCADOR AND CALCULO.ID_MANOBRA_OPERACAO = THIS.ID_MANOBRA_OPERACAO
WHERE 
	THIS.ID_OPERACAO = P_ID_OPERACAO
	and this.id_tipo_manobra = X.id_tipo_manobra;

-- 1) selecione informações da op.
	select 
		substr(num_contrato, 0, 3) sigla, ind_tp_tarifa,dwt
		into 
			op_sigla,op_tptarifa,op_dwt
		from 
			vw_operacao 
		where 
			id_operacao = x.id_operacao;
-- 2) selecione o contrato de interfaturamento
	begin
	select 
		num_contrato 
		into s_num_contrato 
		from 
			dw_contrato_filial 
		where 
			num_contrato like op_sigla||'%' 
			and 
			tarifacao = op_tptarifa and id_cliente = P_ID_EMPRESA;
		exception when no_data_found then 
			select 
				num_contrato 
				into s_num_contrato 
			from dw_contrato_filial 
			where num_contrato 
			like op_sigla||'%'  and id_cliente = P_ID_EMPRESA and (tarifacao = 'A' or TARIFACAO is null);
	end;

dbms_output.put_line('Contrato: ' || s_num_contrato);

-- 2.2) selecione o tipo de contrato para definir o fator de calculo
--POR HORA
--LUMPSUM
--REBOCADOR
--MANOBRA
select cd_tipo_contrato into w_cd_tipo_contrato
 from VW_CONTRATO where NUM_CONTRATO = s_num_contrato;

dbms_output.put_line(w_cd_tipo_contrato);

if w_cd_tipo_contrato = 'MANOBRA' THEN
	fator_tarifa := qtd_rebc_manobra;
end if;

-- 3) selecione o valor linear	
	for t in (select 
		(case op_tptarifa when 'L' then vlr_liner else vlr_tramp end) 
		 vlr_interfat 
		from 
		vw_tarifa_contrato a
		inner join tb_contrato b 
			on a.id_contrato = b.id_contrato
	 	where 
	 		num_contrato = s_num_contrato
	 	and
	 		op_dwt between dwt_inicial and dwt_final
	 	and id_tipo_manobra = X.ID_TIPO_MANOBRA
and A.id_terminal = x.id_terminal and rownum =1
)


loop
	t.vlr_interfat := t.vlr_interfat/fator_tarifa;
	dbms_output.put_line(t.vlr_interfat);
end loop;

end;

end loop;
end;

--notas:
-- [ ] verificar se os contratos existem e se existiu algum erro de digitação
-- [ ] cvrd – vit000018 (liner + tramp) 
--end ANTAQ REF
