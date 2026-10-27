package com.company.framework.utils;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * TestDataUtil — Generates realistic fake test data using JavaFaker.
 * Usage: TestDataUtil.randomEmail()
 */
public class TestDataUtil {

    private static final Faker faker = new Faker(Locale.ENGLISH);

    private TestDataUtil() {}

    public static String randomFirstName()  { return faker.name().firstName(); }
    public static String randomLastName()   { return faker.name().lastName(); }
    public static String randomFullName()   { return faker.name().fullName(); }
    public static String randomEmail()      { return faker.internet().emailAddress(); }
    public static String randomPassword()   { return faker.internet().password(8, 16, true, true); }
    public static String randomPhone()      { return faker.phoneNumber().cellPhone(); }
    public static String randomCity()       { return faker.address().city(); }
    public static String randomAddress()    { return faker.address().streetAddress(); }
    public static String randomZipCode()    { return faker.address().zipCode(); }
    public static String randomCompany()    { return faker.company().name(); }
    public static int    randomNumber(int min, int max) { return faker.number().numberBetween(min, max); }
    public static String randomAlphanumeric(int length) { return faker.bothify("?" .repeat(length)); }
}
