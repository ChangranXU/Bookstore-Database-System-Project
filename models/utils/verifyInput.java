package models.utils;

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
}

