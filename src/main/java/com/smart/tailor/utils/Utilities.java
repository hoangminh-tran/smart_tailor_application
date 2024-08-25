package com.smart.tailor.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private static final Set<Integer> usedNumbers = new HashSet<>();

    public static boolean isNonNullOrEmpty(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        return !str.isEmpty() && !str.isBlank();
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-+])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidAlphabeticString(String input) {
        String alphabeticRegex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(alphabeticRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean isValidVietnamesePhoneNumber(String phoneNumber) {
        String vietnamesePhoneNumberRegex = "^0\\d{9}$";
        Pattern pattern = Pattern.compile(vietnamesePhoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidDate(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); // Đặt giá trị này để không cho phép chấp nhận các ngày không hợp lệ
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String generateRandomNumber() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static boolean isValidNumber(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isStringNotNullOrEmpty(String str) {
        if (Optional.ofNullable(str).isPresent()) {
            return isNonNullOrEmpty(str);
        }
        return false;
    }


    public static boolean isValidateDate(String dateStr, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        try {
            formatter.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidBoolean(Boolean bool) {
        if (bool == null) return false;
        try {
            Boolean.parseBoolean(bool.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidFloat(Float floatType) {
        if (floatType == null) return false;
        try {
            Float.parseFloat(floatType.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidInteger(Integer integerType) {
        if (integerType == null) return false;
        try {
            Integer.parseInt(integerType.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static byte[] encodeStringToBase64(String stringToCode) {
        byte[] bytesToEncode = stringToCode.getBytes();
        return Base64.getEncoder().encode(bytesToEncode);
    }

    public static String decodeBase64ToString(byte[] base64Bytes) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Bytes);
        return new String(decodedBytes);
    }

//    public static int convertStringToInt(String StringStr) {
//        if (StringStr.equals("null")) {
//            return 000000;
//        }
//        MessageDigest md = null;
//        try {
//            md = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        byte[] hash = md.digest(StringStr.getBytes(StandardCharsets.UTF_8));
//        BigInteger number = new BigInteger(1, hash);
//        int intValue = number.mod(BigInteger.valueOf(1000000)).intValue();
//
//        // Kiểm tra xung đột và tạo lại nếu cần
//        while (usedNumbers.contains(intValue)) {
//            StringStr = Utilities.generateCustomPrimaryKey().toString();
//            hash = md.digest(StringStr.getBytes(StandardCharsets.UTF_8));
//            number = new BigInteger(1, hash);
//            intValue = number.mod(BigInteger.valueOf(1000000)).intValue();
//        }
//
//        usedNumbers.add(intValue);
//        return intValue;
//    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTimeFormatter.format(localDateTime);
    }

    public static int roundToNearestHalf(double value) {
        double fractionalPart = value - Math.floor(value);
        if (fractionalPart < 0.5) {
            return (int) Math.floor(value);
        } else {
            return (int) Math.ceil(value);
        }
    }

    public static String generateCustomPrimaryKey() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        StringBuilder customerPrimaryKey = new StringBuilder();

        // Get current time and microseconds
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmssSSSSSS");
        String timeString = now.format(formatter);

        // Generate and append components
        customerPrimaryKey.append(generateRandomChars(chars, 2, random));
        customerPrimaryKey.append(timeString.substring(0, 2)); // HH
        customerPrimaryKey.append(generateRandomChars(chars, 2, random));
        customerPrimaryKey.append(timeString.substring(2, 4)); // mm
        customerPrimaryKey.append(generateRandomChars(chars, 2, random));
        customerPrimaryKey.append(timeString.substring(4, 6)); // ss
        customerPrimaryKey.append(selectRandomMicroChars(timeString.substring(6, 12), random));

        return customerPrimaryKey.toString();
    }

    private static String generateRandomChars(String chars, int length, Random random) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }

    private static String selectRandomMicroChars(String microseconds, Random random) {
        char randChar1 = microseconds.charAt(random.nextInt(6));
        char randChar2;
        do {
            randChar2 = microseconds.charAt(random.nextInt(6));
        } while (randChar2 == randChar1);
        return "" + randChar1 + randChar2;
    }

    public static boolean isValidCustomKey(String value) {
        Pattern pattern = Pattern.compile(
                "^[a-zA-Z0-9]{2}" +     // 2 ký tự ngẫu nhiên từ chars
                        "\\d{2}" +              // HH
                        "[a-zA-Z0-9]{2}" +      // 2 ký tự ngẫu nhiên từ chars
                        "[0-5][0-9]" +          // mm (00-59)
                        "[a-zA-Z0-9]{2}" +      // 2 ký tự ngẫu nhiên từ chars
                        "[0-5][0-9]" +          // ss (00-59)
                        "[0-9]{2}" +            // 2 ký tự từ microseconds, bất kỳ
                        "$"
        );
        return pattern.matcher(value).matches();
    }

    public static BigDecimal roundToNearestThousand(BigDecimal value) {
        // Truncate the decimal part
        value = value.setScale(0, RoundingMode.DOWN);
        BigDecimal thousand = BigDecimal.valueOf(1000);
        BigDecimal remainder = value.remainder(thousand);
        if (remainder.compareTo(BigDecimal.valueOf(500)) >= 0) {
            return value.subtract(remainder).add(thousand);
        } else {
            return value.subtract(remainder);
        }
    }
}
