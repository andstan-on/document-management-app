package com.springframework.documentmanagementapp.bootstrap;

import com.springframework.documentmanagementapp.repositories.DocumentRepository;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BootstrapDataTest {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(documentRepository, userRepository);
    }

    @Test
    void Testrun() throws  Exception{

        bootstrapData.run(null);

        assertThat(documentRepository.count()).isEqualTo(3);
        assertThat(userRepository.count()).isEqualTo(3);

    }
}