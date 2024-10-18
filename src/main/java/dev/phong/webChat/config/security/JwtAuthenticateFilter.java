package dev.phong.webChat.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.phong.webChat.common.ServiceError;
import dev.phong.webChat.common.ServiceResponse;
import dev.phong.webChat.handler.exceptionHandler.exeption.ForbiddenException;
import dev.phong.webChat.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isPermitted(request)){
            filterChain.doFilter(request,response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try{
            if(!StringUtils.isEmpty(authHeader) && StringUtils.startsWith(authHeader,"Bearer ")){
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }
            else{
                throw new ForbiddenException("Yêu cầu xác thực");
            }
        }catch (ForbiddenException exception){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        new ServiceResponse(
                                new Date(),
                                new ServiceError(HttpStatus.FORBIDDEN.value(),exception.getMessage()),
                                null
                        )
                )
            );
            return;
        }catch (Exception ex){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(
                    new ObjectMapper().writeValueAsString(
                            new ServiceResponse(
                                    new Date(),
                                    new ServiceError(HttpStatus.BAD_REQUEST.value(),ex.getMessage()),
                                    null
                            )
                    )
            );
            return;
        }

        if(StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
            if(userDetails == null){
                log.error("Phiên đăng nhập hết hạn.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                new ServiceResponse(
                                        new Date(),
                                        new ServiceError(HttpStatus.UNAUTHORIZED.value(),"Phiên đăng nhập hết hạn."),
                                        null
                                )
                        )
                );
                return;
            }
            else if(!userDetails.isEnabled()){
                log.error("Tài khoản đã đăng xuất.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                new ServiceResponse(
                                        new Date(),
                                        new ServiceError(HttpStatus.UNAUTHORIZED.value(),"Tài khoản đã đăng xuất."),
                                        null
                                )
                        )
                );
                return;
            }
            //---- check valid token -----
            if(jwtService.isTokenValid(token,userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken access_token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                access_token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(access_token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request,response);
    }

    private boolean isPermitted(HttpServletRequest request) {
        return request.getServletPath().contains("swagger")
                        || request.getServletPath().contains("/api-docs")
                        || request.getServletPath().contains("/auth/login")
                        || request.getServletPath().contains("/webChat")
                        || request.getServletPath().contains("/eSoft")
                        || (request.getServletPath().contains("/question") && request.getMethod().equals("GET"));
    }
}