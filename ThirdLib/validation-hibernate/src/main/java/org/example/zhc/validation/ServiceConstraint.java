package org.example.zhc.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ServiceValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceConstraint {
    String message() default "Invalid service";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
