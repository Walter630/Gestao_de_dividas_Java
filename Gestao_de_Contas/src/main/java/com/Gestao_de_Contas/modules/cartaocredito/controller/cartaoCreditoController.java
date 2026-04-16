package com.Gestao_de_Contas.modules.cartaocredito.controller;

import com.Gestao_de_Contas.modules.cartaocredito.dto.cartaoCreditoDTO;
import com.Gestao_de_Contas.modules.cartaocredito.entity.CartCreditEntity;
import com.Gestao_de_Contas.modules.cartaocredito.service.CartaoCreditoService;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cartCredito")
public class cartaoCreditoController {
    private CartaoCreditoService cartaoCreditoService;
    @Autowired
    public cartaoCreditoController(CartaoCreditoService cartaoCreditoService) {
        this.cartaoCreditoService = cartaoCreditoService;
    }

    @PostMapping
    public ResponseEntity<CartCreditEntity> save(@Valid @RequestBody cartaoCreditoDTO cartCreditEntity, @AuthenticationPrincipal User userLogad) {
            var cartaoResult = cartaoCreditoService.create(cartCreditEntity, userLogad);
            return ResponseEntity.ok(cartaoResult);
    }

    @GetMapping
    public ResponseEntity<List<CartCreditEntity>> findAll(@AuthenticationPrincipal User userLogad) {
        List<CartCreditEntity> cartoes = cartaoCreditoService.findAllByUser(userLogad);
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartCreditEntity> findById(@PathVariable UUID id) {
        // Agora retornando um único objeto, não uma lista
        return ResponseEntity.ok(cartaoCreditoService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cartaoCreditoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
