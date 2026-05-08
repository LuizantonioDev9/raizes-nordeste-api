package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.api.DTO.CatalogoUnidadeResponseDTO;
import com.testefinal.demofinal.api.DTO.ProdutoEstoqueDTO;
import com.testefinal.demofinal.api.DTO.ResumoUnidadeDTO;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.model.Estoque;
import com.testefinal.demofinal.domain.model.Produto;
import com.testefinal.demofinal.domain.model.Unidade;
import com.testefinal.demofinal.domain.exception.NegocioException;
import com.testefinal.demofinal.domain.exception.ProdutoNaoEncontradoException;
import com.testefinal.demofinal.domain.exception.UnidadeNaoEncontradaException;
import com.testefinal.demofinal.infrastructure.repository.EstoqueRepository;
import com.testefinal.demofinal.infrastructure.repository.ProdutoRepository;
import com.testefinal.demofinal.infrastructure.repository.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository, UnidadeRepository unidadeRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
    }

    @Transactional
    public Estoque cadastrarEstoque(UUID produtoId, UUID unidadeId, int quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));

        Unidade unidade = unidadeRepository.findById(unidadeId)
                .orElseThrow(()-> new UnidadeNaoEncontradaException("Unidade não encontrada"));


        if(estoqueRepository.existsByProdutoIdAndUnidadeId(produtoId,unidadeId)) {
            throw new NegocioException("Produto já está associado a esta unidade");
        }

        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setUnidade(unidade);
        estoque.setQuantidade(quantidade);

        return estoqueRepository.save(estoque);
    }

    public Estoque buscarEstoque(UUID produtoId, UUID unidadeId){
        return estoqueRepository.findByProdutoIdAndUnidadeId(produtoId,unidadeId)
                .orElseThrow(()-> new NaoEncontradoException("Estoque nao encontrado"));
    }

    @Transactional
    public void diminuirEstoque(UUID produtoId, UUID unidadeId, int quantidade){
        Estoque estoque = buscarEstoque(produtoId, unidadeId);

        //verifica se tem o produto suficiente antes de diminuir o estoque
        if(estoque.getQuantidade() < quantidade){
            throw new NegocioException("Estoque insuficiente");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);

        //estou forçando o hibernate a alterar o banco imediatamente
        estoqueRepository.saveAndFlush(estoque);
    }

    @Transactional
    public void adicionarAoEstoque(UUID produtoId, UUID unidadeId, int quantidade){
        Estoque estoque = estoqueRepository.findByProdutoIdAndUnidadeId(produtoId, unidadeId)
                .orElseGet(() -> {
                    Estoque novo = new Estoque();
                    novo.setProduto(produtoRepository.findById(produtoId).get());
                    novo.setUnidade(unidadeRepository.findById(unidadeId).get());
                    novo.setQuantidade(0);
                    return novo;
                });

        estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        estoqueRepository.save(estoque);
    }

    public void validarEstoque(UUID produtoId, UUID unidadeId, int quantidade) {
        Estoque estoque = buscarEstoque(produtoId, unidadeId);

        if(quantidade <= 0) {
            throw new NegocioException("Quantidade inválida");
        }

        if(estoque.getQuantidade() < quantidade){
            throw new NegocioException("Estoque insuficiente");
        }
    }

    public CatalogoUnidadeResponseDTO listarProdutosPorUnidade(UUID unidadeId) {
        Unidade unidade = unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new NaoEncontradoException("Unidade não encontrada"));

        List<Estoque> estoques = estoqueRepository.findByUnidadeId(unidadeId);

        List<ProdutoEstoqueDTO> produtos = estoques.stream()
                .map(estoque -> new ProdutoEstoqueDTO(
                        estoque.getProduto().getId(),
                        estoque.getProduto().getNome(),
                        estoque.getProduto().getDescricao(),
                        estoque.getProduto().getPreco(),
                        estoque.getQuantidade()
                ))
                .toList();

        ResumoUnidadeDTO unidadeResumo = new ResumoUnidadeDTO(
                unidade.getId(),
                unidade.getNome(),
                unidade.getCidade(),
                unidade.getEndereco()
        );

        return new CatalogoUnidadeResponseDTO(unidadeResumo, produtos);
    }

    private ResumoUnidadeDTO mapearUnidade(Unidade unidade) {
        return new ResumoUnidadeDTO(
                unidade.getId(),
                unidade.getNome(),
                unidade.getCidade(),
                unidade.getEndereco()
        );
    }

    private ProdutoEstoqueDTO mapearProdutoEstoque(Estoque estoque) {
        Produto produto = estoque.getProduto();

        return new ProdutoEstoqueDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                estoque.getQuantidade()
        );
    }




}
