package com.devrezaur.main.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logging")
public class LoggingController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @GetMapping("/trace")
    public String trace() {
        logger.trace("This is a TRACE level log message");
        return "TRACE log generated";
    }

    @GetMapping("/debug")
    public String debug() {
        logger.debug("This is a DEBUG level log message");
        return "DEBUG log generated";
    }

    @GetMapping("/info")
    public String info() {
        logger.info("This is an INFO level log message");
        return "INFO log generated";
    }

    @GetMapping("/warn")
    public String warn() {
        logger.warn("This is a WARN level log message");
        return "WARN log generated";
    }

    @GetMapping("/error")
    public String error() {
        logger.error("This is an ERROR level log message");
        return "ERROR log generated";
    }
}
