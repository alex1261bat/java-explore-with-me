package ru.practicum.config;

import org.apache.commons.lang3.time.DateUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.Date;

public class DateValidatorInner implements ConstraintValidator<DateValidator, String> {
    private String dateFormat;

    @Override
    public void initialize(DateValidator constraintAnnotation) {
        this.dateFormat = constraintAnnotation.dateFormat();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ((value == null) || (value.isEmpty())) return true;
        try {
            Date date = DateUtils.parseDate(value, dateFormat);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }
}
