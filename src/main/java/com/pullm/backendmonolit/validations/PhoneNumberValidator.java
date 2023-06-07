package com.pullm.backendmonolit.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
      "^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    if (phoneNumber == null) {
      return true;
    }
    return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
  }
}
