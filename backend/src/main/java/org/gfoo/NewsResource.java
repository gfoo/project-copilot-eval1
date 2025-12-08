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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "News", description = "News articles management API")
public class NewsResource {
    
    @Inject
    NewsService newsService;
    
    @GET
    @Operation(summary = "Get all news articles", description = "Retrieves a paginated list of news articles")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = PagedResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public Response getNews(
            @Parameter(description = "Page number (zero-based)", example = "0")
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Number of items per page", example = "10")
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
    @Operation(summary = "Get news article by ID", description = "Retrieves a single news article by its unique identifier")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "News article found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = NewsResponse.class))),
        @APIResponse(responseCode = "404", description = "News article not found")
    })
    public Response getNewsById(
            @Parameter(description = "News article ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathParam("id") String id) {
        return newsService.getNewsById(id)
            .map(Response::ok)
            .orElse(Response.status(Response.Status.NOT_FOUND))
            .build();
    }
}
