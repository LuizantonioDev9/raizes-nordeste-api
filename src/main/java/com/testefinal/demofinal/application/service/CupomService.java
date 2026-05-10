package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.domain.enums.TipoDesconto;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.NegocioException;
import com.testefinal.demofinal.domain.exception.ValidaRegraException;
import com.testefinal.demofinal.domain.model.Cupom;
import com.testefinal.demofinal.infrastructure.integration.log.LogService;
import com.testefinal.demofinal.infrastructure.repository.CupomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CupomService {

    private final CupomRepository cupomRepository;
    private final LogService logService;

    public CupomService(CupomRepository cupomRepository, LogService logService) {
        this.cupomRepository = cupomRepository;
        this.logService = logService;
    }

    @Transactional
    public Cupom salvar(Cupom cupom) {
        if (cupomRepository.existsByCodigo(cupom.getCodigo())) {
            throw new NegocioException("O cupom " + cupom.getCodigo() + " já está cadastrado.");
        }

        if (cupom.getValor() == null || cupom.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidaRegraException("O valor do cupom deve ser maior que zero");
        }

        if (TipoDesconto.PERCENTUAL.equals(cupom.getTipoDesconto()) &&
                cupom.getValor().compareTo(new BigDecimal("100")) >= 0
        ) {
            throw new ValidaRegraException("Desconto percentual não pode ser maior que 100%");
        }

        return cupomRepository.save(cupom);
    }


    public List<Cupom> listarCuponsValidos() {
        LocalDateTime agora = LocalDateTime.now();
        return cupomRepository.findByValidadeAfter(agora);
    }


    public Cupom buscarPorCodigo(String codigo) {
        Cupom cupom = cupomRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Cupom não encontrado"));
        validadeCupom(cupom);
        return cupom;
    }

    public BigDecimal aplicarDesconto(Cupom cupom, BigDecimal total) {
        if (cupom == null) {
            return total;
        }

        validadeCupom(cupom);
        BigDecimal desconto;

        if (cupom.getTipoDesconto() == TipoDesconto.PERCENTUAL) {
            desconto = total
                    .multiply(cupom.getValor())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (cupom.getTipoDesconto() == TipoDesconto.FIXO) {
            desconto = cupom.getValor();
        } else {
            throw new ValidaRegraException("Tipo de desconto inválido.");
        }

        BigDecimal totalFinal = total.subtract(desconto).setScale(2, RoundingMode.HALF_UP).max(BigDecimal.ZERO);


        logService.auditoria("Desconto aplicado. cupom="
                + cupom.getCodigo()
                + ", tipoDesconto= " + cupom.getTipoDesconto()
                + ", valorDesconto= " + desconto.setScale(2, RoundingMode.HALF_UP)
                + ", totalAntes= " + total
                + ", totalDepois= " + totalFinal
        );


        return totalFinal;


    }

    private void validadeCupom(Cupom cupom) {
        if (cupom.getValidade().isBefore(LocalDateTime.now())) {
            throw new NegocioException("Este cupom já está expirado");
        }
    }
}
