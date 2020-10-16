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

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public void addPost (Post post) {
        Iterable<Post> postIterable = postRepository.findAll();
        List<Post> posts = new ArrayList<>();
        postIterable.forEach(posts::add);
        posts.add(post);
        postRepository.save(post);
    }

    public List<Post> getPosts() {
        Iterable<Post> postIterable = postRepository.findAll();
        List<Post> posts = new ArrayList<>();
        postIterable.forEach(posts::add);
        return posts;
    }
}
