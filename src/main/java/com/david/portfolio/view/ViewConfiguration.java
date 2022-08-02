package com.david.portfolio.view;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ViewConfiguration implements WebMvcConfigurer {
    
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("inicio").setViewName("inicio");
        registry.addViewController("sobre").setViewName("sobre");
        registry.addViewController("curriculo").setViewName("curriculo");
        registry.addViewController("contato").setViewName("contato");
        registry.addViewController("obrigado").setViewName("obrigado");
    }
}
