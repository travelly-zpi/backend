package pwr.edu.pl.travelly.api.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pwr.edu.pl.travelly.core.post.PostFacade;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    public ResponseEntity<?> findAllPosts(@RequestParam final int page,
                                          @RequestParam final int size,
                                          @RequestParam(required = false) final String query,
                                          @RequestParam(required = false) final String activeFrom,
                                          @RequestParam(required = false) final String activeTo,
                                          @RequestParam(required = false) final String date,
                                          @RequestParam(required = false) final Boolean active,
                                          @RequestParam(required = false) final Integer participants,
                                          @RequestParam(required = false) final String startPoint,
                                          @RequestParam(required = false) final String endPoint,
                                          @RequestParam(required = false) final UUID author,
                                          @RequestParam(required = false) final UUID notAuthor,
                                          @RequestParam(required = false) final String type){
        final PostFilterForm filterForm = PostFilterForm.builder()
                .title(query)
                .startDate(activeFrom)
                .endDate(activeTo)
                .date(date)
                .active(active)
                .participants(participants)
                .startPoint(startPoint)
                .endPoint(endPoint)
                .author(author)
                .notAuthor(notAuthor)
                .type(type)
                .build();
        return ResponseEntity.ok(postFacade.findAll(PageRequest.of(page-1,size, Sort.by("creationTimestamp").descending()), filterForm));
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> findPost(@PathVariable final UUID uuid){
        return ResponseEntity.ok(postFacade.findByUuid(uuid));
    }

    @RequestMapping(value="/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable final UUID uuid, @RequestBody @Valid final SavePostForm savePostForm){
        return ResponseEntity.ok(postFacade.update(uuid, savePostForm));
    }

    @RequestMapping(value="/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable final UUID uuid){
        postFacade.delete(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/{uuid}/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStatus(@PathVariable final UUID uuid, final @NotNull @RequestParam Boolean status){
        postFacade.updateStatus(uuid, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/{uuid}/attachmentUpload", method = RequestMethod.PUT)
    public ResponseEntity<?> uploadAttachment(@RequestBody final MultipartFile image, final @PathVariable("uuid") UUID postUuid, final @RequestParam Boolean status) throws IOException {
        postFacade.uploadAttachment(image, postUuid, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/{uuid}/attachmentDelete", method = RequestMethod.PUT)
    public ResponseEntity<?> deleteAttachment(final @PathVariable("uuid") UUID postUuid, @RequestParam UUID attachmentUuid) {
        postFacade.deleteAttachment(postUuid, attachmentUuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@RequestBody @Valid final SavePostForm savePostForm) {
        return ResponseEntity.ok(postFacade.create(savePostForm));
    }

}
