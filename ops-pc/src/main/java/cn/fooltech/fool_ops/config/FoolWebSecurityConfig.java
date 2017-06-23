package cn.fooltech.fool_ops.config;

import javax.servlet.Filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cn.fooltech.fool_ops.component.security.FoolAccessDecisionManager;
import cn.fooltech.fool_ops.component.security.FoolAuthenticationProvider;
import cn.fooltech.fool_ops.component.security.FoolFilterSecurityInterceptor;
import cn.fooltech.fool_ops.component.security.FoolInvocationSecurityMetadataSource;
import cn.fooltech.fool_ops.component.security.FoolLoginFailureHandler;
import cn.fooltech.fool_ops.component.security.FoolLoginSuccessHandler;
import cn.fooltech.fool_ops.component.security.FoolUserDetailsService;
import cn.fooltech.fool_ops.component.security.PassLoginService;
import cn.fooltech.fool_ops.component.security.ValidateCodeAuthenticationFilter;
import cn.fooltech.fool_ops.domain.sysman.repository.ResourceRepository;

@Configuration
@EnableWebSecurity
public class FoolWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${cn.fooltech.fool_ops.emptyValidatecode}")
	private boolean allowEmptyValidateCode = false;
	
	@Value("${cn.fooltech.fool_ops.debug.passLogin}")
	private boolean passLogin = false;
	
	@Value("${cn.fooltech.fool_ops.debug.loginName}")
	private String defaultLoginName = "";
	
	@Value("${cn.fooltech.fool_ops.debug.password}")
	private String defaultPassword = "";

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ResourceRepository resRepo;



	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/v2/api-docs/**").permitAll()
				.antMatchers("/registerController/**").permitAll()
				.antMatchers("/message/refreshPush").permitAll()
				.antMatchers("/swagger-resources/configuration/ui").permitAll()
				.antMatchers("/resources/**", "/ValidateCode", "/login**","/api/**","/error/**").permitAll().anyRequest().authenticated()
				.and().addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
				.addFilterBefore(validateCodeFilter(), UsernamePasswordAuthenticationFilter.class).exceptionHandling().accessDeniedPage("/error/403")
				.authenticationEntryPoint(loginEntryPoint()).and().formLogin().disable()
				
				/*
				 * .loginPage("/login") .failureUrl("/login?error=1")
				 * .defaultSuccessUrl("/main/index")
				 * .passwordParameter("password") .usernameParameter("username")
				 * .permitAll() .and()
				 */
				.logout().logoutSuccessUrl("/login").permitAll().and().csrf().disable().headers().frameOptions()
				.sameOrigin().and();
				//.apply(securityConfigurerAdapter());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());

	}

	@Bean
	public ValidateCodeAuthenticationFilter validateCodeFilter() {
		ValidateCodeAuthenticationFilter filter = new ValidateCodeAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager);
		filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		filter.setAuthenticationSuccessHandler(successHandler());
		filter.setAuthenticationFailureHandler(failureHandler());
		filter.setUsernameParameter("username");
		filter.setPasswordParameter("password");
		filter.setAllowEmptyValidateCode(allowEmptyValidateCode);
		return filter;
	}

	@Bean
	public FoolLoginSuccessHandler successHandler() {
		return new FoolLoginSuccessHandler("/main/index");
	}

	@Bean
	public FoolLoginFailureHandler failureHandler() {
		return new FoolLoginFailureHandler("/login?error=1");
	}

	@Bean
	public LoginUrlAuthenticationEntryPoint loginEntryPoint() {
		return new LoginUrlAuthenticationEntryPoint("/login");
	}

	@Bean
	public FoolAuthenticationProvider authenticationProvider() {
		FoolAuthenticationProvider provider = new FoolAuthenticationProvider();
		provider.setUserDetailService(userDetailsService());
		return provider;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new FoolUserDetailsService();
	}

	@Bean
	public AccessDecisionManager accessDecisionManager() {
		return new FoolAccessDecisionManager();
	}

	@Bean
	public FilterInvocationSecurityMetadataSource metedataSource() {
		FoolInvocationSecurityMetadataSource metedataSource = new FoolInvocationSecurityMetadataSource();
		metedataSource.setResRepo(resRepo);
		metedataSource.setPassLogin(passLogin);
		metedataSource.setPassLoginService(passLoginService());
		return metedataSource;
	}
	
	@Bean 
	public PassLoginService passLoginService(){
		PassLoginService pls = new PassLoginService();
		pls.setAuthenticationManager(authenticationManager);
		pls.setDefaultLoginName(defaultLoginName);
		pls.setDefaultPassword(defaultPassword);
		return pls;
	}

	@Bean
	public Filter filterSecurityInterceptor() {
		FoolFilterSecurityInterceptor filter = new FoolFilterSecurityInterceptor();
		filter.setAccessDecisionManager(accessDecisionManager());
		filter.setSecurityMetadataSource(metedataSource());
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


}