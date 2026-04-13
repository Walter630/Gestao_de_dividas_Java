package com.Gestao_de_Contas.modules.parcela.entity;

import com.Gestao_de_Contas.modules.compraparcelada.entity.CompareParcelEntity;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import com.Gestao_de_Contas.modules.parcela.dto.ParcelaDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_parcelas")
public class ParcelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "tb_compraId")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompareParcelEntity compraId;

    @Column(nullable = false)
    private Integer numeroParcela;
    private Double valor;
    @Column(nullable = false)
    private LocalDateTime dataVencimento;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusDivida status;
    private LocalDateTime dataPagamento;
    private Integer valorPago;

    @CreationTimestamp
    private LocalDateTime creatAt;

    // Na sua Entity
    public static ParcelEntity toEntity(ParcelaDTO dto) {
        return ParcelEntity.builder()
                .valor(dto.valor())
                .dataVencimento(dto.dataVencimento())
                .numeroParcela(dto.numeroParcela())
                .status(dto.status())
                .dataPagamento(dto.dataPagamento())
                .valorPago(dto.valorPago() != null ? dto.valorPago() : 0) // garante comecar 0
                .build();
    }
}


