package main.api.response;

import main.entity.Post;
import main.entity.User;
import java.time.LocalDate;
import java.util.TreeMap;


public class PostAnnounceResponse {
        private final Integer id;
        private final LocalDate timestamp;
        private final TreeMap<String, Object> user ;
        private final String title;
        private final String announce;
        private final Integer likeCount;
        private final Integer dislikeCount;
        private final Integer viewCount;

        public PostAnnounceResponse(Post post)
        {
            User  usr = new User(post.getUserId());
            user = new TreeMap<>();
            this.id = post.getPostId();
            this.timestamp = post.getTime();
            this.title = post.getTitle();
            this.announce = post.getAnnounce();
            this.likeCount = post.getLikeCount();
            this.dislikeCount = post.getDislikeCount();
            this.viewCount = post.getViewCount();
            user.put("id", post.getUserId());
            user.put("name", usr.getName());
        }

        public Integer getId() {
            return id;
        }

        public LocalDate getTimestamp() {
            return timestamp;
        }

        public TreeMap<String, Object> getUser() {
            return user;
        }

        public String getTitle() {
            return title;
        }

        public String getAnnounce() {
            return announce;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public Integer getDislikeCount() {
            return dislikeCount;
        }

        public Integer getViewCount() {
            return viewCount;
        }
    }

