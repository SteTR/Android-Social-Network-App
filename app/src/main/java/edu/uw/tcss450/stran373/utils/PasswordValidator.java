package edu.uw.tcss450.stran373.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 *
 * Combinator Password validator (not in use)
 * @author Steven Tran
 * @deprecated
 */
public interface PasswordValidator extends Function<String, Optional<PasswordValidator.ValidationResult>> {

    enum ValidationResult {
        SUCCESS,
        FAILURE_LESS_THAN_REQUIRED_CHAR,
        FAILURE_NO_MATCH,
        FAILURE_MISSING_DIGIT,
        FAILURE
    }

    /**
     * Returns a validator that when applied will validate that the String contains at least
     * one uppercase letter.
     *
     * When a String s is applied to the returning validator, it will evaluate to an Optional
     * containing ValidationResult.SUCCESS when s contains at least one uppercase letter,
     * otherwise ValidationResult.PWD_MISSING_UPPER.
     *
     * @return a validator that validates that the String contains an uppercase letter
     */
    static PasswordValidator checkPwdUpperCase() {
        return password ->
                Optional.of(checkStringContains(password, Character::isUpperCase) ?
                        ValidationResult.SUCCESS : ValidationResult.FAILURE);
    }

    /**
     * Returns a validator that when applied will validate that the String contains at least
     * one lowercase letter.
     *
     * When a String s is applied to the returning validator, it will evaluate to an Optional
     * containing ValidationResult.SUCCESS when s contains at least one lowercase letter,
     * otherwise ValidationResult.PWD_MISSING_LOWER.
     *
     * @return a validator that validates that the String contains an lowercase letter
     */
    static PasswordValidator checkPwdLowerCase() {
        return password ->
                Optional.of(checkStringContains(password, Character::isLowerCase) ?
                        ValidationResult.SUCCESS : ValidationResult.FAILURE);
    }

    /**
     * Returns a validator that will check if the length of a string is greater than minLength set
     *
     * The validator will return a Optional containing either success or invalid length
     *
     * @param minLength length to be checked for greater than
     *
     * @return validator that checks if length of string is greater
     */
    static PasswordValidator checkPasswordGreater(int minLength)
    {
        return password -> Optional.of(password.length() > minLength ?
                ValidationResult.SUCCESS : ValidationResult.FAILURE_LESS_THAN_REQUIRED_CHAR);
    }

    /**
     * Returns a validator that when applied will validate that the String contains at least
     * one of these special characters: "@#$%&*!?".
     *
     * When a String s is applied to the returning validator, it will evaluate to an Optional
     * containing ValidationResult.SUCCESS when s contains at least one of these special
     * characters: "@#$%&*!?", otherwise ValidationResult.PWD_MISSING_SPECIAL.
     *
     * @return a validator that validates that the String contains a special character
     */
    static PasswordValidator checkPwdSpecialChar() {
        return checkPwdSpecialChar("@#$%&*!?");
    }

    /**
     * Returns a validator that when applied will validate that the String contains at least
     * one of the characters found in specialChars.
     *
     * When a String s is applied to the returning validator, it will evaluate to an Optional
     * containing ValidationResult.SUCCESS when s contains at least one of the
     * characters found in specialChars, otherwise ValidationResult.PWD_MISSING_SPECIAL.
     *
     * @param specialChars the characters to look for
     * @return a validator that validates that the String contains a special character
     */
    static PasswordValidator checkPwdSpecialChar(String specialChars) {
        return password ->
                Optional.of(checkStringContains(password,
                        c -> specialChars.contains(Character.toString((char) c))) ?
                        ValidationResult.SUCCESS : ValidationResult.FAILURE);
    }

    /**
     * Returns a validator that will check if the password is the same as the otherPassword
     *
     * @param otherPassword other password to be checked against
     * @return a validator that will check against the other password to see if they're equal
     */
    static PasswordValidator checkPasswordEqual(String otherPassword)
    {
        return password -> Optional.of(password.equals(otherPassword) ? ValidationResult.SUCCESS : ValidationResult.FAILURE_NO_MATCH);
    }

    /**
     * Helper, determines if the String check contains at least one character that test
     * evaluates true for.
     *
     * @param check The String to test
     * @param test the character test
     * @return true if check contains at least one character that test evaluates true for,
     * false otherwise
     */
    static boolean checkStringContains(String check, IntPredicate test) {
        return check.chars().filter(test).count() > 0;
    }

    /**
     * Returns validator that checks if password has at least one digit
     *
     * @return a validator that will check for password digits
     */
    static PasswordValidator checkPasswordHasDigit() {
        return password ->
                Optional.of(checkStringContains(password, Character::isDigit) ?
                        ValidationResult.SUCCESS : ValidationResult.FAILURE_MISSING_DIGIT);
    }

    /**
     * Returns a composed PasswordValidator that represents a short-circuiting logical AND of
     * this PasswordValidator and another.  When evaluating the composed PasswordValidator,
     * if this PasswordValidator in not successful, then the other PasswordValidator is not
     * evaluated.
     *
     * NOTE: THIS is the Combinator!
     *
     * @param other a PasswordValidator that will be logically-ANDed with this
     *      PasswordValidator
     * @return a composed PasswordValidator that represents a short-circuiting logical AND of
     *      this PasswordValidator and another
     */
    default PasswordValidator and(PasswordValidator other)
    {
        return password -> {
            return this.apply(password).flatMap(result -> result == ValidationResult.SUCCESS ? other.apply(password) : Optional.of(result));
        };
    }

    /**
     * Returns a validator that when applied will validate a String based on theTest Predicate.
     *
     * When a String s is applied to the returning validator, it will evaluate to an Optional
     * containing ValidationResult.SUCCESS when s passes theTest predicate, otherwise
     * ValidationResult.PWD_CLIENT_ERROR.
     *
     * @param theTest a predicate to test the String for validation
     * @return a validator that validates that the String based on theTest Predicate
     */
    static PasswordValidator checkClientPredicate(Predicate<String> theTest) {
        return password ->
                Optional.of(theTest.test(password) ?
                        ValidationResult.SUCCESS : ValidationResult.FAILURE);
    }

    /**
     * This helper method is a work around used since Android does not support java language
     * features introduced after Java 1.8. The Optional class introduced several helpful methods
     * in Java 1.9 that should be used here instead of this.
     * @param result the result of a validation action
     * @param onSuccess the action to take when the password successfully validates
     * @param onError the action to take when the password unsuccessfully validates
     */
    default void processResult(Optional<PasswordValidator.ValidationResult> result,
                               Runnable onSuccess,
                               Consumer<ValidationResult> onError) {
        if (result.isPresent()) {
            if (result.get() == PasswordValidator.ValidationResult.SUCCESS) {
                onSuccess.run();
            } else {
                onError.accept(result.get());
            }
        } else {
            throw new IllegalStateException("Nothing to process");
        }
    }
}
