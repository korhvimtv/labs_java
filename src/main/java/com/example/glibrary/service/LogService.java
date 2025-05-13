package com.example.glibrary.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.example.glibrary.model.LogStatus;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ch.qos.logback.classic.*;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class LogService {

    private static final String LOG_PATH = "logs";
    private final Map<String, LogStatus> statusMap = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public String createLogSession() {
        String id = UUID.randomUUID().toString();
        statusMap.put(id, LogStatus.PENDING);

        executor.submit(() -> captureLogs(id));

        return id;
    }

    private void captureLogs(String id) {
        statusMap.put(id, LogStatus.IN_PROGRESS);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            Path logDir = Paths.get(LOG_PATH);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            String logFileName = "log-" + id + ".txt";
            Path logFilePath = logDir.resolve(logFileName);

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(context);
            encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
            encoder.start();

            FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
            fileAppender.setContext(context);
            fileAppender.setName("FA-" + id);
            fileAppender.setEncoder(encoder);
            fileAppender.setAppend(true);
            fileAppender.setFile(logFilePath.toString());
            fileAppender.start();

            ch.qos.logback.classic.Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLogger.addAppender(fileAppender);

            Logger logger = (Logger) LoggerFactory.getLogger(LogService.class);
            logger.info("Started capturing logs for ID {}", id);

            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 20_000) {
                Thread.sleep(500);
            }

            logger.info("Finished capturing logs for ID {}", id);

            rootLogger.detachAppender(fileAppender);
            fileAppender.stop();
            encoder.stop();

            statusMap.put(id, LogStatus.DONE);
        } catch (Exception e) {
            LoggerFactory.getLogger(LogService.class).error("Error generating logs for ID: " + id, e);
            statusMap.put(id, LogStatus.ERROR);
        } finally {
            MDC.clear();
        }
    }

    public LogStatus getStatus(String id) {
        return statusMap.getOrDefault(id, LogStatus.PENDING);
    }

    public String getLogFile(String id) throws IOException {
        if (statusMap.get(id) != LogStatus.DONE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log File is not Ready");
        }
        Path path = Paths.get(LOG_PATH, "log-" + id + ".txt");
        return Files.readString(path);
    }
}
