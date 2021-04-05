package org.eshishkin.edu.k8sdemo.movieservice.web;

import org.eshishkin.edu.k8sdemo.movieservice.persistence.MapperTemplate;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.entity.MovieEntity;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.mapper.MovieMapper;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.UUID;

@Path("/movies")
public class MovieResource {

    @Inject
    MapperTemplate mapperTemplate;

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieEntity create(@Valid MovieEntity request) {
        return mapperTemplate.doWithMapper(MovieMapper.class, mapper -> {
            MovieEntity entity = new MovieEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setTitle(request.getTitle());
            entity.setDescription(request.getTitle());
            entity.setReleased(LocalDate.now());

            mapper.create(entity);
            return mapper.findById(entity.getId());
        });
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieEntity get(@PathParam("id") UUID movieId) {
        return mapperTemplate.doWithMapper(
                MovieMapper.class,
                mapper -> mapper.findById(movieId.toString())
        );
    }

}