package org.example.zhc.validation;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationTest {

    public static void main(String[] args){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Service svc = Service.builder()
                .name("ttt")
                .namespace("namespace")
                .build();
        Set<ConstraintViolation<Service>> validate = validator.validate(svc);
        for (ConstraintViolation<Service> cs :validate){
            System.out.println(cs.getMessage());
        }
        System.out.println("validate end");
    }
    @Test
    public void commanLang(){
        boolean anyBlank = StringUtils.isAnyBlank("i", "love", "", "you");
    }
}
