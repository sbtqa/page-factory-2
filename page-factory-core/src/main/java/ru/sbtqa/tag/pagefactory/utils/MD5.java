package ru.sbtqa.tag.pagefactory.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String hash(String data) throws NoSuchAlgorithmException {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data.getBytes());
            return DatatypeConverter.printHexBinary(md5.digest()).toLowerCase();
    }
}
