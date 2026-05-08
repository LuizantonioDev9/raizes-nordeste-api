package com.testefinal.demofinal.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 409 - REGRA DE NEGÓCIO (Aqui entra Estoque Insuficiente, Canal Desativado, etc)
    @ExceptionHandler({NegocioException.class})
    public ResponseEntity<Map<String, Object>> tratarNegocio(NegocioException ex, HttpServletRequest request) {
        // Pega o nome da classe e deixa em caixa alta (ex: ESTOQUE_INSUFICIENTE)
        String erroNome = ex.getClass().getSimpleName().replace("Exception", "").replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();

        // MUDAMOS AQUI: De BAD_REQUEST para CONFLICT (409) conforme o roteiro
        return montarErro(HttpStatus.CONFLICT, erroNome, ex.getMessage(), request);
    }

    // 409 - CONFLITO DE DADOS (E-mail duplicado, etc)
    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<Map<String,Object>> tratarConflito(ConflitoException ex, HttpServletRequest request) {
        return montarErro(HttpStatus.CONFLICT, "CONFLITO_DE_DADOS", ex.getMessage(), request);
    }

    // 401 - NÃO AUTORIZADO
    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<Map<String,Object>> tratarLogin(NaoAutorizadoException ex, HttpServletRequest request) {
        return montarErro(HttpStatus.UNAUTHORIZED, "NAO_AUTORIZADO", ex.getMessage(), request);
    }

    // 404 - NÃO ENCONTRADO
    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Map<String,Object>> tratarNaoEncontrado(NaoEncontradoException ex, HttpServletRequest request) {
        return montarErro(HttpStatus.NOT_FOUND, "NAO_ENCONTRADO", ex.getMessage(), request);
    }

    // 400 - ERRO DE VALIDAÇÃO / LEITURA (Aqui entra o Canal de Pedido Inválido "ZAP")
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> tratarValidacao(Exception ex, HttpServletRequest request) {
        String msg = "O corpo da requisição está malformado ou contém valores inválidos.";

        // Se for erro de @Valid, pega a mensagem específica do campo
        if (ex instanceof MethodArgumentNotValidException validEx) {
            msg = validEx.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }

        return montarErro(HttpStatus.BAD_REQUEST, "ERRO_VALIDACAO", msg, request);
    }


    private ResponseEntity<Map<String,Object>> montarErro(HttpStatus status, String erro, String msg, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", erro);
        body.put("message", msg);
        body.put("path", request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}