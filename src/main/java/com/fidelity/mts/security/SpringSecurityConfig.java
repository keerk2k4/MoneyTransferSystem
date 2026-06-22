package com.fidelity.mts.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fidelity.mts.entity.Account;
import com.fidelity.mts.repo.AccountRepo;

@Configuration
public class SpringSecurityConfig {

	@Autowired
	private AccountRepo accountRepo;

	/**
	 * BCrypt password encoder Bean.
	 * Used to hash passwords and verify them during login.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Database-driven UserDetailsService.
	 *
	 * Instead of one hardcoded user in memory,
	 * this loads the user from the MySQL account table.
	 *
	 * How it works:
	 *   1. Angular sends: Authorization: Basic base64(username:password)
	 *   2. Spring Security decodes it → username, password
	 *   3. This method is called with that username
	 *   4. We look up the account by holderName in MySQL
	 *   5. Return the UserDetails so Spring can verify the password
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			// username here = what Angular sends as the username
			// in your app = the holderName of the account

			// Look up account in MySQL by holderName
			Account account = accountRepo
					.findByHolderName(username)
					.orElseThrow(() ->
						new UsernameNotFoundException(
							"No account found with holder name: " + username
						)
					);

			// Build Spring Security UserDetails from the account
			return User.builder()
					.username(account.getHolderName())  // login username
					.password(account.getPassword())    // BCrypt hash from DB
					.roles("USER")
					.build();
		};
	}

	/**
	 * CORS configuration — allows Angular (port 4200) to call the API.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	/**
	 * Security rules.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(auth ->
				auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.anyRequest().authenticated()
		);
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}
}
