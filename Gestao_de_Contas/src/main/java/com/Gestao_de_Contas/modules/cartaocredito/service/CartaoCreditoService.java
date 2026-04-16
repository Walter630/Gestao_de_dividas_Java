package com.Gestao_de_Contas.modules.cartaocredito.service;

import com.Gestao_de_Contas.modules.cartaocredito.dto.cartaoCreditoDTO;
import com.Gestao_de_Contas.modules.cartaocredito.entity.CartCreditEntity;
import com.Gestao_de_Contas.modules.cartaocredito.repository.CartaoCreditoRepository;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartaoCreditoService { // Nome corrigido para PascalCase
    private final CartaoCreditoRepository repository;

    @Transactional // Boa prática para operações de escrita
    public CartCreditEntity create(cartaoCreditoDTO dto, User userLogado) {
        // 1. Verificação de nome duplicado
        repository.findByName(dto.name())
                .ifPresent(c -> { throw new RuntimeException("Você já possui um cartão com este nome"); });

        // 2. Construção da Entity com mapeamento correto
        CartCreditEntity cartao = CartCreditEntity.builder()
                .name(dto.name())
                .valorLimite(dto.valorLimite())
                .limitDisponivel(dto.limitDisponivel())
                .diaFechamento(dto.diaFechamento())
                .diaVencimento(dto.diaVencimento())
                .ativo(dto.ativo() != null ? dto.ativo() : true)
                .user(userLogado)
                .build();

        return repository.save(cartao);
    }

    public List<CartCreditEntity> findAllByUser(User user) {
        // Idealmente, você deve buscar apenas os cartões do usuário logado
        return repository.findByUser(user);
    }

    public CartCreditEntity update(UUID id, cartaoCreditoDTO dto, User userLogado) {
        // No update, primeiro buscamos o existente
        CartCreditEntity existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Atualiza apenas o que faz sentido
        existente.setName(dto.name());
        existente.setValorLimite(dto.valorLimite());
        existente.setLimitDisponivel(dto.limitDisponivel());

        return repository.save(existente);
    }

    public CartCreditEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}