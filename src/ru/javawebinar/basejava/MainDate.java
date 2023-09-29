package ru.javawebinar.basejava;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainDate {

    public static void main(String[] args) throws InterruptedException {

        Date startDate = new Date();
        long st = System.currentTimeMillis();
        Thread.sleep(1000);
        Date endtDate = new Date();
        long et = System.currentTimeMillis();
        System.out.println(startDate);
        System.out.println(endtDate);
        System.out.println(et - st);

        LocalDateTime ldt = LocalDateTime.now();
        Thread.sleep(1000);
        LocalDateTime ldt2 = LocalDateTime.now();
        System.out.println(Duration.between(ldt2,ldt));
        LocalDate ld = LocalDate.from(ldt);
        System.out.println(ld);
        LocalTime lt = LocalTime.from(ldt);
        System.out.println(lt);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/YYYY");
        System.out.println(dateTimeFormatter.format(ldt));

    }
}
