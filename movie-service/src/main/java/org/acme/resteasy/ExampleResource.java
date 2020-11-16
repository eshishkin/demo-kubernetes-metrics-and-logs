package org.acme.resteasy;

import org.acme.persistence.MapperTemplate;
import org.acme.persistence.entity.MovieEntity;
import org.acme.persistence.mapper.MovieMapper;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.UUID;

@Path("/resteasy/hello")
public class ExampleResource {

    @Inject
    MapperTemplate mapperTemplate;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieEntity create() {
        return mapperTemplate.doWithMapper(MovieMapper.class, mapper -> {
            MovieEntity entity = new MovieEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setTitle("Title");
            entity.setDescription("Description");
            entity.setReleased(LocalDate.now());

            mapper.create(entity);
            return mapper.findById(entity.getId());
        });
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieEntity get(@PathParam("id") UUID movieId) {
        System.out.println("movieId" + movieId.toString());
        return mapperTemplate.doWithMapper(
                MovieMapper.class,
                mapper -> mapper.findById(movieId.toString())
        );
    }

}