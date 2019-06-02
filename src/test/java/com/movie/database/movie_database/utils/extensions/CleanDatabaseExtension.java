package com.movie.database.movie_database.utils.extensions;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class CleanDatabaseExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        getBean(context, JdbcTemplate.class).update("delete from application_user_movie");
        getBean(context, JdbcTemplate.class).update("delete from token_blacklist");
        getBean(context, JdbcTemplate.class).update("delete from movie_category");
        getBean(context, JdbcTemplate.class).update("delete from movie_rate");
        getBean(context, JdbcTemplate.class).update("delete from category");
        getBean(context, JdbcTemplate.class).update("delete from movie");
        getBean(context, JdbcTemplate.class).update("delete from application_user_token");
        getBean(context, JdbcTemplate.class).update("delete from application_user_role");
        getBean(context, JdbcTemplate.class).update("delete from application_user");
        getBean(context, JdbcTemplate.class).update("delete from role");
    }

    private <T> T getBean(ExtensionContext context, Class<T> clazz) {
        return SpringExtension.getApplicationContext(context).getBean(clazz);
    }
}
