package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.ValidaRegraException;
import com.testefinal.demofinal.domain.model.Produto;
import com.testefinal.demofinal.infrastructure.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto criarProduto(Produto produto) {
        if(produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidaRegraException("O preço do produto deve ser maior que zero");
        }
        return produtoRepository.save(produto);
    }
    public Produto buscarProduto(Produto produto) {
        return produtoRepository.findById(produto.getId())
                .orElseThrow(() -> new NaoEncontradoException("Produto não encontrado"));
    }

    public Produto buscarProdutoPorId(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Produto não encontrado"));
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }


    public Produto atualizarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }
}
