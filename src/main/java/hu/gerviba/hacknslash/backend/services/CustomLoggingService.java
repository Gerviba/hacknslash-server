package hu.gerviba.hacknslash.backend.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom logger service
 * @author Gergely Szabó
 */
@Slf4j
@Service
public class CustomLoggingService {

    @Value("${game.custom-log:log/log-<date>.log}")
    String fileName;
    
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private PrintWriter pw;
    
    /**
     * Creates the file and inits the PrintWriter
     * @throws FileNotFoundException
     */
    @PostConstruct
    public void init() throws FileNotFoundException {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        File f = new File(fileName.replace("<date>", date.format(System.currentTimeMillis())));
        f.getParentFile().mkdirs();
        pw = new PrintWriter(f);
        log.info("Custom logger file: " + f.getName());
        info("Logging started");
    }
    
    /**
     * Closes the file on shutdown
     */
    @PreDestroy
    public void destroy() {
        info("Shutting down...");
        pw.flush();
        pw.close();
    }
    
    /**
     * Flush the file in every 10sec
     */
    @Scheduled(fixedRate = 10000)
    public void flushIt() {
        pw.flush();
    }
    
    /**
     * Log an INFO level message
     * @param message The message to log
     */
    public synchronized void info(String message) {
        pw.println(String.format("[%s] [ INFO]: %s", format.format(System.currentTimeMillis()), message));
    }

    /**
     * Log an ERROR level message
     * @param message The message to log
     */
    public synchronized void error(String message) {
        pw.println(String.format("[%s] [ERROR]: %s", format.format(System.currentTimeMillis()), message));
    }

    /**
     * Log a WARN level message
     * @param message The message to log
     */
    public synchronized void warning(String message) {
        pw.println(String.format("[%s] [ WARN]: %s", format.format(System.currentTimeMillis()), message));
    }
    
}
