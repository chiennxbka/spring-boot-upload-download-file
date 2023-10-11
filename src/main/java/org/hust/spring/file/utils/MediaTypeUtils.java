package org.hust.spring.file.utils;

import org.springframework.http.MediaType;

import javax.servlet.ServletContext;

public class MediaTypeUtils {
    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            return MediaType.parseMediaType(mineType);
        } catch (Exception ex) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
