//package com.cts.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
//        UserDetails user = User.builder()
//            .username("user@gmail.com")
//            .password(passwordEncoder.encode("userpassword"))
//            .roles("USER")
//            .build();
//
//        UserDetails admin = User.builder()
//            .username("admin@gmail.com")
//            .password(passwordEncoder.encode("adminpassword")) 
//            .roles("ADMIN")
//            .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests((authz) -> authz
//            	
////            		.requestMatchers("/OMP/register").permitAll() 
//                    .requestMatchers("/OMP/*").permitAll() 
////                    .requestMatchers("/OMP/verify-email").permitAll() 
////                    .requestMatchers("/OMP/reset-password").permitAll() // Public reset password
////                    .requestMatchers("/OMP/*").permitAll()
//            		  
////                .requestMatchers("/OMP/viewUsersSubscribedToProduct").hasRole("ADMIN") 
////               .requestMatchers("/OMP/*").hasAnyRole( "USER")
//                     
//                .anyRequest().authenticated()
//            )
//            .httpBasic(withDefaults())
//            .csrf((csrf) -> csrf.disable()); 
//
//        return http.build();
//    }
//}


//package com.cts.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(Customizer.withDefaults())
//                .authorizeHttpRequests(authorize -> authorize
//                     .requestMatchers("/OMP/register").permitAll() 
//   //                     .requestMatchers("/OMP/admin/**").hasRole("ADMIN") 
//                     
//                     .requestMatchers("/OMP/profile").hasRole("User")
//                     .requestMatchers("/OMP/login","/OMP/dashboard").hasRole("ADMIN")
////                     .requestMatchers().hasRole("ADMIN")   
//                        .anyRequest().authenticated() 
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
//                .sessionManagement(Customizer.withDefaults());
//
//        return http.build();
//    }
//}


//package com.cts.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.core.GrantedAuthorityDefaults;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
//    	return new GrantedAuthorityDefaults(""); // Remove "ROLE_" prefix
//    }
//    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(Customizer.withDefaults())
//                .authorizeHttpRequests(authorize -> authorize
//                 
//                    
//                      
//                       .requestMatchers("/OMP/admin").hasRole("ADMIN")
//                       .requestMatchers("/OMP/login").hasRole("USER")
//                       .requestMatchers("/OMP/reset-password").hasRole("ADMIN")
//                       .requestMatchers("/OMP/viewAllProducts").hasRole("ADMIN")
//                       
////                        .requestMatchers("/OMP/profile").hasRole("USER") // Only USER role can access profile
//                        //.requestMatchers("/OMP/login").hasRole("ADMIN") // Only ADMIN can access login and dashboard
//   //                     .anyRequest().authenticated())// All other endpoints require authentication
//                		//.requestMatchers( "/OMP/profile").hasAnyRole("USER")
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
//                .sessionManagement(Customizer.withDefaults());
//
//        return http.build();
//    }
//    
//    
//}

//    package com.cts.config;
//    
//    import org.springframework.beans.factory.annotation.Autowired;
//    import org.springframework.context.annotation.Bean;
//    import org.springframework.context.annotation.Configuration;
//    import org.springframework.http.HttpMethod;
//    import org.springframework.security.authentication.AuthenticationProvider;
//    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//    import org.springframework.security.config.Customizer;
//    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//    import org.springframework.security.config.core.GrantedAuthorityDefaults;
//    import org.springframework.security.core.userdetails.UserDetailsService;
//    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//    import org.springframework.security.crypto.password.PasswordEncoder;
//    import org.springframework.security.web.SecurityFilterChain;
//    import org.springframework.web.cors.CorsConfiguration;
//    import org.springframework.web.cors.CorsConfigurationSource;
//    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//    import java.util.Arrays;
//    import java.util.Collections;
//    
//    @Configuration
//    @EnableWebSecurity
//    public class SecurityConfig {
//    
//        @Autowired
//        private UserDetailsService userDetailsService;
//    
//        @Bean
//        public AuthenticationProvider authenticationProvider() {
//            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//            provider.setUserDetailsService(userDetailsService);
//            provider.setPasswordEncoder(passwordEncoder());
//            return provider;
//        }
//    
//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder();
//        }
//    
//        @Bean
//        public CorsConfigurationSource corsConfigurationSource() {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:3000")); // Or your frontend's origin
//            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Include OPTIONS
//            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
//            configuration.setAllowCredentials(true);
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", configuration);
//            return source;
//        }
//    
//        @Bean
//        public GrantedAuthorityDefaults grantedAuthorityDefaults() {
//            return new GrantedAuthorityDefaults(""); 
//        }
//    
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
//                    .csrf(csrf -> csrf.disable()) 
//                    .authorizeHttpRequests(authorize -> authorize
//    
//    
////                            .requestMatchers("/OMP/admin").hasRole("ADMIN")
////                            .requestMatchers("/OMP/login").hasRole("USER")
////                            .requestMatchers("/OMP/reset-password").hasAnyRole("ADMIN","USER")
////                            .requestMatchers("/OMP/viewAllProducts").hasRole("ADMIN")
//                    		//.requestMatchers("/OMP/login","/OMP/reset-password","/OMP/generate-reset-link").permitAll()
//                    		
//                            .requestMatchers("/OMP/reset-password").hasAnyRole("ADMIN")
//                            .requestMatchers("/OMP/admin/**").hasAnyRole("ADMIN")
//    
//    
//                    )
//                    .httpBasic(Customizer.withDefaults())
//                    .formLogin(Customizer.withDefaults())
//                    .sessionManagement(Customizer.withDefaults());
//    
//            return http.build();
//        }
//    }
//   
//package com.cts.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:3000")); // Replace with your frontend origin
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(Customizer.withDefaults())
//                .authorizeHttpRequests(authorize -> authorize
//                		
//                        //.requestMatchers("/OMP/login", "/OMP/reset-password", "/OMP/generate-reset-link").permitAll() 
//      
//                        //.requestMatchers("/OMP/**").permitAll()  
//                        //.requestMatchers("/OMP/admin/**").hasRole("ADMIN") 
//                        .requestMatchers("/OMP/getUserIdByEmail").hasAnyRole("ADMIN")
//                        .anyRequest().authenticated() 
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
//                .sessionManagement(Customizer.withDefaults());
//
//        return http.build();
//    }
//}


package com.cts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                						.requestMatchers(HttpMethod.OPTIONS,"OMP/**").permitAll()
                                        .requestMatchers("/OMP/admin/**","OMP/viewUsersSubscribedToProduct","OMP/getAllUser").hasAuthority("ADMIN")
                                        .requestMatchers("/OMP/login","OMP/register","OMP/reset-password","OMP/generate-reset-link",
                                        		"OMP/viewAllProducts","/OMP/viewProductDetails/{productId}","/OMP/product/**","OMP/topSubscribedProduct",
                                        		"OMP/topRatedProducts","OMP/searchProductByName","OMP/searchProductBySubsCount","OMP/searchProductByRating","OMP/searchProductBySubsCountAndRating",
                                        		"OMP/searchProductByNameSubsRating","OMP/searchProductByNameAndRating","OMP/searchProductByNameAndSubsCount",
                                        		"/OMP/reviews/getSpecificProductReviews","/OMP/reviews/highestRatedReview","OMP/getUserIdByEmail",
                                        		"OMP/myDetails").permitAll()
                                        .requestMatchers("OMP/addSubscription","/OMP/removeSubscription","OMP/updateUser/{userId}",
                                        		"OMP/reviews/user/{userId}","OMP/viewSubscriptionList","OMP/getProductSubscriptionList").hasAnyAuthority("ADMIN","USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .authenticationProvider(authenticationProvider())
                .csrf((csrf) -> csrf.disable());

        return http.build();
    }
}
