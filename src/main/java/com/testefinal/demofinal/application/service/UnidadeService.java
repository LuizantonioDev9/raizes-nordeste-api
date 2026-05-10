package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.api.DTO.UnidadeDTO;
import com.testefinal.demofinal.domain.exception.ConflitoException;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.model.Unidade;
import com.testefinal.demofinal.infrastructure.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeService {


    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public Unidade cadastrarUnidade(UnidadeDTO unidadeDTO) {
        if(unidadeRepository.existsByNome(unidadeDTO.nome())) {
            throw new ConflitoException("Já existe uma unidade cadastrada com este nome.");
        }

        Unidade novaUnidade = unidadeDTO.mapearUnidade();
        return unidadeRepository.save(novaUnidade);
    }

    public Unidade buscarUnidade(Unidade unidade) {
        return unidadeRepository.findById(unidade.getId())
                .orElseThrow(()-> new NaoEncontradoException("Unidade não encontrada"));
    }

    public void removerUnidade(Unidade unidade) {
        unidadeRepository.deleteById(unidade.getId());
    }

    public List<Unidade> listarUnidades() {
        return this.unidadeRepository.findAll();
    }


}
