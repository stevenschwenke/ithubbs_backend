package de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


class DomainUserDetailsServiceTest {

    @Test
    void encodeMe() {
        System.out.println(new BCryptPasswordEncoder().encode("mypassword"));
    }
}