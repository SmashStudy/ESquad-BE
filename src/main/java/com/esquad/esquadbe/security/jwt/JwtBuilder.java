package com.esquad.esquadbe.security.jwt;

import io.jsonwebtoken.ClaimsMutator;

public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
    JwtBuilder claims(String name, Object value);
}
