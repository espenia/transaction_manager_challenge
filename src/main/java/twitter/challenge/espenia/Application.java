package twitter.challenge.espenia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import twitter.challenge.espenia.infra.util.ScopeUtils;

/** Main class for the App. */
@Slf4j
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class Application {

  /**
   * @param args command line arguments for the application.
   */
  public static void main(String[] args) {
    ScopeUtils.calculateScopeSuffix();
    log.info("Starting application with scope: {}", ScopeUtils.getScopeValue());
    new SpringApplicationBuilder(Application.class).registerShutdownHook(true).run(args);
  }
}
