package sfa.bill_service.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UuidGenerator {
    private static final Random random = new Random();
    public static String generateUniqueId() {
        return String.valueOf(10000 + random.nextInt(90000)); // Generates a number between 10000-99999
    }
}