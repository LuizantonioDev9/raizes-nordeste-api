package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.domain.enums.TipoDesconto;
import com.testefinal.demofinal.domain.exception.CupomNaoEncontradoException;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.NegocioException;
import com.testefinal.demofinal.domain.exception.ValidaRegraException;
import com.testefinal.demofinal.domain.model.Cupom;
import com.testefinal.demofinal.infrastructure.repository.CupomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CupomService {

    private final CupomRepository cupomRepository;

    public CupomService(CupomRepository cupomRepository) {
        this.cupomRepository = cupomRepository;
    }

    @Transactional
    public Cupom salvar(Cupom cupom) {
        if(cupomRepository.existsByCodigo(cupom.getCodigo())) {
            throw new NegocioException("O cupom " + cupom.getCodigo() + " já está cadastrado.");
        }

        if(cupom.getValor() == null || cupom.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidaRegraException("O valor do cupom deve ser maior que zero");
        }

        if(TipoDesconto.PERCENTUAL.equals(cupom.getTipoDesconto()) &&
            cupom.getValor().compareTo(new BigDecimal("100")) > 0
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

        if ("PERCENTUAL".equals(cupom.getTipoDesconto())) {
            desconto = total.multiply(
                    cupom.getValor().divide(BigDecimal.valueOf(100))
            );
        } else {
            desconto = cupom.getValor();
        }

        BigDecimal totalFinal = total.subtract(desconto);

        return totalFinal
                .setScale(2, RoundingMode.HALF_UP)
                .max(BigDecimal.ZERO);
    }

    private void validadeCupom(Cupom cupom) {
        if (cupom.getValidade().isBefore(LocalDateTime.now())) {
            throw new NegocioException("Este cupom já está expirado");
        }
    }
}
