package com.spd.trello.repository.impl.helper;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Attachment;
import com.spd.trello.domain.Card;
import com.spd.trello.domain.Comment;
import com.spd.trello.repository.Repository;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AttachmentHelper {

    public Attachment create(Attachment attachment, Card card) {
        JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("insert into attachments(id, link, name, file, card_id) values (?,?,?,?,?)");
            statement.setObject(1, attachment.getId());
            statement.setString(2, attachment.getLink());
            statement.setString(3, attachment.getName());
            statement.setBytes(4, fileToByte(attachment.getFile()));
            statement.setObject(5, card.getId());
            statement.executeUpdate();
            return attachment;
        });
        return findById(attachment.getId());
    }

    public Attachment create(Attachment attachment, Comment comment) {
        JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("insert into attachments(id, link, name, file, comment_id) values (?,?,?,?,?)");
            statement.setObject(1, attachment.getId());
            statement.setString(2, attachment.getLink());
            statement.setString(3, attachment.getName());
            statement.setObject(4, fileToByte(attachment.getFile()));
            statement.setObject(5, comment.getId());
            statement.executeUpdate();
            return attachment;
        });
        return findById(attachment.getId());
    }

    public Attachment findById(UUID id) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("select * from attachments where id = ?");
            statement.setObject(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                    return buildAttachment(resultSet);
            }
            throw new RuntimeException();
        });
    }

    private Attachment buildAttachment(ResultSet resultSet) throws SQLException {
        Attachment attachment = new Attachment();
        attachment.setId(UUID.fromString(resultSet.getString("id")));
        attachment.setName(resultSet.getString("name"));
        attachment.setLink(resultSet.getString("link"));
        attachment.setFile(writeBytesToFile(new File("db_files/" + attachment.getName() + ".jpg"),
                resultSet.getBytes("file")));

        UUID cardId = Optional.ofNullable(resultSet.getString("card_id"))
                .map(UUID::fromString).orElse(null);
        if (cardId != null) {
            Card card = new Card();
            card.setId(cardId);
            attachment.setCard(card);
        } else {
            Comment comment = new Comment();
            comment.setId(Optional.ofNullable(resultSet.getString("comment_id"))
                    .map(UUID::fromString).orElse(null));
            attachment.setComment(comment);
        }
        return attachment;
    }

    public boolean delete(UUID id) {
        return new Repository.Helper().delete(id, "delete from attachments where id = ?");
    }

    public byte[] fileToByte(File file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes1 = new byte[1024];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            for (int readNum; (readNum = fileInputStream.read(bytes1)) != -1; ) {
                byteArrayOutputStream.write(bytes1, 0, readNum);
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private File writeBytesToFile(File file, byte[] bytes) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return file;
    }
}