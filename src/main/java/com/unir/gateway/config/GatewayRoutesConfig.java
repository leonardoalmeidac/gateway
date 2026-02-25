package com.unir.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                // 📚 Transcripción para el catálogo
                                // Mapeamos /libros -> /api/v1/books
                                // Mapeamos /libros/books -> /api/v1/books
                                // Mapeamos /libros/{id} -> /api/v1/books/{id}
                                .route("libros-route-base", r -> r.path("/libros", "/libros/")
                                                .filters(f -> f.setPath("/api/v1/books"))
                                                .uri("lb://MS-BOOKS-CATALOGUE"))

                                .route("libros-route-all", r -> r.path("/libros/books/**")
                                                .filters(f -> f.rewritePath("/libros/books(?<segment>.*)",
                                                                "/api/v1/books${segment}"))
                                                .uri("lb://MS-BOOKS-CATALOGUE"))

                                .route("libros-route-detail", r -> r.path("/libros/{id}")
                                                .filters(f -> f.rewritePath("/libros/(?<id>.*)", "/api/v1/books/${id}"))
                                                .uri("lb://MS-BOOKS-CATALOGUE"))

                                // 💳 Transcripción para los pagos
                                .route("pagos-route-base", r -> r.path("/pagos", "/pagos/")
                                                .filters(f -> f.setPath("/api/v1/purchases"))
                                                .uri("lb://MS-BOOKS-PAYMENT"))

                                .route("pagos-route-detail", r -> r.path("/pagos/{id}")
                                                .filters(f -> f.rewritePath("/pagos/(?<id>.*)",
                                                                "/api/v1/purchases/${id}"))
                                                .uri("lb://MS-BOOKS-PAYMENT"))

                                // 📚 Rutas directas usadas por el Frontend
                                .route("direct-books", r -> r.path("/api/v1/books/**")
                                                .uri("lb://MS-BOOKS-CATALOGUE"))
                                .route("direct-purchases", r -> r.path("/api/v1/purchases/**")
                                                .uri("lb://MS-BOOKS-PAYMENT"))
                                .build();
        }
}
