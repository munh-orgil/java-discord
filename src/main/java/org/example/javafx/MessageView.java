package org.example.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import org.example.modules.Message;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MessageView extends ListCell<Message> {
    @FXML
    private Label author;
    @FXML
    private Label timestamp;
    @FXML
    private Label content;

    public MessageView() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            author.setText(item.author.nickname);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String createdAt = formatter.format(item.createdAt.toLocalDateTime());
            timestamp.setText(createdAt);
            content.setWrapText(true);
            content.setTextAlignment(TextAlignment.JUSTIFY);
            content.setMaxWidth(1100);
            content.setText(item.content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
