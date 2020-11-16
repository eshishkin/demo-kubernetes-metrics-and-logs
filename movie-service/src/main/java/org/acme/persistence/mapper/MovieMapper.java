package org.acme.persistence.mapper;

import org.acme.persistence.entity.MovieEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface MovieMapper extends BaseMapper {

    @Select("SELECT * FROM movie WHERE id = #{id}")
    MovieEntity findById(String id);

    @Insert("INSERT INTO movie (id, title, description, released) " +
            "VALUES" +
            "(#{id}, #{title}, #{description}, #{released})")
    void create(MovieEntity entity);
}
