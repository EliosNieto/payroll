package com.entic.payroll.core.i18n;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Scans the payroll-core module and verifies that every error key passed to
 * {@code ValidationException}/{@code NotFoundException}/{@code AlreadyExistsException}
 * exists in the English messages file.
 */
class I18nKeyCoverageTest {

    private static final String CORE_SOURCES = "src/main/java";
    private static final String I18N_BASE = "../payroll-shared/src/main/resources/i18n";
    private static final String[] PROPERTIES = {
            "messages.properties"
    };

    private static final Pattern EXCEPTION_CALL =
            Pattern.compile("new (?:ValidationException|NotFoundException|AlreadyExistsException)\\(");

    private static final List<String> PREFIXES = List.of(
            "employee.", "contract.", "payroll.", "overtime.", "vacation.",
            "leave.", "benefit.", "settlement.", "payment.", "company.",
            "error.", "http."
    );

    @Test
    void everyErrorKeyExistsInMessagesProperties() throws IOException {
        Set<String> relevantKeys = collectErrorKeys().stream()
                .filter(this::isRelevant)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (String locale : PROPERTIES) {
            Set<String> present = loadKeys(locale);
            List<String> missing = relevantKeys.stream()
                    .filter(key -> !present.contains(key))
                    .toList();
            assertTrue(missing.isEmpty(),
                    "Missing i18n keys in " + locale + ": " + missing + System.lineSeparator()
                            + "Keys used in payroll-core but without a message entry.");
        }
    }

    private Set<String> collectErrorKeys() throws IOException {
        Path root = Paths.get(CORE_SOURCES);
        assertTrue(Files.isDirectory(root), "payroll-core sources not found: " + root.toAbsolutePath());

        Set<String> keys = new LinkedHashSet<>();
        try (Stream<Path> paths = Files.walk(root)) {
            List<Path> javaFiles = paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .toList();
            for (Path file : javaFiles) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                keys.addAll(extractKeys(source));
            }
        }
        return keys;
    }

    private Set<String> extractKeys(String source) {
        Set<String> keys = new LinkedHashSet<>();
        Matcher matcher = EXCEPTION_CALL.matcher(source);
        while (matcher.find()) {
            int i = matcher.end();
            int depth = 1;
            List<String> literals = new ArrayList<>();
            while (i < source.length() && depth > 0) {
                char c = source.charAt(i);
                if (c == '(') {
                    depth++;
                } else if (c == ')') {
                    depth--;
                } else if (c == '"') {
                    int end = source.indexOf('"', i + 1);
                    while (end != -1 && source.charAt(end - 1) == '\\') {
                        end = source.indexOf('"', end + 1);
                    }
                    if (end == -1) {
                        break;
                    }
                    literals.add(source.substring(i + 1, end));
                    i = end + 1;
                    continue;
                }
                i++;
            }
            if (!literals.isEmpty()) {
                keys.add(literals.get(literals.size() - 1));
            }
        }
        return keys;
    }

    private boolean isRelevant(String key) {
        return PREFIXES.stream().anyMatch(key::startsWith);
    }

    private Set<String> loadKeys(String locale) throws IOException {
        Path file = Paths.get(I18N_BASE, locale);
        assertTrue(Files.isRegularFile(file), "i18n file not found: " + file.toAbsolutePath());
        return Files.readAllLines(file, StandardCharsets.UTF_8).stream()
                .filter(line -> !line.isBlank() && !line.trim().startsWith("#"))
                .filter(line -> line.contains("="))
                .map(line -> line.substring(0, line.indexOf('=')).trim())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
