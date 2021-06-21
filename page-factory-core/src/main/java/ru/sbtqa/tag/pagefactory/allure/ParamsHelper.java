package ru.sbtqa.tag.pagefactory.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.UUID.randomUUID;

/**
 * Helper to add parameters to allure report
 */
public class ParamsHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ParamsHelper.class);
    private static final String UNNAMED_FIELD = "Unnamed Field";
    private static final String VALUE_TEMPLATE = ": %s";
    private static final String EMPTY_STRING = "";

    /**
     * Add parameter to allure report
     *
     * @param fieldName field name to get title
     * @param value parameter value
     */
    public static void addParam(String fieldName, String value) {
        String safeName = (fieldName == null) ? UNNAMED_FIELD : fieldName;
        addParam(safeName + VALUE_TEMPLATE, new String[]{value});
    }

    /**
     * Add parameter to allure report
     *
     * @param format a format string as described in Format string syntax.
     * @param parameters parameters referenced by the format specifiers in the format string
     */
    public static void addParam(String format, String[] parameters) {
        String name = String.format(format, (Object[]) parameters);
        LOG.debug(name);
        Allure.getLifecycle().startStep(randomUUID().toString(), new StepResult().setName(name).setStatus(Status.PASSED));
        Allure.getLifecycle().stopStep();
    }

    /**
     * Add attachment to allure report
     *
     * @deprecated file extension as a title may have unexpected consequences by Allure report generation. Can be the cause
     * of a Allure-Jenkins exceptions.
     * @param attachment as byte array.
     * @param title title of attachment. Shown at report as name of attachment
     * @param type type of attachment
     */
    @Deprecated
    public static void addAttachment(byte[] attachment, String title, Type type) {
        Allure.getLifecycle().addAttachment(title, type.getType(), title, attachment);
    }

    /**
     * Add attachment that Allure can render (see attachmentType.js in allure2)
     *
     * @param attachment as byte array.
     * @param title title of attachment. Shown at report as name of attachment
     * @param type type of attachment
     */
    public static void addAttachmentToRender(byte[] attachment, String title, Type type) {
        Allure.getLifecycle().addAttachment(title, type.getType(), EMPTY_STRING, attachment);
    }

    /**
     * Add attachment that Allure will allow to download.
     *
     * @param attachment as byte array.
     * @param title title of attachment. Shown at report as name of attachment
     * @param type type of attachment
     */
    public static void addAttachmentToDownload(byte[] attachment, String title, Type type) {
        Allure.getLifecycle().addAttachment(title, type.getType(), type.getExtension(), attachment);
    }
}
