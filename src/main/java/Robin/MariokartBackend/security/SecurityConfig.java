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
                            .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                            .requestMatchers(HttpMethod.GET, "/characters","/courses","/kartParts","/profiles","/records","/users").permitAll()
                            .requestMatchers(HttpMethod.GET, "/characters/{id}","/courses/{id}","/kartParts/{id}","/profiles/{id}","/records/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/records/{id}/recording").permitAll()
                            .requestMatchers(HttpMethod.POST, "/records/").authenticated()
                            .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()
                            .requestMatchers(HttpMethod.POST, "/records/").authenticated()
                            .requestMatchers(HttpMethod.GET, "/records/rivals_only").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/users/{id}","/profiles/{id}","/records/{id}").authenticated()
                            .requestMatchers(HttpMethod.DELETE, "/users/{id}","/profiles/{id}","/records/{id}").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/profiles/record/{id}").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/profiles/rival/{id}").authenticated()
                            .requestMatchers(HttpMethod.DELETE, "/profiles/rival/{id}").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/records/{id}/recording").authenticated()
                            .requestMatchers(HttpMethod.DELETE, "/records/{id}/recording").authenticated()
                            .requestMatchers(HttpMethod.POST,"/kartParts","/characters","/courses").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/kartParts","/characters","/courses").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/kartParts","/characters","/courses").hasAuthority("ADMIN")
                            .anyRequest().denyAll()
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(csrf -> csrf.disable())
                    .addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }


    }


