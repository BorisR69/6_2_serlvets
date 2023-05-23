package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryImpl implements PostRepository{

  private final List<Post> posts = Collections.synchronizedList(new ArrayList<>());
  private AtomicLong count = new AtomicLong(1);
  @Override
  public List<Post> all() {
    return posts;
  }

  @Override
  public Optional<Post> getById(long id) {
    Post p = findPostById(id);
    return p != null ? Optional.of(p) : Optional.empty();
  }

  @Override
  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(count.longValue());
      count.addAndGet(1);
      posts.add(post);
      return post;
    }

    Post p = findPostById(post.getId());
    if (p != null) {
      posts.set(posts.indexOf(p), post);
    }
    return p;
  }

  @Override
  public boolean removeById(long id) {
    Post p = findPostById(id);
    if (p != null) {
      posts.remove(p);
      return true;
    }
    return false;
  }

  private Post findPostById(long id) {
    return posts.stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
  }
}