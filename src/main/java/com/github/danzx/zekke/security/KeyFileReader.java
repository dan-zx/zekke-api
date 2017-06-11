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
package com.github.danzx.zekke.security;

import static java.util.Objects.requireNonNull;

import static com.github.danzx.zekke.util.Strings.isNullOrBlank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

/**
 * Reads a file looking for an encoded key.
 *  
 * @author Daniel Pedraza-Arcega
 */
public class KeyFileReader {

    private String classpathFile;

    private KeyFileReader() { }

    /**
     * New KeyFileReader from a classpath file.
     * 
     * @param classpathFile a classpath file.
     * @return a KeyFileReader.
     */
    public static KeyFileReader fromClasspath(String classpathFile) {
        KeyFileReader keyFileReader = new KeyFileReader();
        keyFileReader.classpathFile = requireNonNull(classpathFile);
        return keyFileReader;
    }

    /** @return reads and parses the key as a byte array. */
    public byte[] getKey() {
        try (BufferedReader reader = openReader()) {
            String line = reader.readLine();
            if (isNullOrBlank(line)) throw new IllegalArgumentException("Key file is empty");
            int lineLength = line.length();
            byte[] encodedBytes = new byte[(lineLength + Byte.SIZE - 1) / Byte.SIZE];
            char c;
            for (int i = 0; i < lineLength; i++) {
                if ((c = line.charAt(i)) == '1') encodedBytes[i / Byte.SIZE] = (byte) (encodedBytes[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
                else if (c != '0') throw new IllegalArgumentException("Invalid key format");
            }
            return Base64.getDecoder().decode(encodedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedReader openReader() {
        InputStream fileStream = KeyFileReader.class.getClassLoader().getResourceAsStream(classpathFile);
        if (fileStream == null) throw new IllegalArgumentException("File cannot be read from " + classpathFile);
        return new BufferedReader(new InputStreamReader(fileStream));
    }
}
