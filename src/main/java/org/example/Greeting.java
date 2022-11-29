package org.example;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class Greeting {

    @Min(value = 0)
    @Max(value = 100)
    Integer limit;

    public Greeting(final Integer limit) {
        this.limit = limit;
    }
}
