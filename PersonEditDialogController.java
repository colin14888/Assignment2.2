package view;

import exceptions.NoSuchAgeException;
import exceptions.TooYoungException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MiniNet;
import model.Adult;
import model.Child;
import model.Person;
import model.YoungChild;

import java.util.List;


/**
 * Dialog to edit details of a person.
 */
public class PersonEditDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField sexField;
    @FXML
    private TextField imageField;
    @FXML
    private TextField father, mother;

    private Stage dialogStage;
    private Person person;
    private MiniNet miniNet;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogStage.setResizable(false);
    }


    /**
     * Sets the person to be edited in the dialog.
     *
     * @param person new person
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Set miniNet to get its lists.
     * @param net
     */
    public void setNet(MiniNet net) {
        this.miniNet = net;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            String name, image, status, sex, state;
            if (imageField.getText().length() != 0 && imageField.getText() != null)
                image = (imageField.getText());
            else
                image = "";
            if (statusField.getText().length() != 0 && statusField.getText() != null)
                status = (statusField.getText());
            else
                status = "";
            int age = Integer.parseInt(ageField.getText());
            name = (nameField.getText());
            sex = (sexField.getText());
            state = (stateField.getText());
            if (age <= 2) {
                YoungChild y = new YoungChild(name, image, status, age, sex, state);
                y.setParent(father.getText());
                y.setParent(mother.getText());
                y.setSiblings(miniNet.getAdult(father.getText()).getChildren());
                addToSibling(miniNet.getAdult(father.getText()).getChildren(), nameField.getText());
                miniNet.getYoungChildrenList().add(y);
                addChildToParents(father.getText(), mother.getText(), name);
            } else if (age <= 16 && age > 2) {
                Child c = new Child(name, image, status, age, sex, state);
                c.setParent(father.getText());
                c.setParent(mother.getText());
                c.setSiblings(miniNet.getAdult(father.getText()).getChildren());
                addToSibling(miniNet.getAdult(father.getText()).getChildren(), nameField.getText());
                miniNet.getChildList().add(c);
                addChildToParents(father.getText(), mother.getText(), name);
            } else {
                Adult a = new Adult(name, image, status, age, sex, state);
                miniNet.getAdultList().add(a);
            }
            person = new Person(name, image, status, age, sex, state);
            miniNet.getPersonList().add(person);
            okClicked = true;
            dialogStage.close();
        }
    }


    private void addChildToParents(String father, String mother, String name) {
        Adult a = miniNet.getAdult(father);
        Adult b = miniNet.getAdult(mother);
        a.addChildren(name);
        b.addChildren(name);
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    private boolean isInputValid() {
        String errorMessage = "";
        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (ageField.getText() == null || ageField.getText().length() == 0) {
            errorMessage += "Null age!\n";
        } else {
            try {
                int age = Integer.parseInt(ageField.getText());
                if (age < 0 || age > 150) {
                    throw new NoSuchAgeException();
                }
            } catch (NumberFormatException e) {
                errorMessage += "No valid age(must be an integer)!\n";
            } catch (NoSuchAgeException e) {
                errorMessage += "Age must be <=150 and >=0\n";
            }
        }
        if (stateField.getText() == null || stateField.getText().length() == 0) {
            errorMessage += "No valid state!\n";
        }
        if (!sexField.getText().equals("Male") && !sexField.getText().equals("Female")) {
            System.out.println(sexField.getText() + "::" + sexField.getText().equals("Male") + "::" + sexField.getText().equals("Female"));

            errorMessage += "Sex must be 'Male' or 'Female'\n";
        }
        if (ageField.getText() != null && ageField.getText().length() != 0 && Integer.parseInt(ageField.getText()) <= 16) {
            if (father.getText() == null || father.getText().length() == 0 || mother.getText() == null || mother.getText().length() == 0) {
                errorMessage += "Young child or child must have parents!\n";
            } else {
                String fatherStr = father.getText();
                String motherStr = mother.getText();
                if (!miniNet.isParentRight(fatherStr, motherStr)) {
                    errorMessage += "The two aren't couple.\n";
                }
            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }

    private void addToSibling(List<String> sibling, String name) {
        label1:
        for (String siblingName : sibling) {
            for (YoungChild y : miniNet.getYoungChildrenList()) {
                if (y.getName().equals(siblingName)) {
                    y.addSibling(name);
                    continue label1;
                }
            }
            for (Child c : miniNet.getChildList()) {
                if (c.getName().equals(siblingName)) {
                    c.addSibling(name);
                    continue label1;
                }
            }
        }
    }


}
