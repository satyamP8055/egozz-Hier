package com.hss.egoz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * @author Satyam Pandey
 * Programmatic Configuration for Dispatcher Servlet of Spring Web MVC
 * */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

	/*
	 * To Manage Resource handling for the URL patterns i.e. not to be looked up in
	 * the other controllers...
	 **/
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/files/**").addResourceLocations("/res/");
		registry.addResourceHandler("/egozz/files/**").addResourceLocations("/res/");
		registry.addResourceHandler("/egozz/**").addResourceLocations("/");
	}


	/*
	 * To automatically redirect blank URLs to index.html without looking up in the
	 * controllers...
	 **/
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
		WebMvcConfigurer.super.addViewControllers(registry);
	}

}
