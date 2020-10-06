package main.base;

import main.model.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Storage {
//    private final int currentId = 1;

    @Autowired
    private PostRepository postRepository;
    private List<Post> posts = new ArrayList<>();
    @Transactional
    public void addPost (Post post) {
        Iterable<Post> postIterable = postRepository.findAll();
        postIterable.forEach(posts::add);
//        indicatorOfDoubledSeat = touristList.stream().anyMatch(t -> t.getSeat().equals(tourist.getSeat()));
//        if(indicatorOfDoubledSeat || !tourist.getBirthday().matches("\\d{4}-\\d{2}-\\d{2}") || !tourist.getName().matches("[a-zA-Z]*")) {
//            return;
//        }
        postRepository.save(post);
    }

    public List<Post> getPosts() {
        return posts;
    }
}
