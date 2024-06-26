package br.com.henrique.avaliadorcredito.controllers;

import br.com.henrique.avaliadorcredito.exception.DadosClienteNotFoundException;
import br.com.henrique.avaliadorcredito.exception.ErroComunicacaoMicroservicesException;
import br.com.henrique.avaliadorcredito.exception.ErroSolicitacaoCartaoException;
import br.com.henrique.avaliadorcredito.models.*;
import br.com.henrique.avaliadorcredito.services.AvaliadorCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("avaliacao-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {
  private final AvaliadorCreditoService avaliadorCreditoService;

  @GetMapping(value = "situacaoCliente", params = "cpf")
  public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
    try {
      SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
      return ResponseEntity.ok(situacaoCliente);
    } catch (DadosClienteNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (ErroComunicacaoMicroservicesException e) {
      return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
    }

  }

  @PostMapping("/realizarAvaliacao")
  public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao) {
    try {
      List<CartaoAprovado> retornoAvaliacaoCliente = avaliadorCreditoService
        .realizarAvaliacao(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());
      return ResponseEntity.ok(retornoAvaliacaoCliente);
    } catch (DadosClienteNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (ErroComunicacaoMicroservicesException e) {
      return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
    }
  }

  @PostMapping("solicitarCartao")
  public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
    try {
      ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService
        .solicitarEmissaoCartao(dados);
      return ResponseEntity.ok(protocoloSolicitacaoCartao);
    } catch (ErroSolicitacaoCartaoException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }

  }
}
