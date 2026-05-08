package com.testefinal.demofinal.infrastructure.security;

import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import com.testefinal.demofinal.infrastructure.repository.FuncionarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;

    public CustomUserDetailsService(ClienteRepository clienteRepository, FuncionarioRepository funcionarioRepository) {
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var clienteOpt = clienteRepository.findByEmail(username);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            return User.builder()
                    .username(cliente.getEmail())
                    .password(cliente.getSenha())
                    .authorities("ROLE_" + cliente.getPerfil().name())
                    .build();
        }

        var funcionarioOpt = funcionarioRepository.findByEmail(username);

        if (funcionarioOpt.isPresent()) {
            var funcionario = funcionarioOpt.get();

            return User.builder()
                    .username(funcionario.getEmail())
                    .password(funcionario.getSenha())
                    .authorities("ROLE_" + funcionario.getPerfil().name())
                    .build();
        }

        throw new UsernameNotFoundException("Usuário não encontrado");
    }

}
