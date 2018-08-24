package ru.mihaly4.vkmd.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoryLogger implements ILogger {
    @Override
    public void error(String message) {
        // "[ERR@" + getTimestamp() +"] " + message
    }

    @Override
    public void info(String message) {
        // "[INFO@" + getTimestamp() +  "] " + message
    }

    private String getTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
