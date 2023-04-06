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

    public static boolean isValidUid(String uid) {
        // Check if the string is non-empty and has a length of at most 10
        if (uid != null && !uid.isEmpty() && uid.length() <= 10) {
            return true;
        }
        return false;
    }
    
    public static boolean isValidName(String name) {
        // Check if the string is non-empty and has a length of at most 50
        if (name != null && !name.isEmpty() && name.length() <= 50) {
                return true;
        }
        return false;
    }

    public static boolean isValidAddress(String address) {
        // Check if the string is non-empty and has a length of at most 200
        if (address != null && !address.isEmpty() && address.length() <= 200) {
                    return true;
        }
        return false;
    }

    public static boolean isLegalInput(String input) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9\\s\\-\\,\\;\\:\\.\\(\\)\\[\\]\\{\\}\\/\\?\\!\\@\\#\\$\\^\\&\\*\\+\\=\\|\\~\\`\\'\\\"]+");
        Matcher m = p.matcher(input);
        boolean b=m.find();
        return !b;
    }
}

