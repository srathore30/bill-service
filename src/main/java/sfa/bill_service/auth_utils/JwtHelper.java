package sfa.bill_service.auth_utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sfa.bill_service.constants.UserRole;
import sfa.bill_service.entities.UserEntity;
import sfa.bill_service.repositories.UserRepo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
@Component
public class JwtHelper {

    @Autowired
    private UserRepo userRepo;

    @Value("${jwt.tokenValidityInSeconds}")
    public long JWT_TOKEN_VALIDITY ;

    private final String secret = "ABCDEFGHIJfVIVEKghklKLMNNDHDNDO01234persisjpandeydcjsdcknsjdt5PQRSUVWXYZabcdemnouvwxyz664565665178-_"; // secret code
    byte[] secretKeyBytes = secret.getBytes();

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKeyBytes)).build().parseClaimsJws(token).getBody();
    }
    public List<UserRole> getUserRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKeyBytes)).build().parseClaimsJws(token).getBody();
        List<String> roles = claims.get("userRole", List.class);
        return roles.stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toList());
    }
    private Boolean isTokenExpired(String token) {                                      // checking expire
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        System.out.println(userDetails.getUsername());
        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        claims.put("name",user.getName());
        claims.put("userRole",user.getUserRoleList());
        claims.put("username",user.getUsername());
        claims.put("status",user.getUserStatus());
        claims.put("mobile",user.getMobileNo());
        claims.put("id", user.getId());
        return doGenerateToken(claims, userDetails.getUsername());
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()),SignatureAlgorithm.HS512).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public Boolean validateOnlyToken(String token) {
        return !isTokenExpired(token);
    }

}
