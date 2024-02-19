package com.se.onlinelibrary.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Some properties about JWT.
 * You can change the value in `application.properties`.
 *
 * @author LBW
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    private int validity;
    private String secret;
    public static String antMatchers;

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAntMatchers() { return antMatchers; }

    public void setAntMatchers(String antMatchers) { this.antMatchers = antMatchers; }

}
