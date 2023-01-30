package com.socialnetwork.app.controllers;

import com.socialnetwork.app.Main;
import com.socialnetwork.app.domain.User;
import com.socialnetwork.app.exceptions.RepoException;
import com.socialnetwork.app.exceptions.ValidationException;
import com.socialnetwork.app.service.AppService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginInterfaceController {

    @FXML
    public TextField emailLoginTextField;

    @FXML
    public TextField emailRegisterTextField;

    @FXML
    public TextField firstNameRegisterTextField;

    @FXML
    public TextField lastNameRegisterTextField;

    @FXML
    public AnchorPane loginPane;

    @FXML
    public Button loginRegisterButton;

    @FXML
    public Label registerButton;

    @FXML
    public AnchorPane registerPane;

    @FXML
    public Button signUpRegisterButton;

    @FXML
    public Label titleLabel;


    @FXML
    public Button signInLoginButton;

    private AppService service;
    private User currentUser;

    @FXML
    public void onSignUpButtonActionLogin(){
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    public void onSignInButtonActionRegister(){
        loginPane.setVisible(true);
        registerPane.setVisible(false);
    }

    @FXML
    public void onSignUpButtonActionRegister() {

        String firstName = firstNameRegisterTextField.getText();
        String lastName = lastNameRegisterTextField.getText();
        String email = emailRegisterTextField.getText();
        try {
            service.add(firstName, lastName, email);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }

    }

    @FXML
    public void onSignInButtonActionLogin() {
        try{
            String email=emailLoginTextField.getText();
            currentUser=service.findUserByEmail(email);
            FXMLLoader loader=new FXMLLoader(Main.class.getResource("UserMainInterfaceRefurbished.fxml"));
            Scene scene;
            try{
                scene = new Scene(loader.load(), 311, 400);
                String css = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
                scene.getStylesheets().add(css);
            }
            catch (Exception ex){
                ex.printStackTrace();
                return;
            }
            UserMainIntefaceRefurbishedController controller = loader.getController();

            controller.setService(service,currentUser);
            Stage currentStage=(Stage) signInLoginButton.getScene().getWindow();



            Stage newStage = new Stage();
            newStage.setScene(scene);
            //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("C:\\Users\\Denis\\Documents\\GitHub\\UBB\\Year 2\\Semester 1\\Advanced Programming Methods\\Social Network - FINAL\\src\\main\\resources\\styles\\mainStyle.css")).toExternalForm());
            newStage.setResizable(false);
            newStage.setTitle("HI6");
            currentStage.close();
            newStage.show();

        }
        catch (RepoException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    public void setService(AppService service1) {
        service = service1;

    }


}
