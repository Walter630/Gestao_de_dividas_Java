package com.Gestao_de_Contas.modules.parcela.entity;

import com.Gestao_de_Contas.modules.compraparcelada.entity.CompraParcelEntity;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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
    private CompraParcelEntity compraId;

    @Column(nullable = false)
    private Integer numeroParcela;
    private Double valor;
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusDivida status;
    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;
    @Column(name = "valor_pago")
    private Integer valorPago;

    @CreationTimestamp
    private LocalDateTime creatAt;

    // Na sua Entity

}


