package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.TimeZone;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        setTimeZone();
    }

    private void setTimeZone () {
//        long ts = System.currentTimeMillis();
//        Date localTime = new Date(ts);
        ZoneId zid1 = ZoneId.of("UTC");
        String format = "yyyy.MM.dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat (format);
        sdf.setTimeZone(TimeZone.getTimeZone(zid1));
    }
}
