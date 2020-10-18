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
public class Storage{

    @Autowired
    private PostRepository postRepository;
    private List<Post> posts = new ArrayList<>();

    @Transactional
    public void addPost (Post post) {
        Iterable<Post> postIterable = postRepository.findAll();
        posts.add(post);
        postIterable.forEach(posts::add);
        postRepository.save(post);
    }

    public List<Post> getPosts() {
        return posts;
    }
}
