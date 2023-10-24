package my.project.providers.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;

/*
**
* {@link SigningKeyResolverAdapter} implementation for RSA and HMAC
*/
class DefaultKeyResolver extends SigningKeyResolverAdapter {

    String getSigningKey() {
        return JwtSigningKey.getInstance();
    }

    @Override
    public Key resolveSigningKey(@SuppressWarnings("rawtypes") final JwsHeader jwsHeader, final Claims claims) {
        final SignatureAlgorithm algorithm = SignatureAlgorithm.valueOf(jwsHeader.getAlgorithm());
        final String secret = getSigningKey();
        if (StringUtils.isEmpty(secret)) {
            throw new SecurityException("Error with authentication secret.");
        }
        if (algorithm.isRsa()) {
            try {
                byte[] publicBytes = Base64.decodeBase64(secret);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(keySpec);
            }
            catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new SecurityException(e);
            }
        }
        else if (algorithm.isHmac()) {
            return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithm.getJcaName());
        }

        throw new UnsupportedOperationException(
                String.format("Unsupported authentication token received. %s key resolving has not been implemented.",
                        jwsHeader.getAlgorithm()));
    }
}