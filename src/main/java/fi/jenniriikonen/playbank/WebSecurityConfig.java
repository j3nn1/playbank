package fi.jenniriikonen.playbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import fi.jenniriikonen.playbank.web.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
//Enables authorization also methods (delete can't be executed with url as user)
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //Able to save users to database
	@Autowired
    private UserDetailServiceImpl userDetailsService;	
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        //All users access endpoints
        .authorizeRequests().antMatchers("/css/**").permitAll() // Enable css when logged out
        .and()
        //Signup page must be available without authentication
        .authorizeRequests().antMatchers("/signup", "/saveuser").permitAll()
        //Authoritized users only endpoints
        .and()
        	//Only admin
        .authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN") 
        .and()
        .authorizeRequests().anyRequest().authenticated()
        .and()
      .formLogin()
      		//Own login page, not default
      	  .loginPage("/login")
          .defaultSuccessUrl("/playlist")
          .permitAll()
          .and()
      .logout()
          .permitAll();
    }
    
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	//Able to save users to database. Password is hashed
    	auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}