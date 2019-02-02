package com.yishan.javaplus.extend.security;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Value;import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;import org.springframework.security.core.context.SecurityContextHolder;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.security.core.userdetails.UserDetailsService;import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;import org.springframework.stereotype.Component;import org.springframework.web.filter.OncePerRequestFilter;import javax.servlet.FilterChain;import javax.servlet.ServletException;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import java.io.IOException;@Componentpublic class JwtAuthenticationFilter extends OncePerRequestFilter {    private final Logger logger = LoggerFactory.getLogger(getClass());    @Autowired    private UserDetailsService userDetailsService;    @Autowired    private JwtTokenUtil jwtTokenUtil;    @Value("#{ShareProperties['jwt.header']}")    private String tokenHeader;    private String bear = "Bearer";    @Override    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {        String tokenHeader = request.getHeader(this.tokenHeader);        if(tokenHeader != null && tokenHeader.startsWith(bear)){            String authToken =tokenHeader.substring(7);            String username = jwtTokenUtil.getUsernameFromToken(authToken);            logger.info("checking authentication for user "+ username);            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){                UserDetails userDetails = userDetailsService.loadUserByUsername(username);                if(jwtTokenUtil.validateToken(authToken, userDetails)) {                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));                    logger.info("authenticated user " + username + ", setting security context");                    SecurityContextHolder.getContext().setAuthentication(authentication);                }else{                    logger.warn("token is no longer valid");                }            }            else {                logger.info("token doesn't container jwt bearer header");            }        } chain.doFilter(request, response);    }}