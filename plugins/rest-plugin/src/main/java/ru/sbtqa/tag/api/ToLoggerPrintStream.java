package ru.sbtqa.tag.api;

import java.io.ByteArrayOutputStream;
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
    private static class LoggerOutputStream extends ByteArrayOutputStream {
        private final Logger logger;

        public LoggerOutputStream(Logger logger) {
            this.logger = logger;
        }

        @Override
        public void flush() {
            String dispatch = this.toString();

            // ALLURE
            if (!dispatch.isEmpty() && !dispatch.trim().isEmpty()) {
                ParamsHelper.addAttachmentToRender(dispatch.getBytes(),
                        (dispatch.startsWith("Request") ? "request" : "response"), Type.TEXT);
            }

            // LOGGING
            logger.debug(dispatch);
            this.reset();
            this.buf = new byte[32];
        }
    }

    private final Logger logger;
    private PrintStream myPrintStream;

    public ToLoggerPrintStream(Logger logger) {
        super();
        this.logger = logger;
    }

    public PrintStream getPrintStream() {
        if (myPrintStream == null) {
            OutputStream output = new LoggerOutputStream(logger);

            myPrintStream = new PrintStream(output, true); // true: autoflush must be set!
        }

        return myPrintStream;
    }
}