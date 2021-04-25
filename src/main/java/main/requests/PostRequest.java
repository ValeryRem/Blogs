package main.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

public class PostRequest implements Serializable {
    long timestamp;
    Integer active;
    String title;

    @Nullable
    List<String> tags;
    String text;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public List<String> getTags() {
        return tags;
    }

    public void setTags(@Nullable List<String> tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
