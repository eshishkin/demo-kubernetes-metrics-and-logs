package org.eshishkin.edu.k8sdemo.movieservice.web;

import org.eshishkin.edu.k8sdemo.movieservice.persistence.MapperTemplate;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.entity.ReviewEntity;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.mapper.ReviewMapper;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.UUID;

@Path("/reviews")
public class ReviewResource {

    @Inject
    MapperTemplate mapperTemplate;

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReviewEntity create(@Valid ReviewEntity request) {
        return mapperTemplate.doWithMapper(ReviewMapper.class, mapper -> {
            ReviewEntity entity = new ReviewEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setTitle(request.getTitle());
            entity.setContent(request.getContent());
            entity.setAuthorId(request.getAuthorId());
            entity.setMovieId(request.getMovieId());
            entity.setCreated(LocalDate.now());

            mapper.create(entity);
            return mapper.findById(entity.getId());
        });
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReviewEntity get(@PathParam("id") UUID reviewId) {
        return mapperTemplate.doWithMapper(
                ReviewMapper.class,
                mapper -> mapper.findById(reviewId.toString())
        );
    }

}