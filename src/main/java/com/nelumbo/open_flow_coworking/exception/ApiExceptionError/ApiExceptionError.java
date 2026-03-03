package com.nelumbo.open_flow_coworking.exception.ApiExceptionError;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApiExceptionError {

    private static final Map<Integer, ErrorEntry> errorMap;

    static {
        try {
            Yaml yaml = new Yaml();

            InputStream input = ApiExceptionError.class.getClassLoader().getResourceAsStream("errors/error.yaml");

            if (input == null) {
                throw new RuntimeException("errors/error.yaml File not found");
            }

            Map<String, Map<Integer, Map<String, String>>> raw = yaml.load(input);

            errorMap = raw.get("errors").entrySet().stream().collect(
                    Collectors.toMap(
                            Map.Entry::getKey,
                            e -> new ErrorEntry(e.getValue().get("message"), e.getValue().get("status"))
                    )
            );

        } catch (Exception e) {
            throw new RuntimeException("Error loading errors/error.yaml file", e);
        }
    }

    public static String formatMessage(int code, Object... args) {
        ErrorEntry entry = errorMap.get(code);
        if (entry == null) return "Error not found";
        return String.format(entry.message(), args);
    }

    public static String getHttpStatusName(int code) {
        ErrorEntry entry = errorMap.get(code);
        return entry != null ? entry.status() : "INTERNAL_SERVER_ERROR";
    }

    public static String getMessage(int code) {
        ErrorEntry entry = errorMap.get(code);
        return entry != null ? entry.message() : "Message not found";
    }

    private record ErrorEntry(String message, String status) {}
}
