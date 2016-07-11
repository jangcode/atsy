package com.epam.rft.atsy.service.passwordchange.validation.impl;

import com.epam.rft.atsy.service.domain.PasswordChangeDTO;
import com.epam.rft.atsy.service.passwordchange.validation.PasswordValidationRule;

import java.util.regex.Pattern;

public class PasswordContainsRule implements PasswordValidationRule {
    private static final String MESSAGE_KEY = "passwordchange.validation.contains";

    private static final Pattern LETTER_PATTERN =
            Pattern.compile(".*[a-zA-Z]+.*");

    private static final Pattern NUMBER_PATTERN =
            Pattern.compile(".*[0-9]+.*");

    private static final Pattern SPECIAL_CHARACTER_PATTERN =
            Pattern.compile(".*[!@#$%^&_.,;:-]+.*");

    @Override
    public boolean isValid(PasswordChangeDTO passwordChangeDTO) {
        String newPassword = passwordChangeDTO.getNewPassword();

        return containsLetters(newPassword) &&
                containsNumbers(newPassword) &&
                containsSpecial(newPassword);
    }

    private boolean containsLetters(String password){
        return LETTER_PATTERN.matcher(password).matches();
    }

    private boolean containsNumbers(String password) {
        return NUMBER_PATTERN.matcher(password).matches();
    }

    private boolean containsSpecial(String password) {
        return SPECIAL_CHARACTER_PATTERN.matcher(password).matches();
    }

    @Override
    public String getErrorMessageKey() {
        return MESSAGE_KEY;
    }
}
