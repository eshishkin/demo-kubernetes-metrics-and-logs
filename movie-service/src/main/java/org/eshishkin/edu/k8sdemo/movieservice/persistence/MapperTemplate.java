package org.eshishkin.edu.k8sdemo.movieservice.persistence;

import org.eshishkin.edu.k8sdemo.movieservice.persistence.mapper.BaseMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.function.Function;

@ApplicationScoped
public class MapperTemplate {

    @Inject
    SqlSessionFactory sqlSessionFactory;

    public <T extends BaseMapper, V> V doWithMapper(Class<T> type, Function<T, V> mapping) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            T mapper = session.getMapper(type);
            V result = mapping.apply(mapper);
            session.commit();
            return result;
        }
    }

    public <V> V doWithSession(Function<SqlSession, V> mapping) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            V result = mapping.apply(session);
            session.commit();
            return result;
        }
    }
}
