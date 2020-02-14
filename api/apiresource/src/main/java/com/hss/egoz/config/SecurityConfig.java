package com.hss.egoz.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
 * @author Satyam Pandey
 * NOTE : No user credentials are defined as the application here is supposed to have NO UI OPERATIONS AVAILABLE
 * Configuration for Spring Security...
 * */
@Configurable
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/login").permitAll().antMatchers("/api/**").permitAll().antMatchers("/actuator/**").permitAll()
				.antMatchers("/files/**").permitAll().antMatchers("/egozz/**").permitAll().antMatchers("/**")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and()
				.logout().invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/logout-success")
				.permitAll();

	}

}
