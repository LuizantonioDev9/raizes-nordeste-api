package com.testefinal.demofinal.infrastructure.config;

import com.testefinal.demofinal.domain.enums.Perfil;
import com.testefinal.demofinal.domain.enums.TipoDesconto;
import com.testefinal.demofinal.domain.model.Cupom;
import com.testefinal.demofinal.domain.model.Estoque;
import com.testefinal.demofinal.domain.model.Funcionario;
import com.testefinal.demofinal.domain.model.Produto;
import com.testefinal.demofinal.domain.model.Unidade;
import com.testefinal.demofinal.infrastructure.repository.CupomRepository;
import com.testefinal.demofinal.infrastructure.repository.EstoqueRepository;
import com.testefinal.demofinal.infrastructure.repository.FuncionarioRepository;
import com.testefinal.demofinal.infrastructure.repository.ProdutoRepository;
import com.testefinal.demofinal.infrastructure.repository.UnidadeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FuncionarioRepository funcionarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final CupomRepository cupomRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(FuncionarioRepository funcionarioRepository,
                           UnidadeRepository unidadeRepository,
                           ProdutoRepository produtoRepository,
                           EstoqueRepository estoqueRepository,
                           CupomRepository cupomRepository,
                           PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.unidadeRepository = unidadeRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.cupomRepository = cupomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Unidade unidadeSalvador = criarUnidadeInicial();

        criarAdminInicial(unidadeSalvador);
        criarFuncionarioInicial(unidadeSalvador);

        Produto carneDeSol = criarProdutoSeNaoExistir(
                "Carne de Sol com Macaxeira",
                "Carne de sol acompanhada de macaxeira cozida.",
                new BigDecimal("36.00")
        );

        Produto cuscuz = criarProdutoSeNaoExistir(
                "Cuscuz Recheado",
                "Cuscuz nordestino recheado.",
                new BigDecimal("18.99")
        );

        Produto sucoCaja = criarProdutoSeNaoExistir(
                "Suco de Cajá",
                "Suco regional de cajá.",
                new BigDecimal("8.99")
        );

        criarEstoqueSeNaoExistir(carneDeSol, unidadeSalvador, 20);
        criarEstoqueSeNaoExistir(cuscuz, unidadeSalvador, 20);
        criarEstoqueSeNaoExistir(sucoCaja, unidadeSalvador, 20);

        criarCupomPromo10SeNaoExistir();
    }

    private Unidade criarUnidadeInicial() {
        //Verifica se a unidade base ja existe para não duplicar toda vez que a aplicação reiniciar
        Optional<Unidade> unidadeExistente =
                unidadeRepository.findByNomeIgnoreCase("Unidade Salvador Shopping");

        if (unidadeExistente.isPresent()) {
            return unidadeExistente.get();
        }

        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Salvador Shopping");
        unidade.setCidade("Salvador");
        unidade.setEndereco("Avenida Tancredo Neves, 2915 - Caminho das Árvores, Salvador - BA");

        return unidadeRepository.save(unidade);
    }

    private void criarAdminInicial(Unidade unidade) {
        String emailAdmin = "admin@teste.com";

        if (funcionarioRepository.findByEmail(emailAdmin).isPresent()) {
            return;
        }

        Funcionario admin = new Funcionario();
        admin.setNome("Administrador Teste");
        admin.setEmail(emailAdmin);
        admin.setSenha(passwordEncoder.encode("123456"));
        admin.setTelefone("71999990000");
        admin.setPerfil(Perfil.ADMIN);
        admin.setUnidade(unidade);

        funcionarioRepository.save(admin);
    }

    private void criarFuncionarioInicial(Unidade unidade) {
        String emailFuncionario = "funcionario@teste.com";

        if (funcionarioRepository.findByEmail(emailFuncionario).isPresent()) {
            return;
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Funcionário Teste");
        funcionario.setEmail(emailFuncionario);
        funcionario.setSenha(passwordEncoder.encode("123456"));
        funcionario.setTelefone("71999991111");
        funcionario.setPerfil(Perfil.FUNCIONARIO);
        funcionario.setUnidade(unidade);

        funcionarioRepository.save(funcionario);
    }

    private Produto criarProdutoSeNaoExistir(String nome, String descricao, BigDecimal preco) {
        Optional<Produto> produtoExistente = produtoRepository.findByNomeIgnoreCase(nome);

        if (produtoExistente.isPresent()) {
            return produtoExistente.get();
        }

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);

        return produtoRepository.save(produto);
    }

    private void criarEstoqueSeNaoExistir(Produto produto, Unidade unidade, int quantidade) {
        boolean jaExiste = estoqueRepository.existsByProdutoIdAndUnidadeId(
                produto.getId(),
                unidade.getId()
        );

        if (jaExiste) {
            return;
        }

        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setUnidade(unidade);
        estoque.setQuantidade(quantidade);

        estoqueRepository.save(estoque);
    }

    private void criarCupomPromo10SeNaoExistir() {
        String codigo = "PROMO10";

        if (cupomRepository.findByCodigo(codigo).isPresent()) {
            return;
        }

        Cupom cupom = new Cupom();
        cupom.setCodigo(codigo);
        cupom.setTipoDesconto(TipoDesconto.PERCENTUAL);
        cupom.setValor(new BigDecimal("10.00"));
        cupom.setValidade(LocalDate.now().plusDays(30).atStartOfDay());

        cupomRepository.save(cupom);
    }
}