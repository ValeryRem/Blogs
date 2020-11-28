package main.api.response;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

public class CalendarResponse {
    List<Integer> years;
    List<LinkedHashMap<LocalDate, Integer>> posts;

    public CalendarResponse(List<Integer> years, List<LinkedHashMap<LocalDate, Integer>> posts) {
        this.years = years;
        this.posts = posts;
    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public List<LinkedHashMap<LocalDate, Integer>> getPosts() {
        return posts;
    }

    public void setPosts(List<LinkedHashMap<LocalDate, Integer>> posts) {
        this.posts = posts;
    }
}
