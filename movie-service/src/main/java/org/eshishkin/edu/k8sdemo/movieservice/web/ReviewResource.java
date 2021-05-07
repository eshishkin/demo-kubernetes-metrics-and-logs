package org.eshishkin.edu.k8sdemo.movieservice.web;

import org.eshishkin.edu.k8sdemo.movieservice.config.RabbitConfig;
import org.eshishkin.edu.k8sdemo.movieservice.exception.ResourceNotFoundException;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.MapperTemplate;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.RabbitTemplate;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.entity.ReviewEntity;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.event.MovieServiceEvent;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.event.MovieServiceEvent.MessageType;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.mapper.ReviewMapper;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

    @Inject
    RabbitTemplate rabbitTemplate;

    @Inject
    RabbitConfig rabbitConfig;

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

            rabbitTemplate.send(
                    rabbitConfig.getReviewRoutingKey(),
                    MovieServiceEvent.of(entity.getId(), MessageType.CREATED)
            );

            return mapper.findById(entity.getId());
        });
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReviewEntity get(@PathParam("id") UUID reviewId) {
        return mapperTemplate.doWithMapper(ReviewMapper.class, mapper -> {
            ReviewEntity entity = mapper.findById(reviewId.toString());

            if (entity == null) {
                throw new ResourceNotFoundException("No entity found: " + reviewId);
            }
            return entity;
        });
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID reviewId) {
        mapperTemplate.doWithMapper(ReviewMapper.class, mapper -> {
            ReviewEntity entity = mapper.findById(reviewId.toString());

            if (entity == null) {
                throw new ResourceNotFoundException("No entity found: " + reviewId);
            }

            mapper.deleteById(entity.getId());

            rabbitTemplate.send(
                    rabbitConfig.getReviewRoutingKey(),
                    MovieServiceEvent.of(entity.getId(), MessageType.DELETED)
            );
            return null;
        });
    }

}