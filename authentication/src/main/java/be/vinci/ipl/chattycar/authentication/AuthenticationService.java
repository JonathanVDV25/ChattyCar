package be.vinci.ipl.chattycar.authentication;

import be.vinci.ipl.chattycar.authentication.models.Credentials;
import be.vinci.ipl.chattycar.authentication.models.InsecureCredentials;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationRepository repository;

    private final Algorithm jwtAlgorithm;
    private final JWTVerifier jwtVerifier;
    public AuthenticationService(AuthenticationRepository repository, AuthenticationProperties properties) {
        this.repository = repository;
        this.jwtAlgorithm = Algorithm.HMAC512(properties.getSecret());
        this.jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
    }


    /**
     * Creates user credentials
     * @param credentials The credentials to create, with insecure password
     * @return True if the credentials were created, false if they already existed
     */
    public boolean createOne(InsecureCredentials credentials) {
        if (repository.existsById(credentials.getEmail())) return false;
        String hashedPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
        repository.save(credentials.toCredentials(hashedPassword));
        return true;
    }

    /**
     * Updates user credentials
     * @param credentials The credentials to create, with insecure password
     * @return True if the credentials were updated, false if they couldn't be found
     */
    public boolean updateOne(InsecureCredentials credentials) {
        if (!repository.existsById(credentials.getEmail())) return false;
        String hashedPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
        repository.save(credentials.toCredentials(hashedPassword));
        return true;
    }

    /**
     * Deletes user credentials
     * @param email Email of the user credentials
     * @return True if the credentials were deleted, false if they couldn't be found
     */
    public boolean deleteOne(String email) {
        if (!repository.existsById(email)) return false;
        repository.deleteById(email);
        return true;
    }


    /**
     * Creates a JWT token for a user credentials
     * @param insecureCredentials The credentials
     * @return The token created, or null if the credentials were not valid
     */
    public String connect(InsecureCredentials insecureCredentials) {
        Credentials credentials = repository.findById(insecureCredentials.getEmail()).orElse(null);
        if (credentials == null) return null;
        if (!BCrypt.checkpw(insecureCredentials.getPassword(), credentials.getHashedPassword())) return null;
        return JWT.create().withIssuer("auth0").withClaim("email", credentials.getEmail()).sign(this.jwtAlgorithm);
    }


    /**
     * Verifies a JWT token
     * @param token The token
     * @return The email of the user in the token, or null if the token couldn't be decoded or the user couldn't be found
     */
    public String verify(String token) {
        try {
            String email = jwtVerifier.verify(token).getClaim("email").asString();
            if (!repository.existsById(email)) return null;
            return email;
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
