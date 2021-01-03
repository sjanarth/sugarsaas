package com.sugarsaas.api.attachment;

import com.sugarsaas.api.core.AbstractNoSQLServiceImpl;
import com.sugarsaas.api.core.SupportsAttachments;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
public class AttachmentServiceImpl extends AbstractNoSQLServiceImpl implements AttachmentService
{
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public Attachment addAttachment(Object entity, String name, MultipartFile file) throws IOException {
        //log.info("addAttachment {}, {}", entity, name);
        Attachment attachment = new Attachment();
        attachment.setEntityId((Integer)getEntityId(entity, SupportsAttachments.class).get());
        attachment.setType(getEntityType(entity, SupportsAttachments.class).get());
        attachment.setName(name);
        attachment.setData(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        Attachment attachment2 = attachmentRepository.insert(attachment);
        return attachment2;
    }

    @Override
    // TODO: Complete this to handle large uploads using gridFsTemplate
    public Attachment addAttachment(Object entity, String name, InputStream file) throws IOException {
        return null;
    }

    public Collection<Attachment> getAttachments(Object target)   {
        //log.info("getAttachments called for "+target);
        Optional<Object> entityId = getEntityId(target, SupportsAttachments.class);
        Optional<String> entityType = getEntityType(target, SupportsAttachments.class);
        if (entityId.isPresent() && entityType.isPresent()) {
            //log.info("  -> entityId={},entityType={}", entityId.get(), entityType.get());
            return attachmentRepository.findByEntityIdAndType((Integer) entityId.get(), entityType.get());
        }
        return new HashSet<>();
    }
}