package com.Gestao_de_Contas.modules.compraparcelada.entity;

import com.Gestao_de_Contas.modules.cartaocredito.entity.CartCreditEntity;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_comprasParceladas")
public class CompareParcelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "tb_cartaoCreditoId")
    @ManyToOne
    private CartCreditEntity cartaoCredito;
    @Column(nullable = false)
    private String loja;
    private String Descricao;
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime dataCompra = LocalDateTime.now();
    @Column(nullable = false)
    private Integer valorTotal;
    @Column(nullable = false)
    private Long quantidadeParcelas;
    private String categoria;
    private Boolean juros;
    private Integer taxaJuros;
    @Column(nullable = false)
    private StatusDivida status;

    @CreationTimestamp
    private LocalDateTime createAt;
}
