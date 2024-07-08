package desmond.example.HNGProject2.util;

import desmond.example.HNGProject2.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


@Component
public class JwtUtil {


    @Value("${SHA_STRING}")
    private String SHA_STRING;

    private final Supplier<SecretKeySpec> getKey = () -> {
        Key key = Keys.hmacShaKeyFor(SHA_STRING
                .getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
    };


    private final Supplier<Date> expirationTime = () -> Date
            .from(LocalDateTime.now()
                    .plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant());


    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getKey.get())
                .build().parseSignedClaims(token).getPayload();
        return claimResolver.apply(claims);
    }

    public Function<String, String> extractUsername = token ->
            extractClaims(token, Claims::getSubject);
    private final Function<String, Date> extractExpirationDate = token ->
            extractClaims(token, Claims::getExpiration);


    public Function<String, Boolean> isTokenExpired = token -> extractExpirationDate.apply(token)
            .after(new Date(System.currentTimeMillis()));


    public BiFunction<String, String, Boolean> isTokenValid = (token, username) ->
            isTokenExpired.apply(token) && Objects.equals(extractUsername.apply(token), username);
    public Function<UserDetails, String> createJwt = userDetails -> {
        User user = (User) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        return Jwts.builder()
                .signWith(getKey.get())
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationTime.get())
                .compact();
    };


}

