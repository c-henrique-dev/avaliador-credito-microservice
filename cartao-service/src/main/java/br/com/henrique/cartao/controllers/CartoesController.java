package br.com.henrique.cartao.controllers;

import br.com.henrique.cartao.dtos.CartaoDto;
import br.com.henrique.cartao.dtos.CartoesPorClienteDto;
import br.com.henrique.cartao.entitys.Cartao;
import br.com.henrique.cartao.services.CartaoService;
import br.com.henrique.cartao.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cartao")
@RequiredArgsConstructor
public class CartoesController {

  @Autowired
  private final CartaoService cartaoService;
  @Autowired
  private final ClienteService clienteCartaoService;

  @PostMapping("/create")
  public ResponseEntity cadastra(@Valid @RequestBody CartaoDto cartaoDto) {
    Cartao cartao = this.cartaoService.save(cartaoDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(cartao);
  }

  @GetMapping(path = "/getCartoesRendaAteh", params = "renda")
  public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda) {
    List<Cartao> lista = cartaoService.getCartoesRendaMenorIgual(renda);
    return ResponseEntity.ok(lista);
  }

  @GetMapping(path = "/getCartoesByCliente", params = "cpf")
  public ResponseEntity<List<CartoesPorClienteDto>> getCartoesByCliente(
    @RequestParam("cpf") String cpf) {
    List<CartoesPorClienteDto> resolveList = this.cartaoService.getCartoesByCliente(cpf);
    return ResponseEntity.ok(resolveList);

  }
}
