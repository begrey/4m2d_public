package com.samyeonyiduk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@CrossOrigin("*")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtil jwtTokenProvider;

    private final WebAccessDeniedHandler webAccessDeniedHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtUtil jwtTokenProvider, WebAccessDeniedHandler webAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.webAccessDeniedHandler = webAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable();


            http
                    .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // CORS preflight 요청은 인증처리를 하지 않겠다
                    .antMatchers( "/login").anonymous()
                    .antMatchers( "/login/getToken").anonymous()
                    //.antMatchers( "/api/").anonymous()
                    .antMatchers( "/4m2d/**").anonymous()
                    .antMatchers(HttpMethod.GET,"/api/posts/**").anonymous()
                    .antMatchers(HttpMethod.GET,"/api/users/**").anonymous()
                    .antMatchers(HttpMethod.GET,"/api/titles/**").anonymous()
                    .antMatchers("/api/posts/**").hasAuthority("ROLE_USER")
                    .antMatchers("/api/users/**").hasAuthority("ROLE_USER")
                    .antMatchers("/api/carts/**").hasAuthority("ROLE_USER")
                    .antMatchers("/api/titles/**").hasAuthority("ROLE_USER")
                    .antMatchers("/api/comments/**").hasAuthority("ROLE_USER")
                .anyRequest().permitAll()
                .and()
                .cors()
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(webAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    // CORS 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://www.4m2d.site/");
        configuration.addAllowedOrigin("http://4m2d.site/");
        configuration.addAllowedOrigin("http://api.4m2d.site/**");
        //configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
