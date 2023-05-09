package com.wccgroup.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class ChallengeApplication extends SpringBootServletInitializer {

    public static final Locale locale = Locale.forLanguageTag("id-ID");

    public static void main(String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }


    @Bean
    public LocaleResolver defaultLocaleResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(locale);
        return resolver;
    }
}
