package org.example.zhc.util.zhc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServiceValidator implements ConstraintValidator<ServiceConstraint,Service> {

    @Override
    public boolean isValid(Service value, ConstraintValidatorContext context) {
        return false;
    }
}
