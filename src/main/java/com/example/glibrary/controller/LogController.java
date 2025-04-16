package com.example.glibrary.controller;

import com.example.glibrary.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/logs")
@Tag(name = "Logs", description = "Operations with Logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/{date}")
    @Operation(summary = "Download Logs")
    public ResponseEntity<Resource> getLogs(@PathVariable String date) throws IOException {
        Path sourcePath = Paths.get("logs/app.log");
        Path targetPath = Paths.get("logs/app." + date + ".log");

        if (!Files.exists(sourcePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        if (!Files.exists(targetPath)) {
            Files.copy(sourcePath, targetPath);
        }

        Resource resource = new InputStreamResource(Files.newInputStream(targetPath));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=app." + date + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createLog() {
        String id = logService.createLogAsync(); // асинхронно запускаем создание
        return ResponseEntity.ok(id); // сразу возвращаем ID
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> getStatus(@PathVariable String id) {
        String status = logService.getStatus(id);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        Path filePath = logService.getFilePath(id);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + id + ".txt")
                    .body(fileBytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Ошибка при чтении файла.");
        }
    }

}