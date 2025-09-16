package com.example.ivf.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class CycleNumberGenerator {

    private final AtomicLong counter = new AtomicLong(1);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String nextCycleNumber() {
        long sequence = counter.getAndIncrement();
        return "CYC-" + LocalDate.now().format(FORMATTER) + "-" + String.format("%04d", sequence);
    }
}
