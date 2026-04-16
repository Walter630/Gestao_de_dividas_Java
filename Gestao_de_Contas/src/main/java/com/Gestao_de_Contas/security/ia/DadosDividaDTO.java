package com.Gestao_de_Contas.security.ia;

import java.util.List;

public record DadosDividaDTO(
        String nome,
        List<DividaItemDTO> dividas
) {}

