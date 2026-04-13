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
    @Column(nullable = false)
    private Integer limit;
    @Column(nullable = false)
    private Integer limitDiponivel;
    @Column(nullable = false)
    private Integer diaFechamento;
    @Column(nullable = false)
    private Integer dataVencimento;

    private Boolean ativo;

    @JoinColumn(name = "tb_userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
