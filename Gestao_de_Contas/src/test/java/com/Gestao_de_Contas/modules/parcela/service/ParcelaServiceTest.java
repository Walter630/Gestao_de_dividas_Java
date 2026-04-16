package com.Gestao_de_Contas.modules.parcela.service;

import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import com.Gestao_de_Contas.modules.parcela.dto.ParcelaDTO;
import com.Gestao_de_Contas.modules.parcela.entity.ParcelEntity;
import com.Gestao_de_Contas.modules.parcela.mapper.ParcelaMapper;
import com.Gestao_de_Contas.modules.parcela.repository.ParcelaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParcelaServiceTest {

    @Mock
    private ParcelaMapper mapper;
    @Mock
    private ParcelaRepository parcelaRepository;
    @InjectMocks
    private ParcelaService service;

    private final UUID FAKE_ID = UUID.randomUUID(); // ✅ ID fixo e real pra todos os testes
    // helper pra não repetir a criação em todo teste
    private ParcelaDTO buildDTO() {
        return new ParcelaDTO(
                2,
                2000.0,
                LocalDate.now().plusDays(30),
                StatusDivida.PENDENTE,
                null,
                0
        );
    }

    private ParcelEntity buildEntity(ParcelaDTO dto) {
        return ParcelEntity.builder()
                .numeroParcela(dto.numeroParcela())
                .valor(dto.valor())
                .dataVencimento(dto.dataVencimento())
                .status(dto.status())
                .valorPago(0)
                .build();
    }

    @Test
    void create() {
        // Given
        var dto = buildDTO();
        var entity = buildEntity(dto);

        given(mapper.toEntity(dto)).willReturn(entity);
        given(parcelaRepository.save(entity)).willReturn(entity);
        given(mapper.toDTO(entity)).willReturn(dto);

        // When
        var result = service.create(dto);

        // Then
        then(parcelaRepository).should().save(any(ParcelEntity.class));
        assertThat(result.numeroParcela()).isEqualTo(2);
        assertThat(result.valor()).isEqualTo(2000.0);
    }

    @Test
    void findAll() {
        // Given
        var dto = buildDTO();
        var entity = buildEntity(dto);

        given(parcelaRepository.findAll()).willReturn(Collections.singletonList(entity)); // ✅ Entity
        given(mapper.toDTO(entity)).willReturn(dto); // mapper precisa ser mockado também

        // When
        List<ParcelaDTO> result = service.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).numeroParcela()).isEqualTo(2);
        verify(parcelaRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        var dto = buildDTO();
        var entity = buildEntity(dto);

        given(parcelaRepository.findById(any())).willReturn(Optional.of(entity));
        given(mapper.toDTO(entity)).willReturn(dto);

        var result = service.findById(FAKE_ID);

        assertThat(result.numeroParcela()).isEqualTo(2);
        assertThat(result.valor()).isEqualTo(2000.0);
        verify(parcelaRepository, times(1)).findById(FAKE_ID);
        System.out.println(result);
    }

    @Test
    void update() {
        var dto = buildDTO();
        var entity = buildEntity(dto);

        given(parcelaRepository.findById(FAKE_ID)).willReturn(Optional.of(entity));
        given(parcelaRepository.save(any(ParcelEntity.class))).willReturn(entity);
        given(mapper.toDTO(any(ParcelEntity.class))).willReturn(dto);

        var result = service.update(FAKE_ID, dto);

        then(parcelaRepository).should(times(1)).save(any(ParcelEntity.class));
        assertThat(result.numeroParcela()).isEqualTo(2);
        assertThat(result.valor()).isEqualTo(2000.0);
        verify(parcelaRepository, times(1)).findById(any());
        System.out.println(result);
    }

    @Test
    void delete() {
        given(parcelaRepository.existsById(FAKE_ID)).willReturn(true); // ✅ simula que existe

        service.delete(FAKE_ID); // ✅ UUID real

        then(parcelaRepository).should(times(1)).deleteById(FAKE_ID); // ✅ deleteById, não delete
    }
}