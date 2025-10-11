package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;

public class ModelTest<T> {

    protected static Validator validator;

    @BeforeAll
    public static void setValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected int validateModel(T model) {
        return validator.validate(model).size();
    }
}
