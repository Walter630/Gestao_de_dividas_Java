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

    //============================== CREATE ==============================

    @PostMapping
    public ResponseEntity<CompraParceladaDTO> compraParcelada(@RequestBody CompraParceladaDTO compraParceladaDTO) {
        var result = this.compraParceladaService.create(compraParceladaDTO);
        return ResponseEntity.ok().body(result);
    }

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

    @DeleteMapping
    public ResponseEntity<CompraParceladaDTO> deleteCompraParceladaById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
