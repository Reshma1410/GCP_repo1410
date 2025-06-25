package org.example.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.example.common.BaseResponse;
import org.example.constants.ConstantCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {

    private String secretKey = "";

    public JWTService(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            System.out.println("sk -->"+sk);
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60*60*50))
                .and()
                .signWith(getKey())
                .compact();
//        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";
    }

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);

    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        try {
            return  Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();


        } catch (ExpiredJwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token has expired", ex);
        } catch (SignatureException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token", ex);
        }

    }


    public boolean validateToken(String token, UserDetails userDetails) {
       final String userName = extractUserName(token);
       return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }
}


















//Backup
//private BaseResponse extractAllClaims(String token) {
//    BaseResponse response = new BaseResponse();
//
//    try {
//        Claims claims = Jwts.parser()
//                .verifyWith(getKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//
//        response.setCode(ConstantCode.SUCCESS);
//        response.getData().put("claims", claims);
//
//        System.err.println("here is the response"+response.getData());
//        return response;
//    } catch (ExpiredJwtException ex) {
//        response.setCode(ConstantCode.UNAUTHORIZED);
//        response.setMessage("JWT token has expired");
//        response.setData(null);
//        return response;
//    } catch (SignatureException ex) {
//        response.setCode(ConstantCode.UNAUTHORIZED);
//        response.setMessage("Invalid JWT signature");
//        response.setData(null);
//        return response;
//    } catch (Exception ex) {
//        response.setCode(ConstantCode.UNAUTHORIZED);
//        response.setMessage("Invalid token");
//        response.setData(null);
//        return
//    }
//
//}
