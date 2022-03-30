package com.spd.trello.service;

import com.spd.trello.domain.items.Attachment;
import com.spd.trello.repository_jpa.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository repository;

    public Attachment save(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setContext(file.getContentType());
        attachment.setFile(fileBytes);

       return repository.save(attachment);
    }

    public Attachment convert(MultipartFile file) {
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setContext(file.getContentType());
        attachment.setFile(fileBytes);

       return attachment;
    }

    public Optional<Attachment> readById(UUID id) {
        try {
            return Optional.ofNullable(repository.findById(id).orElseThrow(FileNotFoundException::new));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
