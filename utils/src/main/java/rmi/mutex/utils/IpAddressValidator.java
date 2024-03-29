package rmi.mutex.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAddressValidator {
    private final Pattern pattern;

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public IpAddressValidator() {
        pattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    public boolean validate(final String ip) {
        Matcher matcher = pattern.matcher(ip);
        return !matcher.matches();
    }
}