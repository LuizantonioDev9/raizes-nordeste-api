package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.CupomResponseDTO;
import com.testefinal.demofinal.application.service.CupomService;
import com.testefinal.demofinal.domain.model.Cupom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cupom> criarCupom(@RequestBody Cupom cupom) {
        Cupom cupomSalvo = cupomService.salvar(cupom);
        return ResponseEntity.status(HttpStatus.CREATED).body(cupomSalvo);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<List<CupomResponseDTO>> listarCupons() {
        List<Cupom> cuponsNoBanco = cupomService.listarCuponsValidos();


        List<CupomResponseDTO> clienteLista = new ArrayList<>();

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

//    @GetMapping("/{codigo}")
//    public ResponseEntity<Cupom> buscarPorCodigo(@PathVariable String codigo) {
//        return ResponseEntity.ok(cupomService.buscarPorCodigo(codigo));
//    }



}
