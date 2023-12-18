package org.example.javafx;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.modules.Message;
import org.example.modules.MessageViewFactory;
import org.example.modules.TextChannel;
import org.example.modules.VoiceChannel;
import org.example.socket.Request;
import org.example.socket.Response;
import org.example.socket.Server;

import static java.lang.Integer.MAX_VALUE;

public class Layout {
    @FXML
    private TitledPane textChannelAccordion;
    @FXML
    private TitledPane voiceChannelAccordion;
    @FXML
    private Button newTextChannel;
    @FXML
    private Button newVoiceChannel;
    @FXML
    private VBox serversPane;
    @FXML
    private Label usernameLabel;
    private List<org.example.modules.Server> servers;
    @FXML
    public static ListView<Message> listView;
    @FXML
    private ImageView avatar;
    @FXML
    private Label serverName;
    @FXML
    private Label textChannelName;
    @FXML
    private TextField textInput;
    @FXML
    private Button sendButton;


    @FXML
    void initialize() {
        if (Constants.user != null) {
            usernameLabel.setText(Constants.user.nickname);
        }
        VBox parent = (VBox) textChannelName.getParent();
        textChannelName.prefWidthProperty().bind(parent.widthProperty());
        try {
            Server.out.writeObject(new Request("list", "server", null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Response res = (Response) Server.in.readObject();
            servers = (List<org.example.modules.Server>) res.body;
            if (Constants.selectedServer == null && !servers.isEmpty()) {
                Constants.selectedServer = servers.getFirst();
                drawServer();
            }
            serverList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        sendButton.setOnAction(e -> {
            String text = textInput.getText();
            Message msg = new Message();
            msg.textChannel = Constants.selectedTextChannel;
            msg.content = text.trim();
            try {
                Server.out.writeObject(new Request("create", "message", msg));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private void serverList() {
        serversPane.getChildren().removeAll(serversPane.getChildren());
        for (org.example.modules.Server server: servers) {
            if (server.logo == null) {
                server.logo = new File("src/main/resources/logo.png");
            }
            Button button = getButton(server);
            if (Constants.selectedServer.id.equals(server.id)) {
                button.setStyle("-fx-background-color: #36373D;");
            } else {
                button.setStyle("-fx-background-color: transparent;");
            }
            serversPane.getChildren().add(button);
        }
    }

    private Button getButton(org.example.modules.Server server) {
        ImageView img = new ImageView(new Image(server.logo.toURI().toString()));
        img.setFitHeight(70);
        img.setPreserveRatio(true);
        Button button = new Button("", img);
        button.setStyle(
                "-fx-border-color: transparent;" +
                "-fx-border-width: 0;" +
                "-fx-background-color: transparent;" +
                "-fx-padding: 0"
                );
        button.setOnAction(e -> {
            try {
                Server.out.writeObject(new Request("find", "server", server));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                Response res = (Response) Server.in.readObject();
                if (res.status == 200) {
                    Constants.selectedServer = (org.example.modules.Server) res.body;
                    serverList();
                }
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            drawServer();
        });

        button.setMinHeight(70);
        button.setPrefWidth(Region.USE_COMPUTED_SIZE);
        return button;
    }

    private void drawServer() {
        if (Constants.selectedServer == null) {
            return;
        }
        serverName.setText(Constants.selectedServer.name);
        Long userId = 0L;
        if (Constants.user != null) {
            userId = Constants.user.id;
        }
        boolean isOwner = Constants.selectedServer.owner.id.equals(userId);
        if (!isOwner) {
            newTextChannel.setVisible(false);
            newVoiceChannel.setVisible(false);
        } else {
            newTextChannel.setVisible(true);
            newVoiceChannel.setVisible(true);
        }
        if (!Constants.selectedServer.textChannels.isEmpty()) {
            Constants.selectedTextChannel = Constants.selectedServer.textChannels.getFirst();
            GetMessages();
        } else {
            Constants.selectedTextChannel = null;
        }
        VBox textChannels = new VBox();
        textChannels.setStyle("-fx-background-color: #282b30");
        for (TextChannel textChannel: Constants.selectedServer.textChannels) {
            Button textChannelButton = channelButton(new Button(textChannel.name));
            textChannelButton.setOnAction(e -> {
                Constants.selectedTextChannel = textChannel;
                GetMessages();
            });
            textChannels.getChildren().add(textChannelButton);
        }
        textChannelAccordion.setContent(textChannels);

        VBox voiceChannels = new VBox();
        voiceChannels.setStyle("-fx-background-color: #282b30");
        for (VoiceChannel voiceChannel: Constants.selectedServer.voiceChannels) {
            Button voiceChannelButton = channelButton(new Button(voiceChannel.name));
            voiceChannels.getChildren().add(voiceChannelButton);
        }
        voiceChannelAccordion.setContent(voiceChannels);
    }

    private Button channelButton(Button button) {
        button.prefWidthProperty().bind(textChannelAccordion.widthProperty().subtract(2));
        button.getStyleClass().add("channelButton");
        return button;
    }

    public void GetMessages() {
        try {
            Message msg = new Message();
            if (Constants.selectedTextChannel != null) {
                textChannelName.setText(Constants.selectedTextChannel.name);
            }
            msg.textChannel = Constants.selectedTextChannel;
            Server.out.writeObject(new Request("list", "message", 1, 50, msg));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Response res = (Response) Server.in.readObject();
            if (res.status == 200) {
                List<Message> messages = (List<Message>) res.body;
                listView.getItems().removeAll(listView.getItems());
                listView.setCellFactory(new MessageViewFactory());
                for (Message msg: messages) {
                    listView.getItems().addFirst(msg);
                }
                listView.scrollTo(listView.getItems().size());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
