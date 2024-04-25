package com.example.omnichannelfinal.component;


import com.example.omnichannelfinal.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.InvalidParameterException;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final Long expiration = 2592000L;
    private static final String secretKey="uqxTl0e8tPKWV9ZhLvP5zxCOQuHvYK6TYLox8KL7RTc=";

    public String generateToken(User user) throws  Exception{
        Map<String,Object> claims = new HashMap<>();
        claims.put("userName",user.getUsername());
        claims.put("role",user.getListRole());
        try {
            String token= Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e){
            System.err.println("Cannot create Jwt token, error: "+e.getMessage());
            throw new InvalidParameterException("Cannot create Jwt token, error: "+e.getMessage());
        }
    }
    private Key getSignInKey(){
        byte[] bytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }


    // code chạy 1 lần để tạo ra secretKey
//    public String generateSecretkey(){
//        SecureRandom random = new SecureRandom();
//        byte [] keyBytes = new byte[32];
//        random.nextBytes(keyBytes);
//        String secreKey= Encoders.BASE64.encode(keyBytes);
//        return secreKey;
//    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token,Claims :: getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims:: getSubject);
    }

    public String extractRoleName(String token){
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    public boolean validateToken(String token, UserDetails userDetails){
        String userName= extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


}