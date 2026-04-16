package com.Gestao_de_Contas.security.ia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ia")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analisar")
    public Mono<String> analisar(@RequestBody DadosDividaDTO dto) {
        return aiService.obterAnaliseFinanceira(dto);
    }

    @PostMapping("/gerar-estrategia")
    public Mono<ResponseEntity<Map<String, String>>> gerarEstrategia(@RequestBody DadosDividaDTO dto) {
        return aiService.obterAnaliseFinanceira(dto)
                .map(res -> ResponseEntity.ok(Map.of("analise", res)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}