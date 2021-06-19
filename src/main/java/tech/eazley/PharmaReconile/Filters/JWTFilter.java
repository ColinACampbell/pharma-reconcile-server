package tech.eazley.PharmaReconile.Filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.eazley.PharmaReconile.Models.AppUserDetails;
import tech.eazley.PharmaReconile.Services.AppUserDetailsService;
import tech.eazley.PharmaReconile.Util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    AppUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        String email = null;
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer"))
        {
            String[] headerComponents = authHeader.split(" ");
            token = headerComponents[1];
            email = jwtUtil.extractEmail(token);
        }

        if (email!=null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            AppUserDetails principalUserDetails =  userDetailsService.loadUserByUsername(email);
            if (jwtUtil.validateToken(token,principalUserDetails))
            {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(principalUserDetails,null,principalUserDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
