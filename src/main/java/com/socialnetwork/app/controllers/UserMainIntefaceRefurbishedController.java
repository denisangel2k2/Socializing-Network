package com.socialnetwork.app.controllers;

import com.socialnetwork.app.Main;
import com.socialnetwork.app.domain.Message;
import com.socialnetwork.app.domain.MessageDTO;
import com.socialnetwork.app.domain.User;
import com.socialnetwork.app.domain.UserDTOFriend;
import com.socialnetwork.app.exceptions.RepoException;
import com.socialnetwork.app.service.AppService;
import com.socialnetwork.app.utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserMainIntefaceRefurbishedController implements Observer {

    private ObservableList<User> usersList = FXCollections.observableArrayList();
    private ObservableList<UserDTOFriend> friendList = FXCollections.observableArrayList();
    private ObservableList<MessageDTO> messagesList = FXCollections.observableArrayList();

    private ObservableList<User> friendRequestsList = FXCollections.observableArrayList();
    private User loggedUser = null;
    @FXML
    private ImageView acceptFriendButton;

    @FXML
    private Label connectedUserLabel;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private TableColumn<User, String> emailColumn;


    @FXML
    private TableColumn<UserDTOFriend, String> friendNameColumn;
    @FXML
    private TableColumn<UserDTOFriend, String> friendsSinceColumn;
    @FXML
    private Label emailUserLabel;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private VBox friendsPane;

    @FXML
    private TableView<UserDTOFriend> friendsTableView;
    @FXML
    private ListView<User> friendRequestsListView;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private VBox messagesListPane;

    @FXML
    private ImageView profileImageView;

    @FXML
    private ImageView rejectFriendButton;

    @FXML
    private Button removeFriendButton;

    @FXML
    private VBox searchPane;

    @FXML
    private TextField searchUserTextField;

    @FXML
    private VBox settingsPane;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private ImageView settingsPaneButton;

    @FXML
    private ImageView usersPaneButton;

    @FXML
    private ImageView logoutButton;

    @FXML
    private ImageView messagesListPaneButton;

    @FXML
    private ImageView friendsPaneButton;

    @FXML
    private TableColumn<MessageDTO, String> messageToUserColumn;

    @FXML
    private TableColumn<MessageDTO, String> messageFromUserColumn;

    @FXML
    private TableView<MessageDTO> chatTable;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendMessageButton;

    @FXML
    private ListView<UserDTOFriend> usersMessageList;

    @FXML
    private VBox chatPane;

    @FXML
    private Button refreshButton;

    private AppService service;


    public void setService(AppService service, User user) {


        settingsPane.setVisible(true);
        this.service = service;
        this.service.addObserver(this);
        this.loggedUser = user;

        connectedUserLabel.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName());
        emailUserLabel.setText(loggedUser.getEmail());
        profileImageView.setImage(new Image("https://i.imgur.com/DnozCSK.png"));
        settingsPaneButton.setImage(new Image("https://i.imgur.com/7Lxpwg7.png"));
        friendsPaneButton.setImage(new Image("https://i.imgur.com/t1SuPxI.png"));
        usersPaneButton.setImage(new Image("https://i.imgur.com/P3fFm98.png"));
        messagesListPaneButton.setImage(new Image("https://i.imgur.com/B7pIMLk.png"));
        logoutButton.setImage(new Image("https://i.imgur.com/qF6YY2t.png"));
        acceptFriendButton.setImage(new Image("https://i.imgur.com/9uXrBHl.png"));
        rejectFriendButton.setImage(new Image("https://i.imgur.com/ZFMYPBu.png"));
        chatTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initLists();
    }

    @FXML
    public void onLogoutButtonAction() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("LoginInterfaceView.fxml"));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 286, 400);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        LoginInterfaceController controller = loader.getController();
        controller.setService(service);
        Stage currentStage = (Stage) deleteAccountButton.getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("HI6");
        currentStage.close();
        newStage.show();
    }

    @FXML
    public void onRemoveAccountAction() {
        try {
            service.remove(loggedUser.getId());

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("LoginInterfaceView.fxml"));
            Scene scene;
            try {
                scene = new Scene(loader.load(), 286, 400);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            LoginInterfaceController controller = loader.getController();
            controller.setService(service);
            Stage currentStage = (Stage) deleteAccountButton.getScene().getWindow();

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.setTitle("HI6");
            currentStage.close();
            newStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No action finished!", ButtonType.OK);
        }
    }



    private void initLists() {
        HashMap<User, String> friendsOfUser = service.getFriends(loggedUser.getId());

        List<UserDTOFriend> friendsTemp = new ArrayList<>();


        List<User> friendReqTemp = new ArrayList<>();
        List<User> friendReqOfUsers = service.getFriendRequests(loggedUser.getId());
        for (User user : friendReqOfUsers) {
            friendReqTemp.add(user);
        }

        for (User user : friendsOfUser.keySet()) {
            UserDTOFriend friend = new UserDTOFriend(user, friendsOfUser.get(user));
            if (!friendReqTemp.contains(user))
                friendsTemp.add(friend);
        }
        friendList.setAll(friendsTemp);
        friendRequestsList.setAll(friendReqTemp);

        List<User> allUsers = service.getAllUsers();
        List<User> allUsersTempList = allUsers.stream()
                .filter(user -> user.getId() != loggedUser.getId())
                .collect(Collectors.toList());

        usersList.setAll(allUsersTempList);


    }

    @FXML
    public void onAcceptButton() {

        try {
            int id = friendRequestsListView.getSelectionModel().getSelectedItem().getId();
            service.addFriendship(loggedUser.getId(), id);
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No user selected!", ButtonType.OK);
            alert.show();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }


    @FXML
    public void onRejectFriendrequestButton() {
        try {
            int userID = friendRequestsListView.getSelectionModel().getSelectedItem().getId();
            service.removeFriendship(loggedUser.getId(), userID);
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No user selected!", ButtonType.OK);
            alert.show();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void onAddFriendButton() {
        try {
            User userToAskFriendship = usersTable.getSelectionModel().getSelectedItem();
            service.addFriendship(loggedUser.getId(), userToAskFriendship.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sent friendrequest", ButtonType.OK);
            alert.show();

        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No user selected!", ButtonType.OK);
            alert.show();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }

    }


    @FXML
    public void onRemoveFriendButton() {

        try {
            int friendID = friendsTableView.getSelectionModel().getSelectedItem().getUID();
            service.removeFriendship(friendID, loggedUser.getId());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No friend selected!", ButtonType.OK);
            alert.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void onSearchUserTextField() {
        List<User> users = service.getAllUsers();
        List<User> usersTemp = new ArrayList<>();
        for (User user : users) {
            String name = user.getFirstName() + " " + user.getLastName();
            if (name.startsWith(searchUserTextField.getText()) && user.getId() != loggedUser.getId())
                usersTemp.add(user);
        }
        usersList.setAll(usersTemp);
        usersTable.setItems(usersList);
    }


    @FXML
    public void initialize() {
        friendNameColumn.setCellValueFactory(new PropertyValueFactory<>("name_user"));
        friendsSinceColumn.setCellValueFactory(new PropertyValueFactory<>("friendsSince"));

        messageToUserColumn.setCellValueFactory(new PropertyValueFactory<>("toMessage"));
        messageFromUserColumn.setCellValueFactory(new PropertyValueFactory<>("fromMessage"));


        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        usersTable.setItems(usersList);

        friendRequestsListView.setItems(friendRequestsList);
        friendsTableView.setItems(friendList);

        usersMessageList.setItems(friendList);
        chatTable.setItems(messagesList);

        searchUserTextField.textProperty().addListener(o -> onSearchUserTextField());

        setWrappingTextForColumns(messageFromUserColumn);
        setWrappingTextForColumns(messageToUserColumn);


    }
    private <E> void setWrappingTextForColumns(TableColumn<E, String> t) {
        t.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    t.setStyle(null);
                    setStyle("-fx-text-fill: white");
                    setText(null);
                } else {
                    setText(null);
                    Text text = new Text(item);
                    text.setStyle("-fx-text-alignment:justify; -fx-text-fill: white");
                    t.setStyle("-fx-background-color: #90EDB4; -fx-control-inner-background: #212121;");

                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                    setGraphic(text);
                }
            }
        });
    }



    @FXML
    public void onUserFromUserMessageListClick() {
        //make visibile pane
        chatTable.getItems().clear();
        messagesList.clear();
        chatTable.refresh();
        chatPane.setVisible(true);
        Scene currentScene=settingsPane.getScene();
        Stage stage=(Stage) currentScene.getWindow();
        stage.setWidth(620);
        int fromUserID = usersMessageList.getSelectionModel().getSelectedItem().getUID();

        User fromUser = service.findUserById(fromUserID);
        List<Message> messageList = service.getMessagesBetweenUsers(loggedUser.getId(), fromUserID);


        List<MessageDTO> messages = new ArrayList<>();
        for (Message message : messageList) {
            MessageDTO messageDTO;
            if (Objects.equals(message.getSender().getId(), loggedUser.getId())) {
                messageDTO = new MessageDTO("", message.getMessage());
            } else {
                messageDTO = new MessageDTO(message.getMessage(), "");
            }
            messages.add(messageDTO);
        }

        messagesList.setAll(messages);


    }

    @FXML
    public void onSendMessageButton(){
        String messageText = messageTextField.getText();
        try{
            int id_receipentUser = usersMessageList.getSelectionModel().getSelectedItem().getUID();
            service.addMessage(loggedUser.getId(),id_receipentUser,messageText);


        }
        catch (RepoException ex){
            Alert alert=new Alert(Alert.AlertType.ERROR, ex.getMessage(),ButtonType.OK);
            alert.show();
        }
    }

    @Override
    public void update() {
        initLists();

    }

    @FXML
    private void onFriendsButton() {
        Scene currentScene=settingsPane.getScene();
        Stage stage=(Stage) currentScene.getWindow();
        stage.setWidth(311);
        settingsPane.setVisible(false);
        chatPane.setVisible(false);
        friendsPane.setVisible(true);
        messagesListPane.setVisible(false);
        searchPane.setVisible(false);
    }

    @FXML
    private void onMessagesButton() {
        Scene currentScene=settingsPane.getScene();
        Stage stage=(Stage) currentScene.getWindow();
        stage.setWidth(311);
        settingsPane.setVisible(false);
        chatPane.setVisible(false);
        friendsPane.setVisible(false);
        messagesListPane.setVisible(true);
        searchPane.setVisible(false);
    }

    @FXML
    private void onSearchButton() {
        Scene currentScene=settingsPane.getScene();
        Stage stage=(Stage) currentScene.getWindow();
        stage.setWidth(311);
        settingsPane.setVisible(false);
        chatPane.setVisible(false);
        friendsPane.setVisible(false);
        messagesListPane.setVisible(false);
        searchPane.setVisible(true);
    }

    @FXML
    private void onSettingsButton() {
        Scene currentScene=settingsPane.getScene();
        Stage stage=(Stage) currentScene.getWindow();
        stage.setWidth(311);
        settingsPane.setVisible(true);
        chatPane.setVisible(false);
        friendsPane.setVisible(false);
        messagesListPane.setVisible(false);
        searchPane.setVisible(false);
    }

}
