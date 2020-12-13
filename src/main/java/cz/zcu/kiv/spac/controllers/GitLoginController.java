package cz.zcu.kiv.spac.controllers;

import cz.zcu.kiv.spac.data.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * Controller for git login window.
 */
public class GitLoginController {

    @FXML
    private TextField txtfieldUsername;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField pfieldPassword;

    private String username;
    private String password;


    // Logger.
    private static Logger log = Logger.getLogger(GitLoginController.class);

    /**
     * Controller.
     */
    public GitLoginController() {

    }

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {

    }

    /**
     * Login action.
     * @param actionEvent Action event.
     */
    @FXML
    private void btnGitLoginAction(ActionEvent actionEvent) {

        username = txtfieldUsername.getText();
        password = pfieldPassword.getText();

        if (!username.equals("") && !password.equals("")) {

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.close();

        } else {

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle(Constants.APP_NAME);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Git authentication window.");
            alert.setContentText("Username or password is not set!");
            alert.showAndWait();
        }
    }


    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;

        txtfieldUsername.setText(username);
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
        pfieldPassword.setText(password);
    }
}