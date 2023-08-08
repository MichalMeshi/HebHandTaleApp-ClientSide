package com.example.test1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unicode{

    public static String decodeUnicode(String input) {
        Pattern pattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher matcher = pattern.matcher(input);

        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            char ch = (char) Integer.parseInt(matcher.group(1), 16);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(String.valueOf(ch)));
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }
}
