package com.tictactoebackend.projectapi.filters;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.tictactoebackend.projectapi.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class AuthFilter extends GenericFilterBean{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException , ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        Enumeration<String> headerNames = httpRequest.getHeaderNames();
while (headerNames.hasMoreElements()) {
    String headerName = headerNames.nextElement();
    String headerValue = httpRequest.getHeader(headerName);
    System.out.println("Header: " + headerName + " - Value: " + headerValue);
}

    System.out.println(httpRequest);

        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("authHeader" + authHeader);
        if(authHeader != null) {
            String[] authHeaderArray = authHeader.split("Bearer ");
            if(authHeaderArray.length > 1 && authHeaderArray[1] != null) {
                String token = authHeaderArray[1];
                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.MY_SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
                    String userId = claims.get("id").toString();
                    httpRequest.setAttribute("id",claims.get("id").toString());


                    String[] uriParts = httpRequest.getRequestURI().split("/");
                    System.out.println(uriParts + "uriParts");
                    String requestedUserIdData = uriParts[uriParts.length - 1];
                    String requestedUserIdUpdate = uriParts[uriParts.length - 2];
                    String requestedUserParam = uriParts[uriParts.length - 3];

                    boolean isAdmin = false;

                    // Get roles from JWT claims
                    String role = claims.get("role").toString();
                    if (role != null) {
                    isAdmin = role.contains("admin");
                    }

                    if (isAdmin || userId.equals(requestedUserIdData) || userId.equals(requestedUserIdUpdate) || userId.equals(requestedUserParam)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    } else {
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Insufficient permissions");
                    }


                } catch (Exception e) {
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/Expired token");
                    return;
                }
            }
            else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization must be Bearer [token]");
                return;
            }
        } else {

            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token needs to be provided");
            return;
        }


        //filterChain.doFilter(servletRequest, servletResponse);
    }

}
