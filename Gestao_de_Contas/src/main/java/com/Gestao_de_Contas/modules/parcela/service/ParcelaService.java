package com.Gestao_de_Contas.modules.parcela.service;

import com.Gestao_de_Contas.modules.parcela.dto.ParcelaDTO;
import com.Gestao_de_Contas.modules.parcela.entity.ParcelEntity;
import com.Gestao_de_Contas.modules.parcela.mapper.ParcelaMapper;
import com.Gestao_de_Contas.modules.parcela.repository.ParcelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParcelaService {
    private final ParcelaRepository parcelaRepository;
    private final ParcelaMapper parcelaMapper;

    //============================== POST ==============================

    public ParcelaDTO create(ParcelaDTO parcelaDTO) {
        // Converte DTO -> Entity, salva, e o resultado converte Entity -> DTO
        var parcelaSaved = this.parcelaRepository.save(parcelaMapper.toEntity(parcelaDTO));
        return parcelaMapper.toDTO(parcelaSaved);
    }

    //============================== GET ID ==============================

    public ParcelaDTO findById(UUID id) {
        return this.parcelaRepository.findById(id)
                .map(parcelaMapper::toDTO)// se existir transforma para DTO
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    //============================== UPDATE ==============================

    public ParcelaDTO update(UUID id, ParcelaDTO parcelaDTO) {
        return this.parcelaRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setValor(parcelaDTO.valor());
                    existingEntity.setDataVencimento(parcelaDTO.dataVencimento());
                    existingEntity.setStatus(parcelaDTO.status());
                    existingEntity.setDataPagamento(parcelaDTO.dataPagamento());
                    existingEntity.setValorPago(parcelaDTO.valorPago());
                    // Salva a própria existingEntity
                    return parcelaMapper.toDTO(this.parcelaRepository.save(existingEntity));
                })
                .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));
    }

    //============================== DELETE ==============================

    public void delete(UUID id) {
        if (!this.parcelaRepository.existsById(id)) {
            throw new RuntimeException("Parcela não encontrada");
        }
        this.parcelaRepository.deleteById(id);
    }

    //============================== FINDALL ==============================

    public List<ParcelaDTO> findAll() {
        return this.parcelaRepository.findAll()
                .stream()
                .map(parcelaMapper::toDTO)// Converte cada item da lista
                .toList();
    }
}
