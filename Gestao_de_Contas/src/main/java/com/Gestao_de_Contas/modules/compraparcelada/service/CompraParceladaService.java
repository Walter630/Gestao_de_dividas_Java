package com.Gestao_de_Contas.modules.compraparcelada.service;

import com.Gestao_de_Contas.modules.cartaocredito.repository.CartaoCreditoRepository;
import com.Gestao_de_Contas.modules.compraparcelada.dto.CompraParceladaDTO;
import com.Gestao_de_Contas.modules.compraparcelada.entity.CompraParcelEntity;
import com.Gestao_de_Contas.modules.compraparcelada.mapper.CompraParcelMapper;
import com.Gestao_de_Contas.modules.compraparcelada.repository.compraParceladaRepository;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import com.Gestao_de_Contas.modules.parcela.entity.ParcelEntity;
import com.Gestao_de_Contas.modules.parcela.repository.ParcelaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
public class CompraParceladaService {

    @Autowired
    private compraParceladaRepository compraParceladaRepository;
    @Autowired
    private CompraParcelMapper compraParcelMapper;
    @Autowired
    private ParcelaRepository parcelaRepository;
    @Autowired
    private CartaoCreditoRepository cartaoRepository;

    //============================== CREATE (UNIFICADO) ==============================
    @Transactional
    public CompraParceladaDTO create(CompraParceladaDTO compra) {
        var cartao = cartaoRepository.findById(compra.cartaoCredito().getId());
        if (cartao.isEmpty()) {
            throw new RuntimeException("Cartao de credito nao encontrado");
        }

        var cartaoReal = cartao.get();
        // 1. Validação (que estava no create antigo)
        if (compra.juros() && compra.taxaJuros() == null) {
            throw new IllegalArgumentException("Informe a taxa de juros");
        }
        // 2. Salva a compra (que estava no salvarCompra)
        CompraParcelEntity compraEntity = compraParcelMapper.compraParcelEntity(compra);
        compraEntity.setCartaoCredito(cartaoReal);
        CompraParcelEntity compraSalva = compraParceladaRepository.save(compraEntity);
        // 3. Gera as parcelas automaticamente

        LocalDate dataCompra = compra.dataCompra().toLocalDate();

        Integer diaF = cartaoReal.getDiaFechamento();
        Integer diaV = cartaoReal.getDiaVencimento();

        int fechamento = (diaF != null) ? diaF : 10;
        int vencimento = (diaV != null) ? diaV : 17;
        for (int i = 1; i <= compra.quantidadeParcelas(); i++) {
            ParcelEntity parcela = new ParcelEntity();
            parcela.setCompraId(compraSalva);
            parcela.setNumeroParcela(i);
            parcela.setValor((double) (compra.valorTotal() / compra.quantidadeParcelas()));
            parcela.setStatus(StatusDivida.PENDENTE);
            LocalDate vencimentoCart = calcularDataVencimento(dataCompra, i, fechamento, vencimento);
            parcela.setDataVencimento(vencimentoCart);
            parcelaRepository.save(parcela);
        }
        return compraParcelMapper.toDto(compraSalva);
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


    private LocalDate calcularDataVencimento(LocalDate dataCompra, int parcelaNum, int diaFech, int diaVenc) {
        // Se a compra foi feita após o fechamento, a primeira parcela pula um mês
        int mesesAdicionais = (dataCompra.getDayOfMonth() > diaFech) ? parcelaNum : parcelaNum - 1;

        LocalDate dataBase = dataCompra.plusMonths(mesesAdicionais);

        // Retorna o dia de vencimento no mês calculado
        try {
            return dataBase.withDayOfMonth(diaVenc);
        } catch (DateTimeException e) {
            // Caso o dia de vencimento seja 31 e o mês só tenha 30, pega o último dia do mês
            return dataBase.with(TemporalAdjusters.lastDayOfMonth());
        }
    }
}
