package org.eshishkin.edu.k8sdemo.movieservice.persistence.mapper;

import org.eshishkin.edu.k8sdemo.movieservice.persistence.entity.ReviewEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ReviewMapper extends BaseMapper {

    @Select("SELECT * FROM review WHERE id = #{id}")
    ReviewEntity findById(String id);

    @Insert("INSERT INTO review (id, movie_id, title, content, author_id, created) " +
            "VALUES" +
            "(#{id}, #{movieId}, #{title}, #{content}, #{authorId}, #{created})")
    void create(ReviewEntity entity);
}
