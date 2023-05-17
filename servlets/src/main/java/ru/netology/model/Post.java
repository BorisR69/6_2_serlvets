package ru.netology.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Post {
  private long id;
  private String content;

  public Post() {
  }

  public Post(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Post post = (Post) o;
    return id == post.id && Objects.equals(content, post.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, content);
  }

  @Override
  public String toString() {
    return "Post{" +
            "id=" + id +
            ", content='" + content + '\'' +
            '}';
  }
}
