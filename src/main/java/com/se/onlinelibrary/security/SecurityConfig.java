package com.se.onlinelibrary.security;

import com.se.onlinelibrary.security.filters.JwtAuthenticationFilter;
import com.se.onlinelibrary.security.filters.JwtLoginFilter;
import com.se.onlinelibrary.security.handlers.JwtAccessDeniedHandler;
import com.se.onlinelibrary.security.handlers.JwtAuthenticationEntryPoint;
import com.se.onlinelibrary.security.handlers.JwtLoginFailureHandler;
import com.se.onlinelibrary.security.handlers.JwtLoginSuccessFilter;
import com.se.onlinelibrary.service.userServices.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;

/**
 * @author LBW
 */
@Configuration
@EnableWebSecurity //启动SpringSecurity的web安全支持
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtLoginFilter jwtLoginFilter;
    @Autowired
    private JwtLoginSuccessFilter jwtLoginSuccessFilter;
    @Autowired
    private JwtLoginFailureHandler jwtLoginFailureHandler;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: you need to configure your http security. Remember to read the JavaDoc carefully.
//        设置登录拦截器成功失败Handler
        jwtLoginFilter.setAuthenticationSuccessHandler(jwtLoginSuccessFilter);
        jwtLoginFilter.setAuthenticationFailureHandler(jwtLoginFailureHandler);
        jwtLoginFilter.setFilterProcessesUrl("/login");
//        添加中文转码
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        http.addFilterBefore(encodingFilter, CsrfFilter.class);

        http

                .formLogin()
                    .loginPage("http://localhost:8081/login")
                    .passwordParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login").permitAll()
                    .and()
                .rememberMe() //记住我：实现自动登录
                .and()
                .authorizeRequests()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                //权限设置，一旦匹配到了就不继续匹配了，所以拦截规则的顺序不能写错
                .antMatchers("/superAdmin/**").hasRole("superAdmin")
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/login", "/register/**","/resources/**","/druid/**","/test/**","/account/**").permitAll()
                .anyRequest().authenticated() 
                //                .accessDecisionManager()?
                .and()
                .logout()
                //.logoutSuccessHandler(new MyLogoutSuccessHandler)
                .logoutUrl("/logout")
                .permitAll()
                .and()
                .csrf().disable() //csrf禁用，因为不使用session
                .cors() //支持跨域
                .and()  //设置Header，支持跨域、ajax请求
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                    new Header("Access-control-Allow-Origin","*"),
                    new Header("Access-Control-Expose-Headers","Authorization"))))
                // Make sure we use stateless session; session won't be used to store user's state.
                    .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                    .accessDeniedHandler(new JwtAccessDeniedHandler());

        http
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
//                登录前JwtAuthenticationFilter过滤
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

//    角色继承：admin能做的事，superAdmin都能做
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_SuperAdmin > ROLE_admin");
        return hierarchy;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    AuthenticationManagerBuilder 用于创建一个 AuthenticationManager，
//    实现内存验证、LADP验证、基于JDBC的验证、添加UserDetailsService、AuthenticationProvider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO: Configure your auth here. Remember to read the JavaDoc carefully.
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

//    配置哪些请求不拦截
    @Override
    public void configure(WebSecurity web) throws Exception {
        // Hint: Now you can view h2-console page at `http://IP-Address:<port>/h2-console` without authentication.
        web.ignoring().antMatchers("/h2-console/**");
    }

}
