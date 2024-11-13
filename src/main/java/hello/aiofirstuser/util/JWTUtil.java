package hello.aiofirstuser.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JWTUtil {
    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret){
        secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    public String generateToken(Map<String,Object> valueMap, int min){

        return Jwts.builder()
                .setHeader(Map.of("typ","JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public Map<String,Object> validationToken(String token){
        Map<String,Object> claim = null;

        try {

            claim = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJSWTException("MalFormed");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJSWTException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJSWTException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJSWTException("JWTError");
        } catch (Exception e) {
            throw new CustomJSWTException("Error");
        }

        return claim;
    }

}
