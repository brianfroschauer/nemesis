package configuration;

import filter.AuthenticationFilter;
import filter.CORSFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Author: brianfroschauer
 * Date: 13/04/2018
 */
@ApplicationPath("")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("resources");
        register(CORSFilter.class);
        register(AuthenticationFilter.class);
        register(MultiPartFeature.class);
    }
}