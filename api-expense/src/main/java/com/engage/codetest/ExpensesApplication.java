package com.engage.codetest;
import com.engage.codetest.security.BasicUser;
import com.engage.codetest.security.UserAuthenticator;
import com.engage.codetest.security.UserAuthorizer;
import com.engage.codetest.services.ExpenseService;
import com.engage.codetest.health.ExpensesApplicationHealthCheck;
import com.engage.codetest.api.ExpenseResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.sql.DataSource;
import java.util.EnumSet;

public class ExpensesApplication extends Application<ExpensesConfiguration>{
    private static final String SQL = "sql";
    // todo change to something not dropwizard blog
    private static final String DROPWIZARD_BLOG_SERVICE = "Dropwizard blog service";
    private static final String BEARER = "Bearer";

    public static void main(String[] args) throws Exception {

        // todo log properly
        //System.out.println(System.getProperty("user.dir"));
        new ExpensesApplication().run("server", "CodeTest.yml");
    }

    @Override
    public void run(ExpensesConfiguration configuration, Environment environment) {
        // Datasource configuration
        final DataSource dataSource =
                configuration.getDataSourceFactory().build(environment.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        // Register Health Check
        ExpensesApplicationHealthCheck healthCheck =
                new ExpensesApplicationHealthCheck();
        environment.healthChecks().register(DROPWIZARD_BLOG_SERVICE, healthCheck);
/*
        environment.jersey().register(new BasicAuthProvider<BasicUser>(new UserAuthenticator(),
                "SUPER SECRET STUFF"));*/

        // Security controls
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<BasicUser>()
                .setAuthenticator(new UserAuthenticator())
                .setAuthorizer(new UserAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(BasicUser.class));

        // CORS headers are added so the frontend can caonsume the services
        enableCorsHeaders(environment);

        // Register resources
        environment.jersey().register(new ExpenseResource());

        // Set up services with the datasource
        ExpenseService.setDbi(dbi);
    }

    private static void enableCorsHeaders(Environment env) {
        /**
         * This method implements the CORS controls access for the angular request.
         * */
        final FilterRegistration.Dynamic cors = env.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "http://localhost:8080");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
