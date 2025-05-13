package com.example.glibrary.controller;

import com.example.glibrary.model.LogStatus;
import com.example.glibrary.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<String> createLog() {
        String id = logService.createLogSession();
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<LogStatus> getStatus(@PathVariable String id) {
        return ResponseEntity.ok(logService.getStatus(id));
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<String> getLogFile(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(logService.getLogFile(id));
    }
}
