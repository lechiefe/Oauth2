package com.knowledgebase.oauth;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().usersByUsernameQuery("select username,password, active from users where username=?")
				.authoritiesByUsernameQuery("SELECT username, crm_data_schema.roles.name FROM crm_data_schema.users "
						+ "INNER JOIN crm_data_schema.user_role "
						+ "ON crm_data_schema.users.id = crm_data_schema.user_role.user_id "
						+ "INNER JOIN crm_data_schema.roles "
						+ "ON crm_data_schema.user_role.role_id = crm_data_schema.roles.id " + " where username= ?;")
				.dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);

		System.out.println(
				"->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + bCryptPasswordEncoder.encode("123"));
		System.out.println("->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
				+ bCryptPasswordEncoder.encode("clientpassword"));
//		auth.inMemoryAuthentication().withUser("john").password("{noop}123").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
	}

}
