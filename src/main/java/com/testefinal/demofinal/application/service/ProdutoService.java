package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.ProdutoNaoEncontradoException;
import com.testefinal.demofinal.domain.model.Produto;
import com.testefinal.demofinal.infrastructure.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto criarProduto(Produto produto) {
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

    public void removerProduto(UUID id) {
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.delete(produto);
    }

    public Produto atualizarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }
}
