package com.Gestao_de_Contas.modules.parcela.controller;

import com.Gestao_de_Contas.modules.parcela.dto.ParcelaDTO;
import com.Gestao_de_Contas.modules.parcela.service.ParcelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/parcel")
@RequiredArgsConstructor
public class ParcelaController {
    private final ParcelaService parcelaService;

    //============================== GET ==============================

    @GetMapping
    public List<ParcelaDTO> findAll() {
        return parcelaService.findAll();
    }

    //============================== POST ==============================

    @PostMapping
    public ResponseEntity<ParcelaDTO> save(@RequestBody @Valid ParcelaDTO parcelaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parcelaService.create(parcelaDTO));
    }

    //============================== UPDATE ==============================

    @PutMapping
    public ResponseEntity<ParcelaDTO> update(@RequestBody ParcelaDTO parcelaDTO, UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parcelaService.update(id, parcelaDTO));
    }

    //============================== DELETE ==============================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        parcelaService.delete(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    //============================== GETID ==============================

    @GetMapping("/{id}")
    public ResponseEntity<ParcelaDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(parcelaService.findById(id));
    }
}
