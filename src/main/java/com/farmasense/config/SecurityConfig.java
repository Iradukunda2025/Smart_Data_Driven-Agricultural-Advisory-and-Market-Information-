package com.farmasense.config;

import com.farmasense.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; // Note: For Spring Boot 2.7.x
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login?expired")
                .and()
                .invalidSessionUrl("/login?invalid")
                .sessionFixation().migrateSession()
                .and()
            .authorizeRequests()
                .antMatchers("/", "/index", "/signup", "/error", "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/*.jpg", "/*.png", "/*.webp", "/api/auth/**").permitAll()
                .antMatchers("/api/v1/market-prices", "/api/v1/market-prices/**").authenticated()
                .antMatchers("/admin/users/**").hasRole("ADMIN")
                .antMatchers("/admin/weather/**", "/admin/market/**", "/admin/advisories/**").hasAnyRole("ADMIN", "VENDOR")
                .antMatchers("/dashboard/admin/**").hasRole("ADMIN")
                .antMatchers("/dashboard/vendor/**", "/vendor/**").hasAnyRole("ADMIN", "VENDOR")
                .antMatchers("/dashboard/farmer/**", "/weather", "/market", "/dashboard", "/advisories").hasAnyRole("ADMIN", "VENDOR", "FARMER")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
                .and()
            .logout()
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
