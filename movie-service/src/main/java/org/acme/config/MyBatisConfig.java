package org.acme.config;

import org.acme.persistence.mapper.BaseMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;

@ApplicationScoped
public class MyBatisConfig {

    @Inject
    DataSource dataSource;

    @Produces
    @ApplicationScoped
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, dataSource);

        Configuration configuration = new Configuration(environment);
        configuration.addMappers(BaseMapper.class.getPackageName(), BaseMapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
