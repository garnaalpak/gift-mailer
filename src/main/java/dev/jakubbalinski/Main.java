package dev.jakubbalinski;

import dev.jakubbalinski.config.AppConfig;
import dev.jakubbalinski.models.DrawResult;
import dev.jakubbalinski.models.Member;
import dev.jakubbalinski.services.DrawService;
import dev.jakubbalinski.services.SendMailService;
import dev.jakubbalinski.utils.FileReader;

import java.io.IOException;
import java.util.List;

class Main {

    private static final String membersFile = "members.txt";

    static void main() {
        try {
            AppConfig config = AppConfig.load();
            DrawService drawService = new DrawService();
            SendMailService sendMailService = new SendMailService(config.mailConfig());

            System.out.println("Host: " + config.mailConfig().smtpHost());
            System.out.println("Port: " + config.mailConfig().smtpPort());
            System.out.println("Użytkownik: " + config.mailConfig().smtpUser());

            System.out.println("Tryb testowy (dry run): " + config.dryRun());

            List<Member> members = FileReader.readFile(membersFile);

            List<DrawResult> drawResults = drawService.getDrawResults(members);

            if (config.dryRun()) {
                System.out.println("[DRY-RUN] Wiadomości nie zostaną wysłane.");

                System.out.println("Wynik losowania: ");
                drawResults.forEach(System.out::println);
            } else {
                sendMailService.sendDrawNotifications(drawResults);
            }


        } catch (IOException e) {
            System.err.println("Błąd podczas ładowania konfiguracji: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Błędny format portu w pliku konfiguracyjnym: " + e.getMessage());
        }
    }
}
