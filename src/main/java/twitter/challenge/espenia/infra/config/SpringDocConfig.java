package twitter.challenge.espenia.infra.config;

import static java.lang.String.format;
import static twitter.challenge.espenia.infra.util.ScopeUtils.getScopeValue;
import static twitter.challenge.espenia.infra.util.ScopeUtils.isLocalScope;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Spring Configuration class for documentation. */
@Configuration
public class SpringDocConfig {

  private static final String LOCAL_URL = "http://localhost:8080";

  private static final String FURY_URL = "https://%s_%s.furyapps.io";

  /**
   * @param appName Application name.
   * @param description Application description.
   * @param version Application version.
   * @return OpenAPI bean used to generate documentation.
   */
  @Bean
  public OpenAPI customOpenAPI(
      @Value("${app.title}") final String appName,
      @Value("${app.description}") final String description,
      @Value("${app.version}") final String version) {
    Info info =
        new Info()
            .title(appName)
            .version(version)
            .description(description);

    String url = isLocalScope() ? LOCAL_URL : format(FURY_URL, getScopeValue(), appName);
    Server server = new Server().url(url).description(format("Scope %s", getScopeValue()));

    return new OpenAPI().components(new Components()).info(info).addServersItem(server);
  }

  @Bean
  public ModelResolver modelResolver(final ObjectMapper objectMapper) {
    return new ModelResolver(objectMapper);
  }
}
