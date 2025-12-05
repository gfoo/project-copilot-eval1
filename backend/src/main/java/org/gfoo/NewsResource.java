package org.gfoo;

import org.gfoo.dto.NewsResponse;
import org.gfoo.dto.PagedResponse;
import org.gfoo.service.NewsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
public class NewsResource {
    
    @Inject
    NewsService newsService;
    
    @GET
    public Response getNews(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            List<NewsResponse> news = newsService.getNews(page, size);
            long total = newsService.count();
            return Response.ok(new PagedResponse<>(news, page, size, total)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getNewsById(@PathParam("id") String id) {
        return newsService.getNewsById(id)
            .map(Response::ok)
            .orElse(Response.status(Response.Status.NOT_FOUND))
            .build();
    }
}
