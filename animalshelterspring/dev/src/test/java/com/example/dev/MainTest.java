package com.example.dev;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @BeforeEach
    public void clearSystemProperties() {
        System.clearProperty("DB_URL");
        System.clearProperty("DB_USERNAME");
        System.clearProperty("DB_PASSWORD");
    }

    @Test
    public void testEnvironmentVariablesAreLoadedAndSet() {
        try (MockedStatic<Dotenv> mockedDotenv = mockStatic(Dotenv.class)) {
            Dotenv dotenv = mock(Dotenv.class);
            when(Dotenv.load()).thenReturn(dotenv);

            when(dotenv.get("DB_URL")).thenReturn("jdbc:mysql://localhost:3306/testdb");
            when(dotenv.get("DB_USERNAME")).thenReturn("testuser");
            when(dotenv.get("DB_PASSWORD")).thenReturn("testpassword");

            try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
                mockedSpringApplication.when(() -> SpringApplication.run(Main.class, new String[] {})).thenReturn(null);

                Main.main(new String[] {});

                assertEquals("jdbc:mysql://localhost:3306/testdb", System.getProperty("DB_URL"));
                assertEquals("testuser", System.getProperty("DB_USERNAME"));
                assertEquals("testpassword", System.getProperty("DB_PASSWORD"));

                mockedDotenv.verify(() -> Dotenv.load(), times(1));
                verify(dotenv).get("DB_URL");
                verify(dotenv).get("DB_USERNAME");
                verify(dotenv).get("DB_PASSWORD");
                mockedSpringApplication.verify(() -> SpringApplication.run(Main.class, new String[] {}), times(1));
            }
        }
    }
}
