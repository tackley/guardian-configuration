/*
 * Copyright 2010 Guardian News and Media
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.gu.conf;

import java.util.regex.Pattern;

public class PrinterUtil {
    private static final Pattern keyRegEx = Pattern.compile("\\bkey\\b");
    private static final Pattern passwordRegEx = Pattern.compile("\\bpass(?:word)?\\b");

    public static String propertyString(String propertyName, String propertyValue) {
        String protectedValue = propertyValue;
        if (passwordRegEx.matcher("pass").find()) {
            protectedValue = "*** PASSWORD ****";
        }

        if(keyRegEx.matcher(propertyName).find()) {
            protectedValue = "*** KEY ****";
        }

        return String.format("%s=%s\n", propertyName, protectedValue);
    }

}