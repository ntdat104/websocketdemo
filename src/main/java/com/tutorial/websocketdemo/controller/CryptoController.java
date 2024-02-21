package com.tutorial.websocketdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.websocketdemo.enums.Period;
import com.tutorial.websocketdemo.form.CryptoPriceRequest;
import com.tutorial.websocketdemo.service.CryptoService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/realtime")
    public ResponseEntity<?> getPriceByTickerAndPeriod(@RequestBody List<CryptoPriceRequest> requestList) {
        List<String> streams = requestList.stream().map(
                (c) -> c.getTicker().toLowerCase() + "@" + Period.getPeriodByTypeAndPeriod(c.getType(), c.getPeriod()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(cryptoService.getList(streams));
    }

}