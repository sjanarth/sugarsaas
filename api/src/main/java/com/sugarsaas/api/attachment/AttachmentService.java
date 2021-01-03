package com.sugarsaas.api.attachment;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface AttachmentService
{
    Attachment addAttachment(Object entity, String name, MultipartFile file) throws IOException;

    Attachment addAttachment(Object entity, String name, InputStream file) throws IOException;

    Collection<Attachment> getAttachments(Object entity);
}
