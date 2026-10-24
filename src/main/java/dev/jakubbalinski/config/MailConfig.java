package dev.jakubbalinski.config;

import java.util.Properties;

public record MailConfig(
        String smtpHost,
        Integer smtpPort,
        String smtpUser,
        String smtpPassword
) {
    public static MailConfig from(Properties props) {
        return new MailConfig(
                props.getProperty("smtp.host", "smtp.gmail.com"),
                Integer.parseInt(props.getProperty("smtp.port", "587")),
                props.getProperty("smtp.user"),
                props.getProperty("smtp.password")
        );
    }
}
