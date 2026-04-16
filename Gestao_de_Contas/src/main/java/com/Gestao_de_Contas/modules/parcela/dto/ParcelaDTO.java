package com.Gestao_de_Contas.modules.parcela.dto;

import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import com.Gestao_de_Contas.modules.parcela.entity.ParcelEntity;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ParcelaDTO (
        Integer numeroParcela,
        @NotNull
        @NumberFormat(style = NumberFormat.Style.NUMBER)
        Double valor,
        @NotNull
        @FutureOrPresent
        LocalDate dataVencimento,
        StatusDivida status,
        @PastOrPresent
        LocalDateTime dataPagamento,
        @NotNull
        Integer valorPago

) {
    // Método para converter Entity -> DTO
    public static ParcelaDTO fromEntity(ParcelEntity entity) {
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
