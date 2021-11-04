package ZMetalHeartY.demo.Config;

import ZMetalHeartY.demo.Service.ZUserDetailsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    ZUserDetailsService zUserDetailsService;
    @Autowired
    ZSecurityMetadataSource zSecurityMetadataSource;
    @Autowired
    ZAccessDecisionManager zAccessDecisionManager;
    @Autowired
    ZAccessDeniedHandler zAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(zUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/getKaptchaImage","/static/layuiadmin/**","/layuiadmin/**","/emoji/**",
                "/images/**","/js/**","/css/**","/fonts/**","/favicon.ico","/static/**","/close",
                "/WeChat/**","/login","/","/csrf","/csrf/","/v2/api-docs", "/swagger-resources/configuration/ui",
                "/swagger-resources", "/swagger-resources/configuration/security",
                "/swagger-ui.html", "/webjars/**", "/file/**", "/user/register","/map/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(zSecurityMetadataSource);
                        object.setAccessDecisionManager(zAccessDecisionManager);
                        return object;
                    }
                })
                .antMatchers("/WeChat/issue/getIssueByKilometers").permitAll()
                .anyRequest().authenticated()

                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login").permitAll().invalidateHttpSession(true).clearAuthentication(true)
                .and().httpBasic()
                .and().headers().frameOptions().sameOrigin()
                .and()
                .exceptionHandling().accessDeniedHandler(zAccessDeniedHandler)
                .and().formLogin().loginPage("/login").loginProcessingUrl("/login_p").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")

                .successHandler(new AuthenticationSuccessHandler() {
                    //用户名和密码正确执行
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (principal != null && principal instanceof UserDetails) {
                            httpServletResponse.setContentType("application/json;charset=utf-8");
                            PrintWriter out = httpServletResponse.getWriter();
                            JSONObject json = new JSONObject();
                            out.write("{\"status\":\"ok\",\"message\":\"登录成功\"}");
                            out.flush();
                            out.close();
                        }
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    //用户名密码错误执行
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write("{\"status\":\"error\",\"message\":\"用户名或密码错误\"}");
                        out.flush();
                        out.close();
                    }
                });




    }

}
