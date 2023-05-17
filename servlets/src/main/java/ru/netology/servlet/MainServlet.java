package ru.netology.servlet;

import ru.netology.Handler;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.repository.PostRepositoryImpl;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private final Map<String, Map<String, Handler>> handlers = new ConcurrentHashMap<>();
  private static final String PATH = "/api/posts";
  private static final String PATH_WITH_PARAMS = "/api/posts/";

  @Override
  public void init() {
    final PostRepository repository = new PostRepositoryImpl();
    final PostService service = new PostService(repository);
    controller = new PostController(service);

    addHandler("GET", PATH, (path, req, resp) -> {
      controller.all(resp);
      resp.setStatus(HttpServletResponse.SC_OK);
    });
    addHandler("GET", PATH_WITH_PARAMS, (path, req, resp) -> {
      try {
        controller.getById(getIdByParsePath(path), resp);
        resp.setStatus(HttpServletResponse.SC_OK);
      } catch (NotFoundException e) {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    });
    addHandler("POST", PATH, (path, req, resp) -> {
      controller.save(req.getReader(), resp);
      resp.setStatus(HttpServletResponse.SC_OK);
    });
    addHandler("DELETE", PATH_WITH_PARAMS, (path, req, resp) -> {
      try {
        controller.removeById(getIdByParsePath(path), resp);
        resp.setStatus(HttpServletResponse.SC_OK);
      } catch (NotFoundException e) {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    });
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final String method = req.getMethod();
      String path = req.getRequestURI();

      String pathToFindTheHandler = path;
      if (path.startsWith(PATH_WITH_PARAMS) && path.matches(PATH_WITH_PARAMS + "\\d+")) {
        pathToFindTheHandler = PATH_WITH_PARAMS;
      } else if (path.startsWith(PATH)) {
        pathToFindTheHandler = PATH;
      }

      Handler handler = handlers.get(method).get(pathToFindTheHandler);
      handler.handle(path, req, resp);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void addHandler(String method, String path, Handler handler) {
    Map<String, Handler> map = new ConcurrentHashMap<>();
    if (handlers.containsKey(method)) {
      map = handlers.get(method);
    }
    map.put(path, handler);
    handlers.put(method, map);
  }

  private long getIdByParsePath(String path) {
    // easy way
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}

