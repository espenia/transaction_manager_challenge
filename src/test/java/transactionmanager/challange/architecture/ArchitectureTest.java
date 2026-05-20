package transactionmanager.challange.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.inject.Inject;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings({"PMD"})
@AnalyzeClasses(
    packages = "transactionmanager.challange",
    importOptions = DoNotIncludeTests.class)
class ArchitectureTest {

  @ArchTest
  private static final ArchRule LAYER_DEPENDENCIES_ARE_RESPECTED =
      layeredArchitecture()
          .consideringAllDependencies()
          .layer("App")
          .definedBy("transactionmanager.challange")
          .layer("Core")
          .definedBy("transactionmanager.challange.core..")
          .layer("Core Exception")
          .definedBy("transactionmanager.challange.core.exception..")
          .layer("Infra")
          .definedBy("transactionmanager.challange.infra..")
          .layer("Gateway Exception")
          .definedBy("transactionmanager.challange.infra.gateway.exception..")
          .layer("Entrypoint")
          .definedBy("transactionmanager.challange.entrypoint..")
          .layer("Entrypoint Exception Handler")
          .definedBy(
              "transactionmanager.challange.entrypoint.exception.handler..")
          .whereLayer("Core Exception")
          .mayOnlyBeAccessedByLayers("Core", "Entrypoint", "Entrypoint Exception Handler", "Infra")
          .whereLayer("Gateway Exception")
          .mayOnlyBeAccessedByLayers("Infra", "Core", "Entrypoint", "Entrypoint Exception Handler")
          .whereLayer("Entrypoint")
          .mayNotBeAccessedByAnyLayer()
          .whereLayer("Core")
          .mayOnlyBeAccessedByLayers("Entrypoint", "Infra")
          .whereLayer("Infra")
          .mayOnlyBeAccessedByLayers("App", "Entrypoint", "Entrypoint Exception Handler")
          .allowEmptyShould(true);

  @ArchTest
  private static final ArchRule CORE_GATEWAY_MAY_ONLY_BE_ACCESSED_BY_CORE_USE_CASE_AND_INFRA =
      layeredArchitecture()
          .consideringAllDependencies()
          .layer("Core Gateway")
          .definedBy("transactionmanager.challange.core.gateway..")
          .layer("Core Use-case")
          .definedBy("transactionmanager.challange.core.usecase..")
          .layer("Infra")
          .definedBy("transactionmanager.challange.infra..")
          .whereLayer("Core Gateway")
          .mayOnlyBeAccessedByLayers("Core Use-case", "Infra")
          .allowEmptyShould(true);

  @ArchTest
  private static final ArchRule NO_FIELD_INJECTION =
      fields()
          .should()
          .notBeAnnotatedWith(Inject.class)
          .andShould()
          .notBeAnnotatedWith(Autowired.class)
          .allowEmptyShould(true);

  @ArchTest
  private static final ArchRule REPOSITORIES_SHOULD_ONLY_BE_ACCESSED_BY =
      classes()
          .that()
          .resideInAPackage("..infra.inmemory.repository..")
          .should()
          .onlyBeAccessed()
          .byClassesThat()
          .resideInAnyPackage("..infra.gateway..", "..infra.inmemory.repository.impl..")
          .allowEmptyShould(true);

  @ArchTest
  private static final ArchRule ENTITIES_MUST_BE_NAMED_ENDING_WITH_ENTITY =
      classes()
          .that()
          .resideInAPackage("transactionmanager.challange.infra.inmemory.entity..")
          .should()
          .haveSimpleNameEndingWith("Entity")
          .orShould()
          .haveSimpleNameEndingWith("EntityBuilder")
          .allowEmptyShould(true);


  @ArchTest
  private static final ArchRule REST_CONTROLLERS_MUST_RESIDE_IN_ENTRYPOINT_PACKAGE =
      classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .should()
          .resideInAPackage("..entrypoint..")
          .allowEmptyShould(true)
          .as(
              "Controllers should reside in a package"
                  + " 'transactionmanager.challange.entrypoint'");

  @ArchTest
  private static final ArchRule CONTROLLERS_MUST_RESIDE_IN_ENTRYPOINT_PACKAGE =
      classes()
          .that()
          .areAnnotatedWith(Controller.class)
          .should()
          .resideInAPackage("..entrypoint..")
          .allowEmptyShould(true)
          .as(
              "Controllers should reside in a package"
                  + " 'transactionmanager.challange.entrypoint'");

  @ArchTest
  private static final ArchRule LOGGERS_SHOULD_BE_PRIVATE_STATIC_FINAL =
      fields()
          .that()
          .haveRawType(Logger.class)
          .should()
          .bePrivate()
          .andShould()
          .beStatic()
          .andShould()
          .beFinal()
          .allowEmptyShould(true)
          .because("we agreed on this convention");

  @ArchTest
  private static final ArchRule INTERFACES_MUST_NOT_BE_PLACED_IN_IMPLEMENTATION_PACKAGES =
      noClasses()
          .that()
          .resideInAPackage("..impl..")
          .should()
          .beInterfaces()
          .allowEmptyShould(true);

  @ArchTest
  private static final ArchRule GATEWAYS_MUST_BE_INTERFACE =
      classes().that().resideInAPackage("..core.gateway..").should().beInterfaces().allowEmptyShould(true);

  @ArchTest
  private static final ArchRule NO_GENERIC_EXCEPTIONS = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.allowEmptyShould(true);

  @ArchTest
  private static final ArchRule NO_JAVA_UTIL_LOGGING = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING.allowEmptyShould(true);

  @ArchTest private static final ArchRule NO_JODATIME = NO_CLASSES_SHOULD_USE_JODATIME.allowEmptyShould(true);
}
