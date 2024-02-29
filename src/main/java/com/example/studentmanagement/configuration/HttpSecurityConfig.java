package com.example.studentmanagement.configuration;

import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.security.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class HttpSecurityConfig {

    private final UserDetailService userDetailService;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests()

                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/userPictures/**").permitAll()

                .requestMatchers("/login").anonymous()
                .requestMatchers("/register").anonymous()

                .requestMatchers("/").authenticated()
                .requestMatchers("/students").authenticated()
                .requestMatchers("/teachers").authenticated()
                .requestMatchers("/lessons").authenticated()
                .requestMatchers("/logout").authenticated()

                .requestMatchers("/lessons/add").hasAuthority(UserType.TEACHER.name())
                .requestMatchers("/lessons/delete").hasAuthority(UserType.TEACHER.name())
                .requestMatchers("/lessons/update").hasAuthority(UserType.TEACHER.name())

                .requestMatchers("/lessons/startLesson").hasAuthority(UserType.STUDENT.name())
                .requestMatchers("/students/**").hasAuthority(UserType.STUDENT.name())

                .and()
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true"))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"));
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

}
