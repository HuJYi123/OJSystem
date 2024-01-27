package com.ayi.ayiojbackenduserservice.config;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class DuplicateHeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setAccessControlAllowOrigin(removeDuplicateValues(Arrays.asList(response.getHeaders().getAccessControlAllowOrigin())));
        return chain.filter(exchange);
    }

    private String removeDuplicateValues(List<String> values) {
        // 使用Set去重
        Set<String> uniqueValues = new LinkedHashSet<>(values);
        return String.join(",", uniqueValues);
    }
}