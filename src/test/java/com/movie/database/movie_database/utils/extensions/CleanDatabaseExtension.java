package com.movie.database.movie_database.utils.extensions;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class CleanDatabaseExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        getBean(context, JdbcTemplate.class).update("delete from role");
        getBean(context, JdbcTemplate.class).update("delete from application_user");
        getBean(context, JdbcTemplate.class).update("delete from application_user_role");
    }

    private <T> T getBean(ExtensionContext context, Class<T> clazz) {
        return SpringExtension.getApplicationContext(context).getBean(clazz);
    }
}
