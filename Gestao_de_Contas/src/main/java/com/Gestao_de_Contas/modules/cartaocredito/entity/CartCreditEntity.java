package com.Gestao_de_Contas.modules.cartaocredito.entity;

import com.Gestao_de_Contas.modules.user.entity.User;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_cartao_credito")
public class CartCreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(name = "valor_limite")
    private Integer valorLimite;
    @Column(name = "limit_disponivel")
    private Integer limitDisponivel;
    @Column(name = "dia_fechamento")
    private Integer diaFechamento;
    @Column(name = "data_vencimento")
    private Integer diaVencimento;

    private Boolean ativo;

    @JoinColumn(name = "tb_userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
