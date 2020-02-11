package com.phoebelord.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(
    ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/static/**")
      .addResourceLocations("/WEB-INF/view/client/build/static/");
    registry.addResourceHandler("/*.js")
      .addResourceLocations("/WEB-INF/view/client/build/");
    registry.addResourceHandler("/*.json")
      .addResourceLocations("/WEB-INF/view/client/build/");
    registry.addResourceHandler("/*.ico")
      .addResourceLocations("/WEB-INF/view/client/build/");
    registry.addResourceHandler("/index.html")
      .addResourceLocations("/WEB-INF/view/client/build/index.html");
  }

  @Bean
  public ViewResolver viewResolver() {
    final InternalResourceViewResolver bean = new InternalResourceViewResolver();

    bean.setViewClass(JstlView.class);
    bean.setPrefix("/WEB-INF/view/");
    bean.setSuffix(".jsp");

    return bean;
  }
}
