package com.horace.gateway.config;

import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author: Horace
 * @desc:
 * @project: spring-cloud-demos
 * @create: 2020-01-05 18:07
 */
@Configuration
public class SleuthConfiguration {

    @Autowired
    private Tracer tracer; //todo  为什么显示不能Autowired，实际上能Autowired

    //gateway 的过滤器 GlobalFilter
    @Bean
    public GlobalFilter customGlobalPostFilter() {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.just(exchange))
                .map(serverWebExchange -> {
                    serverWebExchange.getResponse().getHeaders().set("TxId", Long.toHexString(tracer.currentSpan()
                            .context().traceId()));
                    return serverWebExchange;
                }).then();
    }
}
