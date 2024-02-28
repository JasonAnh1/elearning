package com.jason.elearning.util;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Util {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static int PAGE_SIZE = 20;
    public static String VEXERE_FORMAT = "yyyy-MM-dd";

    public static String formatNumber(double d) {
        DecimalFormat formatter = new DecimalFormat("###,###,###,###.###");
        try {
            return formatter.format(d);
        } catch (Exception e) {
            return d + "";
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T mergeObjects(final T first, final T second)
            throws IllegalAccessException, InstantiationException {
        Class<?> clazz = first.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Object returnValue = clazz.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value1 = field.get(first);
            Object value2 = field.get(second);
            Object value = value1 != null ? value1 : value2;
            field.set(returnValue, value);
        }
        return (T) returnValue;
    }

    public static Date convertStringToDate1(final String date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
//		String pattern = "yyyy-MM-dd HH:mm:ss.SSS";

        try {
            DateFormat df = new SimpleDateFormat(pattern);
            Date date1 = df.parse(date);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(Date departureDate, Date landDate) {
        String pattern1 = "yyyy-MM-dd HH:mm";
        String pattern2 = "HH:mm";

        DateFormat df1 = new SimpleDateFormat(pattern1);
        DateFormat df2 = new SimpleDateFormat(pattern2);
        String dateString1 = df1.format(departureDate);
        String dateString2 = df2.format(landDate);
        return dateString1 + " " +dateString2;
    }

    public static Date convertStringToDate(final String date) {
        String pattern = "dd/MM/yyyy";

        try {
            DateFormat df = new SimpleDateFormat(pattern);
            Date date1 = df.parse(date);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertStringToDateVNPay(final String date) {
        String pattern = "yyyyMMddHHmmss";

        try {
            DateFormat df = new SimpleDateFormat(pattern);
            Date date1 = df.parse(date);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertXMLToDate(final XMLGregorianCalendar xmlCalendar) {
        return xmlCalendar.toGregorianCalendar().getTime();
    }

    public static String convertDateToString(final Date date) {
        String pattern = "dd/MM/yyyy";

        DateFormat df = new SimpleDateFormat(pattern);
        String dateString = df.format(date);
        return dateString;
    }

    public static String convertDateToString(final Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        String dateString = df.format(date);
        return dateString;
    }

    public static String convertDateToStringGloble(final Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        String dateString = df.format(date);
        return dateString;
    }

    public static String convertDateToString1(final Date date) {
        String pattern = "yyyyMMddHHmmss";

        DateFormat df = new SimpleDateFormat(pattern);
        String dateString = df.format(date);
        return dateString;
    }

    public static XMLGregorianCalendar convertStringToXMLGregorianCalendar(final String str) {
        if (str == null) {
            return null;
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(str);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);

            XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

            return xmlGregCal;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final Pattern[] inputRegexes = new Pattern[4];

    static {
        inputRegexes[0] = Pattern.compile(".*[A-Z].*");
        inputRegexes[1] = Pattern.compile(".*[a-z].*");
        inputRegexes[2] = Pattern.compile(".*\\d.*");
        inputRegexes[3] = Pattern.compile(".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*");
    }

    public static boolean isMatchingRegex(String input) {
        boolean inputMatches = true;
        for (Pattern inputRegex : inputRegexes) {
            if (!inputRegex.matcher(input).matches()) {
                inputMatches = false;
            }
        }
        return inputMatches;
    }

    public static String randomAlphaNumeric(int count, String type) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return type.concat(builder.toString());
    }

    public static class VNPay {

        public static int MULTIPLE = 100;

        public static String md5(String message) {
            String digest = null;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hash = md.digest(message.getBytes("UTF-8"));
                // converting byte array to Hexadecimal String
                StringBuilder sb = new StringBuilder(2 * hash.length);
                for (byte b : hash) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                digest = sb.toString();
            } catch (UnsupportedEncodingException ex) {
                digest = "";
                // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
                // null, ex);
            } catch (NoSuchAlgorithmException ex) {
                // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
                // null, ex);
                digest = "";
            }
            return digest;
        }

        public static String Sha256(String message) {
            String digest = null;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(message.getBytes("UTF-8"));

                // converting byte array to Hexadecimal String
                StringBuilder sb = new StringBuilder(2 * hash.length);
                for (byte b : hash) {
                    sb.append(String.format("%02x", b & 0xff));
                }

                digest = sb.toString();

            } catch (UnsupportedEncodingException ex) {
                digest = "";
                // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
                // null, ex);
            } catch (NoSuchAlgorithmException ex) {
                // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
                // null, ex);
                digest = "";
            }
            return digest;
        }

        //Util for VNPAY
        public static String hashAllFields(Map fields, String vnp_HashSecret) throws UnsupportedEncodingException {
            // create a list and sort it
            List fieldNames = new ArrayList(fields.keySet());
            Collections.sort(fieldNames);
            // create a buffer for the md5 input and add the secure secret first
            StringBuilder sb = new StringBuilder();
            sb.append(vnp_HashSecret);
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) fields.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    sb.append(fieldName);
                    sb.append("=");
                    sb.append(URLDecoder.decode(fieldValue,"UTF-8"));
                }
                if (itr.hasNext()) {
                    sb.append("&");
                }
            }
            return Sha256(sb.toString());
        }

        public static String getIpAddress(HttpServletRequest request) {
            String ipAdress;
            try {
                ipAdress = request.getHeader("X-FORWARDED-FOR");
                if (ipAdress == null) {
                    ipAdress = request.getRemoteAddr();
                }
            } catch (Exception e) {
                ipAdress = "Invalid IP:" + e.getMessage();
            }
            return ipAdress;
        }

        public static String getRandomNumber(int len) {
            Random rnd = new Random();
            String chars = "0123456789";
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(chars.charAt(rnd.nextInt(chars.length())));
            }
            return sb.toString();
        }
    }


}
