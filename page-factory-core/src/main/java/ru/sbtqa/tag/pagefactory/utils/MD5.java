package ru.sbtqa.tag.pagefactory.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
    public static String hash(String data) {
            return DigestUtils.md5Hex(data).toLowerCase();
    }
}
