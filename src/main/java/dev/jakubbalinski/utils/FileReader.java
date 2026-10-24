package dev.jakubbalinski.utils;

import dev.jakubbalinski.models.Member;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {

    public static List<Member> readFile(String fileName) {

        try (Stream<String> lines = Files.lines(Path.of(fileName), StandardCharsets.UTF_8)) {

            return lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(line -> line.trim().split(","))
                    .filter(parts -> parts.length >= 2)
                    .map(parts -> new Member(parts[0].trim(), parts[1].trim()))
                    .toList();

        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }

        return List.of();
    }

}
