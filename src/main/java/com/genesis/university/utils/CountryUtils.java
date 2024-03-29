package com.genesis.university.utils;

public class CountryUtils {
    public static String capitalizeFirstCharacterOfCountry(String country) {
        country = country.toLowerCase();
        return country.substring(0, 1).toUpperCase() + country.substring(1);
    }
}
