/* Pis 320201005000000 */
/* Cofins 320201004000000 */
SET serveroutput ON;
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
/