package my.project.providers.jwt;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Static holder for the public key or secret used for validating JSON Web Tokens (JWTs) in the
 * application
 */
public class JwtSigningKey {

    static final String OAUTH_KEY_FILE_KEY = "auth.oauth-secret-or-public-key-file";
    static final String OAUTH_KEY_KEY = "auth.oauth-secret-or-public-key";

    /**
     * Helper function for formatting RSA keys
     */
    public static final Function<String, String> FORMAT = s -> s
            .replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "").trim();

    /**
     * Lazy holder pattern
     */
    private static class Secret {
        static final String INSTANCE = readSecret(ConfigProvider.getConfig());
    }

    /**
     * Static initializer for loading the auth secret
     * @param config configuration value access
     * @return the secret or key
     */
    static String readSecret(final Config config) {
        String secret = "";
        Optional<String> oauthKeyFile = config.getOptionalValue(OAUTH_KEY_FILE_KEY, String.class);
        Optional<String> oauthKey = config.getOptionalValue(OAUTH_KEY_KEY, String.class);
        if (oauthKeyFile.isPresent()) {
            try {
                secret = Files.readString(Paths.get(oauthKeyFile.get()));
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        else if (oauthKey.isPresent()) {
            secret = oauthKey.get();
        }

        return FORMAT.apply(secret);
    }

    /**
     * The instance wide auth signing key
     * @return the key
     */
    public static String getInstance() {
        return Secret.INSTANCE;
    }
}
