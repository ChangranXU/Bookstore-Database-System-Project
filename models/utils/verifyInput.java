package models.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class verifyInput {
    public static boolean isPositiveInteger(String input) {
        try {
            int number = Integer.parseInt(input);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isISBN(String input) {
        return input.matches("\\d{1}-\\d{4}-\\d{4}-\\d{1}");
    }

    public static boolean isLegalInput(String input) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9\\s\\-\\,\\;\\:\\.\\(\\)\\[\\]\\{\\}\\/\\?\\!\\@\\#\\$\\^\\&\\*\\+\\=\\|\\~\\`\\'\\\"]+");
        Matcher m = p.matcher(input);
        boolean b=m.find();
        return !b;
    }
}

