package org.seasar.doma.quarkus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Config {
  String value();

  class Literal extends AnnotationLiteral<Config> implements Config {
    private final String name;

    public Literal(String name) {
      this.name = Objects.requireNonNull(name);
    }

    @Override
    public String value() {
      return name;
    }
  }
}
