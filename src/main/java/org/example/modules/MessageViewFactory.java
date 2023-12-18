package org.example.modules;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.example.javafx.MessageView;

public class MessageViewFactory implements Callback<ListView<Message>, ListCell<Message>> {
    @Override
    public ListCell<Message> call(ListView<Message> param) {
        return new MessageView();
    }
}