package com.markovejnovic.Ikap;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.ByteArrayOutputStream;

public class RequestPageGenerator {
    public static byte[] GenerateSuccess() {
        JtwigTemplate template = JtwigTemplate
                .classpathTemplate("templates/success.twig");
        JtwigModel model = JtwigModel.newModel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        template.render(model, baos);
        return baos.toByteArray();
    }

    public static byte[] GenerateError(InvalidityType type) {
        JtwigTemplate template = JtwigTemplate
                .classpathTemplate("templates/error.twig");

        JtwigModel model = JtwigModel.newModel();
        if (type == InvalidityType.EMAIL) {
            model.with("invalidEmail", true);
        } else if (type == InvalidityType.DATE) {
            model.with("invalidDate", true);
        } else if (type == InvalidityType.DATE_TAKEN) {
            model.with("dateTaken", true);
        } else if (type == InvalidityType.START_TIME) {
            model.with("invalidStartTime", true);
        } else if (type == InvalidityType.STOP_TIME) {
            model.with("invalidStopTime", true);
        } else if (type == InvalidityType.PURPOSE) {
            model.with("invalidPurpose", true);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        template.render(model, baos);
        return baos.toByteArray();
    }
}