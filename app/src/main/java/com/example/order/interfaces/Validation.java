package com.example.order.interfaces;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Validation {
    static boolean Validate(String text, String expression){
        final String regex = expression; //"(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
        final String string = text;
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
}
