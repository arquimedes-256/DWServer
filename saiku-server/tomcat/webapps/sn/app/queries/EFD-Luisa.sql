

SELECT SDFS_CDEMPRESA
      ,SDFS_CDFILIAL
      ,SDFS_TPOP
      ,SDFS_CDCLIFOR
      ,FOR_NOME
      ,SDFS_DOCUMENTO
      ,SDFS_DATAESCR
      ,SDFS_VLTOTAL
      ,SDFS_VLBCPIS 
      ,SDFS_ALIQPIS
      ,SDFS_VLPIS
      ,SDFS_VLBCCOFINS
      ,SDFS_ALIQCOFINS
      ,SDFS_VLCOFINS
        FROM SPEDDOCFISSERV_SDFS
            ,FORNEC_FOR
        WHERE SDFS_CDCLIFOR = FOR_CODIGO
        AND   SDFS_DATAESCR >= TO_DATE ('01/05/16','DD/MM/YY')
        AND   SDFS_DATAESCR <= TO_DATE ('31/05/16','DD/MM/YY')
        AND   SDFS_CDEMPRESA = '004' --Sulnorte código 004 / HDANTAS código 001
        AND   SDFS_TPOP IN ('H09') --Tipo do Relatório 
            ORDER BY FOR_NOME
                    
                                       

