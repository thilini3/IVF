package com.example.ivf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "external")
public record ExternalIntegrationProperties(
    Fhir fhir,
    Barcode barcode,
    Analytics analytics
) {

    public record Fhir(String baseUrl, @DefaultValue("false") boolean enabled) { }

    public record Barcode(String baseUrl, @DefaultValue("false") boolean enabled) { }

    public record Analytics(String baseUrl, @DefaultValue("false") boolean enabled) { }
}
