package main.api.response;
import java.util.TreeMap;

public class PostResponse {
        private final Integer id;
        private final long timestamp;
        private final TreeMap<String, Object> user;
        private final String title;
        private final String announce;
        private Integer likeCount;
        private Integer dislikeCount;
        private final Integer commentCount;
        private final Integer viewCount;

    public PostResponse(Integer postId, long timestamp, String title, String announce,
                        Integer commentCount, Integer viewCount, TreeMap<String, Object> user) {
        this.id = postId;
        this.timestamp = timestamp;
        this.title = title;
        this.announce = announce;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.user = user;
    }

        public Integer getId() {
            return id;
        }

        public long getTimestamp() {
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

        public Integer getCommentCount() {
            return commentCount;
    }

        public Integer getViewCount() {
            return viewCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public void setDislikeCount(Integer dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

}

