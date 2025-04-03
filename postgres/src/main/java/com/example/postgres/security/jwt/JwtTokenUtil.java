package com.example.postgres.security.jwt;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.example.postgres.entity.Teacher;
import com.example.postgres.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;



@Component
public class JwtTokenUtil {

    private final String jwtSecret = "Aether#@!123Oct2022TimeTracker";
    private final String jwtIssuer = "aethertimetracker";

    public String generateAccessToken(Teacher person) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret.getBytes());

        List<String> authorities = new ArrayList<>();

        authorities.add(person.getRoles().name());
        return JWT.create()
                .withSubject(String.format("%s,%s", person.getId(), person.getUsername() + "," + person.getMobileNo()))
                .withIssuer(jwtIssuer).withClaim("roles", authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + (120 * 60 * 1000))).sign(algorithm);
    }

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Autherization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) return bearerToken.substring(7);
        return null;

    }

    public String generateTokenFromUsername(UserDetails userDetail) {
        String userName = userDetail.getUsername();
        return Jwts.builder().setSubject(userName).setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + 100000)).signWith(key()).compact();

    }

    public String getUserNameFromJwtToken(){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJwt("komal").getBody().getSubject();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode("everestAbacus"));
    }

    public String generateRefreshToken(Teacher person) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret.getBytes());

        return JWT.create()
                .withSubject(String.format("%s,%s", person.getId(), person.getEmail() + "," + person.getMobileNo()))
                .withIssuer(jwtIssuer).withExpiresAt(new Date(System.currentTimeMillis() + (180 * 60 * 1000)))
                .sign(algorithm);
    }

    public boolean validate(String token) {
        try {
            Long expiresAt = JWT.decode(token).getExpiresAt().getTime();
            if (expiresAt >= new Date().getTime()) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(String.format("JWT is invalid - {%s}", e.getMessage()));
        }

        return false;
    }

    public String getUserName(String token) {
        String subject = JWT.decode(token).getSubject();

        return subject.split(",")[1];
    }

    public String getMobileNo(String token) {
        String subject = JWT.decode(token).getSubject();

        return subject.split(",")[2];
    }

    public Integer getUserId(String token) {
        String subject = JWT.decode(token.replace("Bearer ", "")).getSubject();

        return Integer.parseInt(subject.split(",")[0]);
    }

    public Boolean validateToken(String token, String userName) {
        try {
            token = token.replace("Bearer ", "");
            final String username = getUserName(token);
            final String mobileNo = getMobileNo(token);
            if ((username.equals(userName) || mobileNo.equals(userName)) && validate(token)) {
                return Boolean.TRUE;
            } else {
                //throw new DataNotFound(AetherMessageEnum.INVALID_CREDENTIALS.getMessage());
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }
}