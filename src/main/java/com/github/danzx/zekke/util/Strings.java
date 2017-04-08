/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.danzx.zekke.util;

import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

/**
 * Various string utilities.
 *
 * @author Daniel Pedraza-Arcega
 */
public class Strings {

    public static final String EMPTY = "";
    public static final String BLANK_SPACE = " ";
    public static final String TAB = "\t";
    public static final String NEW_LINE = "\n";

    private static final char DOUBLE_QUOTES = '"';
    private static final char UNDERSCORE = '_';

    private Strings() {
        throw new AssertionError();
    }

    /**
     * @param s the string to check.
     * @return if the string is blank.
     */
    public static boolean isBlank(String s) {
        return s.trim().isEmpty();
    }

    /** 
     * Converts a string in ALL_CAPS into camelCase or CamelCase.
     * 
     * @param allCapsStr a string in ALL_CAPS case.
     * @param isUpperCamelCase if is upper or lower camel case.
     * @return the same string in camelCase or CamelCase.
     */
    public static String allCapsToCamelCase(String allCapsStr, boolean isUpperCamelCase) {
        if (isBlank(allCapsStr)) return allCapsStr;
        StringBuilder camelCaseBuilder = new StringBuilder();
        char[] allCapsChars = allCapsStr.toCharArray();
        boolean lower = false;
        for (int i = 0; i < allCapsChars.length; i++) {
            char character = allCapsChars[i];
            if (character == UNDERSCORE) lower = false;
            else if (lower) camelCaseBuilder.append(toLowerCase(character));
            else {
                if (!isUpperCamelCase && camelCaseBuilder.length() == 0) camelCaseBuilder.append(toLowerCase(character));
                else camelCaseBuilder.append(toUpperCase(character));
                lower = true;
            }
        }
        return camelCaseBuilder.toString();
    }

    /**
     * @param a string. 
     * @return the given string between double quotes.
     */
    public static String quoted(String s) {
        return s == null ? null : DOUBLE_QUOTES + s + DOUBLE_QUOTES;
    }
}
