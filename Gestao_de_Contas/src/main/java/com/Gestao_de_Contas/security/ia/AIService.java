package com.Gestao_de_Contas.security.ia;

import com.Gestao_de_Contas.modules.debt.entity.Debt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient; // Use o Builder do Spring!
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AIService {

    private final WebClient webClient;

    // O parâmetro deve ser WebClient.Builder
    public AIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5000").build();
    }

    public Mono<String> obterAnaliseFinanceira(DadosDividaDTO dados) {
        return this.webClient.post()
                .uri("/ia/analisar")
                .bodyValue(dados)
                .retrieve()
                .bodyToMono(String.class);
    }

    public DadosDividaDTO prepararDadosParaIA(String nomeUsuario, List<Debt> dividas) {
        List<DividaItemDTO> itens = dividas.stream()
                .map(debt -> new DividaItemDTO(
                        debt.getDescricao(), // ou o nome do campo na sua entidade Debt
                        debt.getValorOriginal().doubleValue(),
                        debt.getTaxJuros(),
                        debt.getDevedorName()
                ))
                .toList();

        return new DadosDividaDTO(nomeUsuario, itens);
    }
}