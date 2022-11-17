package pwr.edu.pl.travelly.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pwr.edu.pl.travelly.persistence.post.entity.Post;
import pwr.edu.pl.travelly.persistence.post.repository.PostRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    public void findAll_exist_returnsAllPosts() {
        Post post1 = new Post();
        post1.setUuid(UUID.randomUUID());
        post1.setTitle("first post");
        Post post2 = new Post();
        post2.setUuid(UUID.randomUUID());
        post2.setTitle("second post");
        List<Post> posts = Arrays.asList(post1, post2);

        postRepository.saveAllAndFlush(posts);

        List<Post> found = postRepository.findAll();

        assertEquals(found.size(), 2);
        assertEquals(found.get(0).getTitle(), post1.getTitle());
        assertEquals(found.get(1), post2);
    }

}