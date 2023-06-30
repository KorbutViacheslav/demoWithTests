package com.example.demowithtests.util.annotations.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class BlockedEmailDomainsValidator implements ConstraintValidator<BlockedEmailDomains, String> {

    private String[] domains;
    private String[] blockedWords;

    @Override
    public void initialize(BlockedEmailDomains constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        domains = constraintAnnotation.contains();
        blockedWords = constraintAnnotation.blockedWords();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null)
            return true;
        boolean containsBlockedDomain = Arrays.stream(domains).anyMatch(email::endsWith);

        boolean containsBlockedWord = Arrays.stream(blockedWords)
                .anyMatch(word -> email.toLowerCase().contains(word));

        return !containsBlockedDomain && !containsBlockedWord;
    }
}
