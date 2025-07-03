import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtGenerator {

    private final SecretKey secretKey;

    public JwtGenerator(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(String email) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 3600_000)) // 1 hora
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
