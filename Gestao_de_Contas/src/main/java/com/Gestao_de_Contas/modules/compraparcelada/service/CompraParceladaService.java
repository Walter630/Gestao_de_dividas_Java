package com.Gestao_de_Contas.modules.compraparcelada.service;

import com.Gestao_de_Contas.modules.compraparcelada.dto.CompraParceladaDTO;
import com.Gestao_de_Contas.modules.compraparcelada.mapper.CompraParcelMapper;
import com.Gestao_de_Contas.modules.compraparcelada.repository.compraParceladaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompraParceladaService {

    @Autowired
    private compraParceladaRepository compraParceladaRepository;
    @Autowired
    private CompraParcelMapper compraParcelMapper;

    //============================== CREATE ==============================

    public CompraParceladaDTO create(CompraParceladaDTO compraParceladaDTO) {
        if (compraParceladaDTO.juros() && compraParceladaDTO.taxaJuros() == null) {
            throw new IllegalArgumentException("Informe a taxa de juros");
        }
        var compraSaved = this.compraParceladaRepository.save(compraParcelMapper.compraParcelEntity(compraParceladaDTO));
        return compraParcelMapper.toDto(compraSaved);
    }

    //============================== GETALL ==============================

    public List<CompraParceladaDTO> findAll() {
        return compraParceladaRepository.findAll()
                .stream()
                .map(compraParcelMapper::toDto)
                .toList();
    }

    //============================== GETID ==============================

    public CompraParceladaDTO findById(UUID id) {
        return compraParceladaRepository.findById(id)
                .map(compraParcelMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Nao foi possivel encontrar por id"));
    }

    //============================== DELETE ==============================

    public void deleteById(UUID id) {
        if (!compraParceladaRepository.existsById(id)) {
            throw new RuntimeException("Nao foi encontrado por id" + id);
        }
        compraParceladaRepository.deleteById(id);
    }

    //============================== UPDATE ==============================

    public CompraParceladaDTO updateById(UUID id, CompraParceladaDTO compraParceladaDTO) {
        return compraParceladaRepository.findById(id)
                .map(existingCompra -> {
                    existingCompra.setDataCompra(compraParceladaDTO.dataCompra());
                    existingCompra.setCategoria(compraParceladaDTO.categoria());
                    existingCompra.setDescricao(compraParceladaDTO.descricao());
                    existingCompra.setLoja(compraParceladaDTO.loja());
                    existingCompra.setJuros(compraParceladaDTO.juros());
                    existingCompra.setStatus(compraParceladaDTO.status());
                    existingCompra.setValorTotal(compraParceladaDTO.valorTotal());
                    existingCompra.setCartaoCredito(compraParceladaDTO.cartaoCredito());
                    existingCompra.setQuantidadeParcelas(compraParceladaDTO.quantidadeParcelas());
                    existingCompra.setTaxaJuros(compraParceladaDTO.taxaJuros());
                    return compraParcelMapper.toDto(compraParceladaRepository.save(existingCompra));
                })
                .orElseThrow(() -> new RuntimeException("Nao foi encontrado por id" + id));
    }
}
