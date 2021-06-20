package ru.sbtqa.tag.api;

import java.io.OutputStream;
import java.io.PrintStream;
import org.slf4j.Logger;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.allure.Type;

/**
 * A wrapper class which takes a logger as constructor argument and offers a PrintStream whose flush
 * method writes the written content to the supplied logger (debug level).
 * <p>
 * Usage:<br>
 * initializing in @BeforeClass of the unit test:
 * <pre>
 *          ToLoggerPrintStream loggerPrintStream = new ToLoggerPrintStream( myLog );
 *          RestAssured.config = RestAssured.config().logConfig(
 *                                 new LogConfig( loggerPrintStream.getPrintStream(), true ) );
 * </pre>
 * will redirect all log outputs of a ValidatableResponse to the supplied logger:
 * <pre>
 *             resp.then().log().all( true );
 * </pre>
 */
public class ToLoggerPrintStream {

    /**
     * Logger for this class
     */
    private final Logger myLog;
    private PrintStream myPrintStream;

    /**
     * Constructor
     *
     * @param aLogger
     */
    public ToLoggerPrintStream(Logger aLogger) {
        super();
        myLog = aLogger;
    }

    /**
     * @return printStream
     */
    public PrintStream getPrintStream() {
        if (myPrintStream == null) {
            OutputStream output = new OutputStream() {
                private StringBuilder myStringBuilder = new StringBuilder();

                @Override
                public void write(int b) {
                    this.myStringBuilder.append((char) b);
                }

                /**
                 * @see java.io.OutputStream#flush()
                 */
                @Override
                public void flush() {
                    String dispatch = this.myStringBuilder.toString();

                    // ALLURE
                    if (!dispatch.isEmpty() && !dispatch.trim().isEmpty()) {
                        ParamsHelper.addAttachmentToRender(dispatch.getBytes(),
                                (dispatch.startsWith("Request") ? "request" : "response"), Type.TEXT);
                    }

                    // LOGGING
                    myLog.debug(dispatch);
                    myStringBuilder = new StringBuilder();
                }
            };

            myPrintStream = new PrintStream(output, true);  // true: autoflush must be set!
        }

        return myPrintStream;
    }
}