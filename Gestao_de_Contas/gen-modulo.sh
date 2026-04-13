#!/bin/bash

NOME_MODULO=$1

if [ -z "$NOME_MODULO" ]; then
    echo "Erro: Ex: ./gen-modulo.sh Parcela"
    exit 1
fi

PACOTE_BASE="com.Gestao_de_Contas"
# Pasta do módulo em minúsculo
CAMINHO_BASE="src/main/java/com/Gestao_de_Contas/modules/${NOME_MODULO,,}"

mkdir -p $CAMINHO_BASE/{controller,service,repository,dto,entity}

# --- 1. GERANDO O DTO (RECORD) ---
cat <<EOF > "${CAMINHO_BASE}/dto/${NOME_MODULO}DTO.java"
package ${PACOTE_BASE}.modules.${NOME_MODULO,,}.dto;

import jakarta.validation.constraints.*;

public record ${NOME_MODULO}DTO(
    Long id
    // Adicione os campos aqui, Walter!
) {}
EOF

# --- 2. GERANDO A ENTIDADE (COM MÉTODO ESTÁTICO) ---
cat <<EOF > "${CAMINHO_BASE}/entity/${NOME_MODULO}Entity.java"
package ${PACOTE_BASE}.modules.${NOME_MODULO,,}.entity;

import ${PACOTE_BASE}.modules.${NOME_MODULO,,}.dto.${NOME_MODULO}DTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_${NOME_MODULO,,}s")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ${NOME_MODULO}Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public static ${NOME_MODULO}Entity fromDTO(${NOME_MODULO}DTO dto) {
        return ${NOME_MODULO}Entity.builder()
                // .campo(dto.campo())
                .build();
    }
}
EOF

# --- 3. GERANDO O REPOSITORY (INTERFACE) ---
cat <<EOF > "${CAMINHO_BASE}/repository/${NOME_MODULO}Repository.java"
package ${PACOTE_BASE}.modules.${NOME_MODULO,,}.repository;

import ${PACOTE_BASE}.modules.${NOME_MODULO,,}.entity.${NOME_MODULO}Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${NOME_MODULO}Repository extends JpaRepository<${NOME_MODULO}Entity, Long> {
}
EOF

# --- 4. GERANDO O SERVICE ---
cat <<EOF > "${CAMINHO_BASE}/service/${NOME_MODULO}Service.java"
package ${PACOTE_BASE}.modules.${NOME_MODULO,,}.service;

import ${PACOTE_BASE}.modules.${NOME_MODULO,,}.repository.${NOME_MODULO}Repository;
import ${PACOTE_BASE}.modules.${NOME_MODULO,,}.dto.${NOME_MODULO}DTO;
import ${PACOTE_BASE}.modules.${NOME_MODULO,,}.entity.${NOME_MODULO}Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ${NOME_MODULO}Service {

    private final ${NOME_MODULO}Repository repository;

    public ${NOME_MODULO}Entity create(${NOME_MODULO}DTO dto) {
        var entity = ${NOME_MODULO}Entity.fromDTO(dto);
        return repository.save(entity);
    }
}
EOF

# --- 5. GERANDO O CONTROLLER ---
cat <<EOF > "${CAMINHO_BASE}/controller/${NOME_MODULO}Controller.java"
package ${PACOTE_BASE}.modules.${NOME_MODULO,,}.controller;

import ${PACOTE_BASE}.modules.${NOME_MODULO,,}.service.${NOME_MODULO}Service;
import ${PACOTE_BASE}.modules.${NOME_MODULO,,}.dto.${NOME_MODULO}DTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${NOME_MODULO,,}s")
@RequiredArgsConstructor
public class ${NOME_MODULO}Controller {

    private final ${NOME_MODULO}Service service;

    @PostMapping
    public void create(@RequestBody @Valid ${NOME_MODULO}DTO dto) {
        service.create(dto);
    }
}
EOF

echo "✅ Módulo '$NOME_MODULO' criado com sucesso seguindo Clean Architecture!"