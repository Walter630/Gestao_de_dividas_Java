package com.Gestao_de_Contas.modules.compraparcelada.mapper;

import com.Gestao_de_Contas.modules.compraparcelada.dto.CompraParceladaDTO;
import com.Gestao_de_Contas.modules.compraparcelada.entity.CompraParcelEntity;
import org.springframework.stereotype.Component;

@Component
public class CompraParcelMapper {
    public CompraParcelEntity compraParcelEntity(CompraParceladaDTO dto) {
        return CompraParcelEntity.builder()
                .dataCompra(dto.dataCompra())
                .juros(dto.juros())
                .loja(dto.loja())
                .status(dto.status())
                .categoria(dto.categoria())
                .quantidadeParcelas(dto.quantidadeParcelas())
                .taxaJuros(dto.taxaJuros())
                .valorTotal(dto.valorTotal())
                .cartaoCredito(dto.cartaoCredito())
                .categoria(dto.categoria())
                .build();
    }

    public CompraParceladaDTO toDto(CompraParcelEntity entity) {
        return new CompraParceladaDTO(
                entity.getCartaoCredito(),
                entity.getLoja(),
                entity.getDescricao(),
                entity.getDataCompra(),
                entity.getValorTotal(),
                entity.getQuantidadeParcelas(),
                entity.getCategoria(),
                entity.getJuros(),
                entity.getTaxaJuros(),
                entity.getStatus()
        );
    }
}
