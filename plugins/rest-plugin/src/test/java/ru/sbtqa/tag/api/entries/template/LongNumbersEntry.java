package ru.sbtqa.tag.api.entries.template;

import org.junit.Assert;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import java.math.BigDecimal;
import java.math.BigInteger;

@Endpoint(method = Rest.POST, path = "client/typed-arrays", title = "long numbers", template = "templates/LongNumbers.json", shouldRemoveEmptyObjects = true)
public class LongNumbersEntry extends EndpointEntry {

    @Body(name = "int")
    private Integer _int = null;

    @Body(name = "intMin")
    private Integer intMin = null;

    @Body(name = "intNeg")
    private Integer intNeg = null;

    @Body(name = "long")
    private Long _long = null;

    @Body(name = "longMin")
    private Long longMin = null;

    @Body(name = "longNeg")
    private Long longNeg = null;

    @Body(name = "bigInt")
    private BigInteger bigInt = null;

    @Body(name = "bigIntMin")
    private BigInteger bigIntMin = null;

    @Body(name = "float")
    private Float _float = null;

    @Body(name = "floatMin")
    private Float floatMin = null;

    @Body(name = "floatNeg")
    private Float floatNeg = null;

    @Body(name = "double")
    private Double _double = null;

    @Body(name = "doubleMin")
    private Double doubleMin = null;

    @Body(name = "bigDec")
    private BigDecimal bigDec = null;

    @Body(name = "bigDecMin")
    private BigDecimal bigDecMin = null;

    @Validation(title = "result")
    public void validate() {
        String expected = "{\n" +
            "  \"int\": 2147483647,\n" +
            "  \"intMin\": -2147483648,\n" +
            "  \"intNeg\": 0,\n" +
            "  \"long\": 9223372036854775807,\n" +
            "  \"longMin\": -9223372036854775808,\n" +
            "  \"longNeg\": 0,\n" +
            "  \"bigInt\": 99999999999999999999999999999999999,\n" +
            "  \"bigIntMin\": -99999999999999999999999999999999999,\n" +
            "  \"float\": 9.223372E+18,\n" +
            "  \"floatMin\": -9.223372E+18,\n" +
            "  \"double\": 9.223372036854776E+18,\n" +
            "  \"doubleMin\": -9.223372036854776E+18,\n" +
            "  \"bigDec\": 99999999999999999999999999999999999.85555565,\n" +
            "  \"bigDecMin\": -99999999999999999999999999999999999.85555565\n" +
            "}";

        Assert.assertEquals("Json from template does not match expected", getBody(), expected);
    }
}