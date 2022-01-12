package com.spd.trello.repository.impl.helper;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Attachment;
import com.spd.trello.domain.Card;
import com.spd.trello.domain.Comment;
import com.spd.trello.repository.Repository;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AttachmentHelper {

    public Attachment create(Attachment attachment, Card card) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("insert into attachments(id, link, name, file, card_id) values (?,?,?,?,?)")) {
            statement.setObject(1, attachment.getId());
            statement.setString(2, attachment.getLink());
            statement.setString(3, attachment.getName());
            statement.setBytes(4, fileToByte(attachment.getFile()));
            statement.setObject(5, card.getId());
            statement.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return findById(attachment.getId());
    }

    public Attachment create(Attachment attachment, Comment comment) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("insert into attachments(id, link, name, file, comment_id) values (?,?,?,?,?)")) {
            statement.setObject(1, attachment.getId());
            statement.setString(2, attachment.getLink());
            statement.setString(3, attachment.getName());
            statement.setObject(4, fileToByte(attachment.getFile()));
            statement.setObject(5, comment.getId());
            statement.executeUpdate();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return findById(attachment.getId());
    }

    public Attachment findById(UUID id) {
        Attachment foundAttachment = null;
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("select * from attachments where id = ?")) {
            statement.setObject(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                foundAttachment = buildAttachment(resultSet);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return foundAttachment;
    }

    private Attachment buildAttachment(ResultSet resultSet) throws SQLException, IOException {
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

    public byte[] fileToByte(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes1 = new byte[1024];
        try {
            for (int readNum; (readNum = fileInputStream.read(bytes1)) != -1; ) {
                byteArrayOutputStream.write(bytes1, 0, readNum);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    private File writeBytesToFile(File file, byte[] bytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
        return file;
    }
}