package com.tutorial.websocketdemo.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tutorial.websocketdemo.data.BinanceKlineData;

@Service
public class CryptoService {
    
    private final Map<String, BinanceKlineData> storage = new LinkedHashMap<>();

    public void add(BinanceKlineData data) {
        if (data.getStream() != null) {
            storage.put(data.getStream(), data);
        }
    }

    public List<BinanceKlineData> getList(List<String> streams) {
        if (streams.size() > 0) {
            return streams.stream().map((c) -> {
                return storage.get(c);
            }).collect(Collectors.toList());
        }

        return null;
    }

}
