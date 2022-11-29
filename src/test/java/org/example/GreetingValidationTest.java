package org.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.reflect.Field;

@QuarkusTest
public class GreetingValidationTest {

    // With @Inject, the tests pass. Without it, they don't.
    //@Inject
    private Validator validator;

    private long maxLimitValue;
    private long minLimitValue;

    @BeforeEach
    public void setUp() throws NoSuchFieldException {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }

        this.maxLimitValue = this.getMaxValueFromAnnotation("limit");
        this.minLimitValue = this.getMinValueFromAnnotation("limit");
    }

    /**
     * Tests that when the limit is above the maximum constraint, a constraint violation is generated.
     */
    @Test
    public void maxLimitTest() {
        final var greeting = new Greeting(
                (int) this.maxLimitValue + 1
        );

        final var cvs = this.validator.validate(greeting);
        Assertions.assertEquals(1, cvs.size());
    }

    /**
     * Tests that when the limit is below the minimum constraint, a constraint violation is generated.
     */
    @Test
    public void minLimitTest() {
        final var greeting = new Greeting(
            (int) this.minLimitValue - 1
        );

        final var cvs = this.validator.validate(greeting);
        Assertions.assertEquals(1, cvs.size());
    }

    /**
     * Gets the value from the {@link Max} annotation from the {@link Greeting} class.
     * @param field the field to get the annotation's value from.
     * @return the value of the annotation.
     */
    private long getMaxValueFromAnnotation(final String field) throws NoSuchFieldException {
        final Field classField = Greeting.class.getDeclaredField(field);
        final Max maxClassAnnotation = classField.getAnnotation(Max.class);

        return maxClassAnnotation.value();
    }

    /**
     * Gets the value from the {@link Min} annotation from the {@link Greeting} class.
     * @param field the field to get the annotation's value from.
     * @return the value of the annotation.
     */
    private long getMinValueFromAnnotation(final String field) throws NoSuchFieldException {
        final Field classField = Greeting.class.getDeclaredField(field);
        final Min minClassAnnotation = classField.getAnnotation(Min.class);

        return minClassAnnotation.value();
    }
}
