package org.eshishkin.edu.sender.service;

import io.micrometer.core.annotation.Timed;
import java.security.KeyPairGenerator;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class LoadGenerationService {

    @Timed(value = "very_long_task", longTask = true)
    public void heavyTask() {
        generateLargeRsaKey(18192);
    }

    @SneakyThrows
    private void generateLargeRsaKey(int length) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(length);
        kpg.generateKeyPair();
    }
}
