package com.duboribu.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
@EnableJpaAuditing
@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuditorAware<String> auditorProvider() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(null == authentication || !authentication.isAuthenticated()) {
			return null;
		}

		//사용자 환경에 맞게 로그인한 사용자의 정보를 불러온다.
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
		UserDetails user = (UserDetails) token.getPrincipal();
		return () -> Optional.of(user.getUsername());
	}
}
