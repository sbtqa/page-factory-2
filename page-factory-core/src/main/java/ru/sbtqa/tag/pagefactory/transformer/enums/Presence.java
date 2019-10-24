//package ru.sbtqa.tag.pagefactory.transformer.enums;
//
//import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
//import cucumber.runtime.CucumberException;
//import ru.sbtqa.tag.pagefactory.transformer.PresenceTransformer;
//
//@XStreamConverter(PresenceTransformer.class)
//public enum Presence {
//    NEGATIVE("от"),
//    POSITIVE("при");
//
//    private final String name;
//
//    Presence(String name) {
//        this.name = name;
//    }
//
//    public String getValue() {
//        return name;
//    }
//
//    public static Presence fromString(String name) {
//        String nameTrim = name.trim();
//        if (nameTrim.startsWith("присутству") || nameTrim.equals("is")) {
//            return Presence.POSITIVE;
//        }
//        if (nameTrim.startsWith("не отображ") || nameTrim.equals("not")) {
//            return Presence.NEGATIVE;
//        }
//        for (Presence value : Presence.values()) {
//            if (value.name.equalsIgnoreCase(nameTrim.trim())) {
//                return value;
//            }
//        }
//        throw new CucumberException("Incorrect enum-value in steps:" + nameTrim);
//    }
//}
