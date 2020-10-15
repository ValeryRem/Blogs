package main.controllers;

import main.api.response.InitResponse;
import main.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {
    private PostRepository postRepository;
    private final InitResponse initResponse;
    public DefaultController(InitResponse initResponse) {
        this.initResponse = initResponse;
    }

    @GetMapping("/")
    public String index(Model model) {
        System.out.println(initResponse.getTitle());
        return "index";
    }
}
