package com.tictactoebackend.projectapi;

import java.util.Arrays;

import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.tictactoebackend.projectapi.filters.AuthFilter;
import com.tictactoebackend.projectapi.services.userService;
import com.tictactoebackend.projectapi.userRepository.userRepository;


@SpringBootApplication
public class ProjectApiApplication {

    private static final String SQL_CREATE = "INSERT INTO public.users (id, username, email, password, role, healthdata) " +
                                          "VALUES (?, ?, ?, ?, ?, ?)";

    @Autowired
     JdbcTemplate jdbcTemplate;

    @Autowired
    userService Userservice;

    @Autowired
    userRepository Userrepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjectApiApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
    String[] preventedUrls = {"/api/users/getAll",  "/api/users/id/*", "/api/health/*", "/api/training/*"};
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns(preventedUrls);
		return registrationBean;
	}

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // config.addAllowedOrigin("*");
        // config.addAllowedOrigin("http://localhost:4200");
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        //config.setAllowedOriginPatterns("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> corsFilterBean = new FilterRegistrationBean<>(new CorsFilter(source));
        corsFilterBean.setOrder(0);
        return corsFilterBean;
    }


    @Bean
    public ApplicationRunner adminUserInitializer() {
        String password = "admin";
        String username = "admin";
        String email = "admin@gmail.com";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        String uuid = UUID.randomUUID().toString();
        String Role = "admin";
        return args -> {
            // Check if the admin user already exists
            if (Userrepository.getCountByEmail("admin@gmail.com") > 0 ) {
                System.out.println("admin exists");

            }
            else {
                jdbcTemplate.update(SQL_CREATE, uuid, username, email, hashedPassword, Role,false);

            }
        };
    }

}
