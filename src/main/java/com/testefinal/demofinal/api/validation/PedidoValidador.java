package com.testefinal.demofinal.api.validation;

import com.testefinal.demofinal.api.DTO.NovoPedidoRequestDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoValidador {

    public List<String> checaErros(NovoPedidoRequestDTO requestDTO) {
        List<String> erros = new ArrayList<>();

        if(requestDTO.canalPedido() == null) {
            erros.add("O canal de pedido é obrigatorio (APP,WEB,TOTEM,BALCAO.");
        }

        if(requestDTO.itens() == null || requestDTO.itens().isEmpty()) {
            erros.add("O pedido está vazio. Adicione um item");
        }

        return erros;
    }
}
