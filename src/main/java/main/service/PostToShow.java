/*
{
			"id": 345,
			"timestamp": 1592338706,
			"user":
				{
				"id": 88,
				"name": "Дмитрий Петров"
				},
			"title": "Заголовок поста",
			"announce": "Текст анонса поста без HTML-тэгов",
			"likeCount": 36,
			"dislikeCount": 3,
			"commentCount": 15,
			"viewCount": 55
		},


 */
package main.service;

import main.entity.Post;
import main.entity.User;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PostToShow {
    private List<Object> postToShow = new ArrayList<>();

    public PostToShow(Post post) {
        postToShow.add(post.getPostId());
        postToShow.add(post.getTime());
        postToShow.add(post.getTitle());
        postToShow.add(post.getAnnounce());
        List<Object> userToShow = new ArrayList<>();
        userToShow.add(post.getUserId());
            User user = new User(post.getUserId());
            userToShow.add(user.getName());
        postToShow.add(userToShow);
        postToShow.add(post.getLikeCount());
        postToShow.add(post.getViewCount());
    }

    public List<Object> getPostToShow() {
        return postToShow;
    }
}
