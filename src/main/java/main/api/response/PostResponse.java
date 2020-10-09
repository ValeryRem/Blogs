package main.api.response;

import main.base.Storage;
import main.model.Post;
import main.repository.PostRepository;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostResponse {
//    @Autowired
//    private ResponseEntity<?> responseEntity;
    @Autowired
    private PostPreviewResponse postPreviewResponse;
    private PostRepository postRepository;

    public ResponseEntity<?> getPosts(Integer offset, Integer limit) {
        Iterable<Post> postPreviewResponses = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : postPreviewResponses) {
            result.add(post);
        }
        if (result.size() == 0) {
            return new ResponseEntity<>("The list of posts is empty", HttpStatus.NO_CONTENT);
        }
        else if (offset + limit <= result.size()) {
            return ResponseEntity.ok(result.subList(offset, offset + limit));
        } else {
            return ResponseEntity.ok(result.subList(offset, result.size()));
        }
    }
}
