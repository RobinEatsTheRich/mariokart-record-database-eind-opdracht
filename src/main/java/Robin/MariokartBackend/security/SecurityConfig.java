package Robin.MariokartBackend.security;

import Robin.MariokartBackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig  {

        private final JwtService jwtService;
        private final UserRepository userRepository;

        public SecurityConfig(JwtService service, UserRepository userRepos) {
            this.jwtService = service;
            this.userRepository = userRepos;
        }

        @Bean
        public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
            var auth = new DaoAuthenticationProvider();
            auth.setPasswordEncoder(passwordEncoder);
            auth.setUserDetailsService(userDetailsService);
            return new ProviderManager(auth);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return new MyUserDetailsService(this.userRepository);
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.POST, "/users").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                            .requestMatchers(HttpMethod.GET, "/profiles").permitAll()
                            .requestMatchers(HttpMethod.GET, "/records").permitAll()
                            .requestMatchers(HttpMethod.POST, "/profiles").authenticated()
                            .requestMatchers(HttpMethod.POST, "/records").authenticated()
                            .requestMatchers(HttpMethod.GET, "/recordingData").authenticated()
                            .requestMatchers(HttpMethod.POST,"/kartParts","/characters","/courses").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/kartParts","/characters","/courses").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/kartParts","/characters","/courses").hasRole("ADMIN")
                            .requestMatchers("/*").permitAll()
                            .anyRequest().denyAll()
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(csrf -> csrf.disable())
                    .addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
    }


