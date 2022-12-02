package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //
@EnableWebSecurity
//WebSecurityConfigureAdapter를 상속받는 클래스에 @EnableWebSecurity 어노테이션을 선언하면
//SpringSecurityFilterChain이 자동으로 포함. WebSecurityConfigurerAdapter를 상속 받아서
//메소드 오버라이딩을 통해 보안 설정을 커스터마이징 할 수 있다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    //http 요청에 대한 보안을 설정. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정 작성
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin()
                //로그인 페이지 URl 설정
                .loginPage("/members/login")
                //로그인 성공시 이동할 URL
                .defaultSuccessUrl("/")
                //로그인 시 사용할 파라미터 이름을 email로 지정
                .usernameParameter("email")
                //로그인 실패시 이동할 url 설정
                .failureUrl("/members/login/error")
                .and()
                .logout()
                //로그아웃 url 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                //로그아웃 성공시 이동할 url
                .logoutSuccessUrl("/");
        //시큐리티 처리에 HttpServletRequest를 이용한다는 것
        http.authorizeRequests()
                .mvcMatchers("/","/members/**",
                                      //permitAll()을 통해 모든 사용자가 인증(로그인) 없이 해당 경로에 접근할 수 있도록 설정.
                                      //메인페이지, 회원 관련 URL, 뒤에서 만들 상품 상세페이지, 상품이미지를 불러오는 경로
                                      "/item/**", "/images/**").permitAll()
                // /admin으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우에만 접근 가능
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                //4
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint
                        //인증되지 않은 사용자가 리소스에 접근했을 때 수행되는 핸들러
                        (new CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        //static 디렉터리의 하위 파일은 인증을 무시하도록 설정
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Bean//직접 빈을 등록하기 위해선 @Configuration 및 @Bean 어노테이션을 사용한다.
    //라이브러리 혹은 내장 클래스 등 개발자가 직접 제어 불가능한 클래스의 경우 @Configuration 어노테이션과 @Bean 어노테이션을 사용하여 Bean으로 등록한다.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception{ //
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
