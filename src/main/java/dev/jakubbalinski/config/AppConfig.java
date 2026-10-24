package dev.jakubbalinski.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public record AppConfig(
        MailConfig mailConfig,
        Boolean dryRun
) {
    private static final String DEFAULT_FILE = "secrets.properties";

    public static AppConfig load() throws IOException {
        return load(DEFAULT_FILE);
    }

    public static AppConfig load(String filePath) throws IOException {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            throw new IOException("Nie znaleziono pliku konfiguracyjnego: " + path.toAbsolutePath());
        }

        Properties props = new Properties();

        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
        }

        return new AppConfig(
                MailConfig.from(props),
                Boolean.parseBoolean(props.getProperty("dry.run", "true"))
        );
    }
}