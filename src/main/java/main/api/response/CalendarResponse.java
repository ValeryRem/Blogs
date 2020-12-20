package main.api.response;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

public class CalendarResponse {
    List<Integer> years;
    LinkedHashMap<Long, Integer> posts;

    public CalendarResponse(List<Integer> years, LinkedHashMap<Long, Integer> posts) {
        this.years = years;
        this.posts = posts;
    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public LinkedHashMap<Long, Integer> getPosts() {
        return posts;
    }

    public void setPosts(LinkedHashMap<Long, Integer> posts) {
        this.posts = posts;
    }
}
