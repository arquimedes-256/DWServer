/* Pis 320201005000000 */
/* Cofins 320201004000000 */

begin

	/*1. Defina percent para cada mês para TMD */
	for X in (

		select 2016,1,.99 percent from dual union all
		select 2016,1,.99 percent from dual union all
		select 2016,1,.94 percent from dual union all
		select 2016,1,.97 percent from dual

	)
	loop
		for CC in (
			select 320201005000000 cc from dual union all 
			select 320201004000000 cc from dual)
		loop
			
		end loop;
	end loop;
end;