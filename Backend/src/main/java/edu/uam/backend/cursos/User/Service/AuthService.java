package edu.uam.backend.cursos.User.Service;

import edu.uam.backend.cursos.User.Model.User;
import edu.uam.backend.cursos.User.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private static final long EXPIRATION_TIME = 86400000; // 1 día en ms

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey jwtSecretKey;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SecretKey jwtSecretKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecretKey = jwtSecretKey;
    }

    public String login(String email, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Contraseña incorrecta");
        }

        return generarToken(user);
    }

    private String generarToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256) // 🔥 Uso correcto de clave segura
                .compact();
    }
}

