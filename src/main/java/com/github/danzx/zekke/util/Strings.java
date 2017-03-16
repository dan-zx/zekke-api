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
     * Converts a string in ALL_CAPS into PascalCase.
     * 
     * @param allCapsStr a string in ALL_CAPS case.
     * @return the same string in PascalCase.
     */
    public static String allCapsToPascalCase(String allCapsStr) {
        if (isBlank(allCapsStr)) return allCapsStr;
        StringBuilder pascalCaseBuilder = new StringBuilder();
        char[] allCapsChars = allCapsStr.toCharArray();
        boolean wasPreviousCharUnderscore = false;
        for (int i = 0; i < allCapsChars.length; i++) {
            if (wasPreviousCharUnderscore) {
                if (allCapsChars[i] == UNDERSCORE) continue;
                pascalCaseBuilder.append(allCapsChars[i]);
                wasPreviousCharUnderscore = false;
            } else {
                if (allCapsChars[i] != UNDERSCORE && i != 0) pascalCaseBuilder.append(toLowerCase(allCapsChars[i]));
                else if (allCapsChars[i] == UNDERSCORE) wasPreviousCharUnderscore = true;
                else pascalCaseBuilder.append(allCapsChars[i]);
            }
        }
        return pascalCaseBuilder.toString();
    }

    /**
     * @param a string. 
     * @return the given string between double quotes.
     */
    public static String quoted(String s) {
        return s == null ? null : DOUBLE_QUOTES + s + DOUBLE_QUOTES;
    }
}
