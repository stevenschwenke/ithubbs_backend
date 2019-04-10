package de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


class DomainUserDetailsServiceTest {

    @Test
    void encodeMe() {
        System.out.println(new BCryptPasswordEncoder().encode("steven"));
        // $2a$10$UoSI1GfEiU1MTsqd/NL2Gur2nKU7Yrvxsce0GohoNiv91NcJvNCea
    }
}