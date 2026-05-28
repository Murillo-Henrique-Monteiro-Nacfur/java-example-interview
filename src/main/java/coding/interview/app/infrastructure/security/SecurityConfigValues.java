package coding.interview.app.infrastructure.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@Getter
public class SecurityConfigValues {

    @Value("classpath:security/app.key")
    private RSAPrivateKey privateTokenSecretFile;
    @Value("classpath:security/app.pub")
    private RSAPublicKey publicTokenSecretFile;
}
