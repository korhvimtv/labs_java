package com.example.glibrary.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogService {

    // Храним статус каждого задания по его ID
    private final Map<String, String> statusMap = new ConcurrentHashMap<>();

    public String createLogAsync() {
        String id = UUID.randomUUID().toString(); // генерируем уникальный ID
        statusMap.put(id, "PROCESSING"); // устанавливаем начальный статус

        // Асинхронный запуск
        CompletableFuture.runAsync(() -> {
            try {
                // Эмуляция задержки (имитация долгой работы)
                Thread.sleep(5000);

                // Путь к лог-файлу
                Path logDir = Paths.get("logs");
                if (!Files.exists(logDir)) {
                    Files.createDirectories(logDir);
                }

                Path filePath = logDir.resolve(id + ".txt");

                // Пишем простой текст в лог
                Files.writeString(filePath, "Это лог-файл с ID: " + id);

                statusMap.put(id, "DONE");
            } catch (Exception e) {
                statusMap.put(id, "ERROR");
                e.printStackTrace();
            }
        });

        return id;
    }

    public String getStatus(String id) {
        return statusMap.getOrDefault(id, "NOT_FOUND");
    }

    public Path getFilePath(String id) {
        return Paths.get("logs", id + ".txt");
    }
}
