package com.example.omnichannelfinal.filter;

import com.example.omnichannelfinal.component.JwtTokenUtil;
import com.example.omnichannelfinal.entity.User;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request
            ,@NotNull HttpServletResponse response
            ,@NotNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if(isBypassToken(request)){
                filterChain.doFilter(request,response);
                return;
            }
            final String authHeader= request.getHeader("Authorization");
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
                return;
            }
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                final String token = authHeader.substring(7);
                final String userName= jwtTokenUtil.extractUsername(token);
                final String role= jwtTokenUtil.extractRoleName(token);
                if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){

                    User userDetails=(User) userDetailsService.loadUserByUsername(userName);
                    if(jwtTokenUtil.validateToken(token,userDetails)){
                        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken
                                (userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        HttpServletRequest requesttmp= request;
                        filterChain.doFilter(request,response);
                        return;
                    }
                }
            }
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
            return;
        }


    }

    private boolean isBypassToken(@NotNull HttpServletRequest request){
        final List<Pair<String,String>> byPassTokens= Arrays.asList(
//                Pair.of("/api/v1/products","GET"),
//                Pair.of("/test","GET"),
//                Pair.of("/api/v1/categories","GET"),
                Pair.of("/","GET"),
                Pair.of("/","POST"),
                Pair.of("/","PUT"),

                Pair.of("/omni-chat","GET"),
                Pair.of("/api/v1/users/login","POST"),
                Pair.of("/api/v1/users/register","POST")
//                http://localhost:8080/ShopBookPTD
        );
        for(Pair<String,String> bypassToken: byPassTokens){
            if(request.getServletPath().contains(bypassToken.getFirst()) && request.getMethod().equals(bypassToken.getSecond())){
                return true;
            }
        }
        return false;
    }
}