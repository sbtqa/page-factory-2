//package ru.sbtqa.tag.pagefactory.transformer.enums;
//
//import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
//import cucumber.runtime.CucumberException;
//import ru.sbtqa.tag.pagefactory.transformer.ClickVariationTransformer;
//
//@XStreamConverter(ClickVariationTransformer.class)
//public enum ClickVariation {
//    DOUBLE_CLICK("двойным кликом"),
//    CLICK(null);
//
//    private final String name;
//
//    ClickVariation(String name) {
//        this.name = name;
//    }
//
//    public String getValue() {
//        return name;
//    }
//
//    public static ClickVariation fromString(String name) {
//        if (name == null) {
//            return ClickVariation.CLICK;
//        }
//        if ("double-click".equalsIgnoreCase(name.trim()) || ClickVariation.DOUBLE_CLICK.getValue().equalsIgnoreCase(name.trim())) {
//            return ClickVariation.DOUBLE_CLICK;
//        }
//        throw new CucumberException("Incorrect enum-value in steps: " + name);
//    }
//}
