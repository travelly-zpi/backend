package pwr.edu.pl.travelly.api.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pwr.edu.pl.travelly.core.post.PostFacade;
import pwr.edu.pl.travelly.core.post.form.CreatePostForm;
import pwr.edu.pl.travelly.core.post.form.CustomPageable;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/post")
public class PostController {
    private final PostFacade postFacade;

    public PostController(@Qualifier("postFacade") final PostFacade postFacade) {
        this.postFacade = postFacade;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findAllPosts(@RequestParam final int page,@RequestParam final int size, @RequestBody final PostFilterForm filterForm){
        return ResponseEntity.ok(postFacade.findAll(PageRequest.of(page,size), filterForm));
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> findPost(@PathVariable final UUID uuid){
        return ResponseEntity.ok(postFacade.findByUuid(uuid));
    }

//    @RequestMapping(value="/{uuid}/activate", method = RequestMethod.PUT)
//    public ResponseEntity<?> activatePost(@PathVariable final UUID uuid){
//        postFacade.activate(uuid);
//        return ResponseEntity.ok(user);
//    }
//
//    @RequestMapping(value="/{uuid}/deactivate", method = RequestMethod.PUT)
//    public ResponseEntity<?> deactivatePost(@PathVariable final UUID uuid){
//        final UserDto user = userFacade.findByUuid(uuid);
//        return ResponseEntity.ok(user);
//    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@ModelAttribute @Valid final CreatePostForm createPostForm) throws IOException {
        return ResponseEntity.ok(postFacade.create(createPostForm));
    }

}
