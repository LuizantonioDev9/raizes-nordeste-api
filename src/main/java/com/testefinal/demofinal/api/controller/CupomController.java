package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.CupomRequestDTO;
import com.testefinal.demofinal.api.DTO.CupomResponseDTO;
import com.testefinal.demofinal.application.service.CupomService;
import com.testefinal.demofinal.domain.exception.CupomNaoEncontradoException;
import com.testefinal.demofinal.domain.model.Cupom;
import com.testefinal.demofinal.infrastructure.repository.CupomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cupons")
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @PostMapping
    public ResponseEntity<Cupom> criarCupom(@RequestBody Cupom cupom) {
        Cupom cupomSalvo = cupomService.salvar(cupom);
        return ResponseEntity.status(HttpStatus.CREATED).body(cupomSalvo);
    }

    @GetMapping
    public ResponseEntity<List<CupomResponseDTO>> listarCupons() {
        //pega só os cupons válidos do banco
        List<Cupom> cuponsNoBanco = cupomService.listarCuponsValidos();

        //cria a lista vazia do DTO
        List<CupomResponseDTO> clienteLista = new ArrayList<>();

        //loop classico para converter
        for (Cupom c : cuponsNoBanco) {
            CupomResponseDTO dto = new CupomResponseDTO(
                    c.getId(),
                    c.getCodigo(),
                    c.getTipoDesconto(),
                    c.getValor(),
                    c.getValidade()
            );
            clienteLista.add(dto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteLista);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Cupom> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(cupomService.buscarPorCodigo(codigo));
    }



}
