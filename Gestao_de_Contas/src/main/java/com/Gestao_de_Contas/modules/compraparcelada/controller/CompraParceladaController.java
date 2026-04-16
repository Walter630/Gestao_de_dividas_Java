package com.Gestao_de_Contas.modules.compraparcelada.controller;

import com.Gestao_de_Contas.modules.compraparcelada.dto.CompraParceladaDTO;
import com.Gestao_de_Contas.modules.compraparcelada.service.CompraParceladaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/compraParcel")
public class CompraParceladaController {
    @Autowired
    private CompraParceladaService compraParceladaService;

    //============================== GETALL ==============================

    @GetMapping
    public ResponseEntity<List<CompraParceladaDTO>> compraParcelada() {
        return ResponseEntity.ok().body(compraParceladaService.findAll());
    }

    //============================== GETID ==============================

    @GetMapping("/{id}")
    public ResponseEntity<CompraParceladaDTO> compraParceladaById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(compraParceladaService.findById(id));
    }

    //============================== DELETE ==============================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompraParceladaById(@PathVariable UUID id) {
        this.compraParceladaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //============================== ENDPOINT DE CRIAÇÃO (COM PARCELAS) ==============================
    @PostMapping
    public ResponseEntity<CompraParceladaDTO> create(@RequestBody CompraParceladaDTO dto) {
        // Chamamos o método salvarCompra para que ele gere as parcelas automaticamente
        CompraParceladaDTO novaCompra = compraParceladaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCompra);
    }

}
