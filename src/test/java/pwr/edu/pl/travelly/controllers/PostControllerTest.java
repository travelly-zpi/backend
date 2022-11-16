package pwr.edu.pl.travelly.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pwr.edu.pl.travelly.api.controllers.PostController;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.post.PostFacade;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @InjectMocks
    PostController postController;

    @Mock
    PostFacade postFacade;
    UUID id = UUID.randomUUID();
    PostDto postDto = new PostDto();

    @BeforeAll
    public static void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void findPost_validInput_returnsStatusOk() {
        when(postFacade.findByUuid(id)).thenReturn(postDto);

        ResponseEntity<?> responseEntity = postController.findPost(id);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void findPost_null_throwsNotFoundException() {
        when(postFacade.findByUuid(null)).thenThrow(new NotFoundException("POST_NOT_FOUND"));

        assertThrows(NotFoundException.class, () -> postController.findPost(null));
    }

    @Test
    public void update_validInput_returnsStatusOk() {
        SavePostForm savePostForm = new SavePostForm();
        when(postFacade.update(id, savePostForm)).thenReturn(postDto);

        ResponseEntity<?> responseEntity = postController.update(id, savePostForm);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void updatePost_null_throwsNotFoundException() {
        when(postFacade.update(null, null)).thenThrow(new NotFoundException("POST_NOT_FOUND"));

        assertThrows(NotFoundException.class, () -> postController.update(null, null));
    }

    @Test
    public void delete_validInput_returnsStatusOk() {
        doNothing().when(postFacade).delete(id);

        ResponseEntity<?> responseEntity = postController.delete(id);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void updateStatus_validUUID_returnsStatusOk() {
        doNothing().when(postFacade).updateStatus(id, true);

        ResponseEntity<?> responseEntity = postController.updateStatus(id, true);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void create_validInput_returnsStatusOk() {
        when(postFacade.create(any(SavePostForm.class))).thenReturn(postDto);

        ResponseEntity<?> responseEntity = postController.createPost(new SavePostForm());

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }
}
