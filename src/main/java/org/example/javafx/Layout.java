package org.example.javafx;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.example.modules.*;
import org.example.socket.Request;
import org.example.socket.Response;
import org.example.socket.Server;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

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
    private Button usernameButton;
    private List<org.example.modules.Server> servers;
    @FXML
    public ListView<Message> listView;
    @FXML
    private Label serverName;
    @FXML
    private Label textChannelName;
    @FXML
    public TextField textInput;
    @FXML
    private Button sendButton;
    @FXML
    private Button disconnect;
    @FXML
    private Button mute;
    @FXML
    private Button deaf;
    @FXML
    private FontIcon muteIcon;
    @FXML
    private FontIcon deafIcon;
    public List<Message> messages;
    public static Layout CurrentInstance = null;

    @FXML
    void initialize() {
        CurrentInstance = this;
        DrawMute();
        DrawDeaf();
        listView.setCellFactory(new MessageViewFactory());
        if (Constants.user != null) {
            usernameButton.setText(Constants.user.nickname);
        }
        textInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
        });
        mute.setOnAction(event -> {
            Constants.user.isMuted = 1 - Constants.user.isMuted;
            User user = new User();
            user.id = Constants.user.id;
            user.isMuted = Constants.user.isMuted;
            try {
                Server.out.writeObject(new Request("edit", "user", "", user));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DrawMute();
        });
        deaf.setOnAction(event -> {
            Constants.user.isDeafened = 1 - Constants.user.isDeafened;
            User user = new User();
            user.id = Constants.user.id;
            user.isDeafened = Constants.user.isDeafened;
            try {
                Server.out.writeObject(new Request("edit", "user", "", user));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DrawDeaf();
        });
        // VBox parent = (VBox) textChannelName.getParent();
        // textChannelName.prefWidthProperty().bind(parent.widthProperty());
        try {
            Server.out.writeObject(new Request("list", "server", "fetchServer", null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void DrawMute() {
        System.out.println(Constants.user);
        if (Constants.user.isMuted.equals(1L)) {
            muteIcon.setIconColor(Color.RED);
            muteIcon.setIconLiteral("mdmz-mic_off");
            Constants.StopSpeak();
        } else {
            muteIcon.setIconColor(Color.valueOf("#b4b4b4"));
            muteIcon.setIconLiteral("mdmz-mic");
            new Thread(Constants::Speak);
        }
    }
    public void DrawDeaf() {
        if (Constants.user.isDeafened.equals(1L)) {
            deafIcon.setIconColor(Color.RED);
            deafIcon.setIconLiteral("mdsmz-volume_off");
        } else {
            deafIcon.setIconColor(Color.valueOf("#b4b4b4"));
            deafIcon.setIconLiteral("mdsmz-volume_up");
        }
    }
    public void fetchServer(Response res) {
        servers = (List<org.example.modules.Server>) res.body;
        if (Constants.selectedServer == null && !servers.isEmpty()) {
            Constants.selectedServer = servers.getFirst();
            drawServer();
        }
        serverList();
        sendButton.setOnAction(e -> {
            sendMessage();
        });
    }
    private void sendMessage() {
        String text = textInput.getText();
        Message msg = new Message();
        msg.textChannel = Constants.selectedTextChannel;
        msg.content = text.trim();
        try {
            Server.out.writeObject(new Request("create", "message", "chat", msg));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void serverList() {
        serversPane.getChildren().removeAll(serversPane.getChildren());
        for (org.example.modules.Server server: servers) {
            if (server.logo == null) {
                server.logo = new File("src/main/resources/logo.png");
            }
            Button button = getServerButton(server);
            if (Constants.selectedServer.id.equals(server.id)) {
                button.setStyle("-fx-background-color: #36373D;");
            } else {
                button.setStyle("-fx-background-color: transparent;");
            }
            serversPane.getChildren().add(button);
        }
    }

    private Button getServerButton(org.example.modules.Server server) {
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
                Server.out.writeObject(new Request("find", "server", "findServer", server));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        button.setMinHeight(70);
        button.setPrefWidth(Region.USE_COMPUTED_SIZE);
        return button;
    }
    public void findServer(Response res) {
        if (res.status == 200) {
            Constants.selectedServer = (org.example.modules.Server) res.body;
            serverList();
        }
        drawServer();
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
            voiceChannelButton.setOnAction(e -> {
                try {
                    Server.out.writeObject(new Request("join", "voice_channel", "joinChannel", voiceChannel));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            voiceChannels.getChildren().add(voiceChannelButton);
        }
        voiceChannelAccordion.setContent(voiceChannels);
    }
    public void joinChannel(Response response) {
        if (response.status != 200) {
            return;
        }
        if (Constants.selectedVoiceChannel != null) {
            Constants.LeaveSpeak();
        }
        Constants.selectedVoiceChannel = (VoiceChannel) response.body;
        new Thread(Constants::Listener).start();
        new Thread(Constants::Speak).start();
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
            Server.out.writeObject(new Request("list", "message", "fetchMessage", msg));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void fetchMessage(Response res) {
        if (res.status == 200) {
            messages = (List<Message>) res.body;
            DrawListView();
        }
    }

    public void DrawListView() {
        listView.getItems().removeAll(listView.getItems());
        for (Message msg: messages) {
            listView.getItems().addFirst(msg);
        }
        listView.refresh();
        listView.scrollTo(messages.size()+100);
    }
}
