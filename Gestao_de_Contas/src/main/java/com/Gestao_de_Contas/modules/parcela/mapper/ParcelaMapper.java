package com.Gestao_de_Contas.modules.parcela.mapper;

import com.Gestao_de_Contas.modules.parcela.dto.ParcelaDTO;
import com.Gestao_de_Contas.modules.parcela.entity.ParcelEntity;
import org.springframework.stereotype.Component;

@Component
public class ParcelaMapper {

    public ParcelEntity toEntity(ParcelaDTO dto) {
        return ParcelEntity.builder()
                .valor(dto.valor())
                .dataVencimento(dto.dataVencimento())
                .numeroParcela(dto.numeroParcela())
                .status(dto.status())
                .dataPagamento(dto.dataPagamento())
                .valorPago(dto.valorPago() != null ? dto.valorPago() : 0) // garante comecar 0
                .build();
    }

    public ParcelaDTO toDTO(ParcelEntity entity) {
        return new ParcelaDTO(
                entity.getNumeroParcela(),
                entity.getValor(),
                entity.getDataVencimento(),
                entity.getStatus(),
                entity.getDataPagamento(),
                entity.getValorPago()
        );
    }
}
