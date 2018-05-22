package view;

import exceptions.NoParentException;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import main.MiniNet;
import model.Adult;
import model.Child;
import model.Person;
import model.YoungChild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Show overview of person selected
 */
public class PersonOverviewController {

    @FXML
    private TableView<Person> personTableView;

    @FXML
    private TableColumn<Person, String> nameColumn;

    @FXML
    private Label nameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label sexLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label stateLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private ListView listView;

    private MiniNet miniNet;

    private final List<String> relations = Arrays.asList("", "parent", "sibling", "classmate", "friend", "colleague", "couple");

    public PersonOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws Exception {

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));


        choiceBox.setItems(FXCollections.observableArrayList(relations));

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> getList(relations.get(newValue.intValue())));
    }


    /**
     *Set person's list to tableview
     * @param miniNet to get Person list from memory
     */
    public void setTable(MiniNet miniNet) {
        this.miniNet = miniNet;
        personTableView.setItems(miniNet.getPersonList());
    }

    /**
     * Called when the user clicks the Add button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson() {
        Person tmp=new Person();
        miniNet.showPersonEditDialog(tmp);
        for(Person p:miniNet.getPersonList()){
            System.out.println(p.getName());
        }
        System.out.println("adult:");
        for(Adult a:miniNet.getAdultList()){
            System.out.println(a.getName());
        }
        System.out.println("child");
        for(Child c:miniNet.getChildList()){
            System.out.println(c.getName());
        }
        System.out.println("youngchild");
        for(YoungChild y:miniNet.getYoungChildrenList()){
            System.out.println(y.getName());
        }
    }

    /**
     * Called when the user clicks the Connect button. Opens a dialog to find out whether two people are directly
     * connected in some ways or
     * define relation between two people e.g. friends, parent, classmates
     */
    @FXML
    private void handleRelation(){
        miniNet.showPersonConnectionDialog();
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTableView.getSelectionModel().getSelectedIndex();
        Person p = personTableView.getSelectionModel().getSelectedItem();
        String name = p.getName().trim();
        if(selectedIndex<0)
            return;
        //if target is a young child, check his parents' list and his siblings' list
        if (p.getType() == 0) {
            YoungChild y = miniNet.getYoungChild(name);
            List<String> parent = y.getParents();
            List<String> sibling = y.getSiblings();
            removeFromParent(parent, name);
            removeFromSibling(sibling, name);
        } else if (p.getType() == 1) {
            Child c = miniNet.getChild(name);
            List<String> parent = c.getParents();
            List<String> sibling = c.getSiblings();
            List<String> classmate = c.getClassmate();
            List<String> friend = c.getFriend();
            removeFromParent(parent, name);
            removeFromSibling(sibling, name);
            removeFromClassmate(classmate, name);
            removeFromFriend(friend, name, p.getType());
        } else if (p.getType() == 2) {
            Adult a = miniNet.getAdult(name);
            List<String> children = a.getChildren();
            try {
                if (!children.isEmpty()) {
                    throw new NoParentException();
                }
                List<String> classmate = a.getClassmate();
                List<String> friend = a.getFriend();
                a.setCouple(null);
                List<String> colleague = a.getColleague();
                removeFromClassmate(classmate, name);
                removeFromColleague(colleague, name);
                removeFromFriend(friend, name, p.getType());
            }catch (NoParentException pa){
                printError(("This  child or young child has no parent or has only one parent\n"));
                return;
            }
        }
        personTableView.getItems().remove(selectedIndex);
    }

    /**
     * Fills all text fields to show details about the person.
     * @param person the person
     */
    public void showPersonDetails(Person person) {
        Image image;
        if (person != null) {
            nameLabel.setText(person.getName());
            ageLabel.setText(Integer.toString(person.getAge()));
            sexLabel.setText(person.getSex());
            statusLabel.setText(person.getStatus());
            stateLabel.setText(person.getState());
            if (person.getImage().length() != 0) {
                image = new Image("resources/" + person.getImage());
            } else {
                image = new Image("resources/timg.jpg");
            }
        } else {
            nameLabel.setText("");
            ageLabel.setText("");
            sexLabel.setText("");
            statusLabel.setText("");
            stateLabel.setText("");
            image = new Image("resources/timg.jpg");
        }
        imageView.setImage(image);
        choiceBox.getSelectionModel().selectFirst();
        listView.setItems(null);
    }

    /**
     * Return one specified relation list of a person's
     * @param relation
     */
    public void getList(String relation) {
        String name = nameLabel.getText();
        if (name.equals("") == false) {
            List<String> nameList = new ArrayList<>();
            //System.out.println(Integer.parseInt(ageLabel.getText()));
            if (Integer.parseInt(ageLabel.getText()) <= 2) {
                YoungChild y = miniNet.getYoungChild(name);
                if (relation.equals("parent")) {
                    nameList = y.getParents();
                } else if (relation.equals("sibling"))
                    nameList = y.getSiblings();
            } else if (Integer.parseInt(ageLabel.getText()) < 17 && Integer.parseInt(ageLabel.getText()) > 2) {
                Child c = miniNet.getChild(name);
                if (relation.equals("parent"))
                    nameList = c.getParents();
                else if (relation.equals("sibling"))
                    nameList = c.getSiblings();
                else if (relation.equals("classmate"))
                    nameList = c.getClassmate();
                else if (relation.equals("friend"))
                    nameList = c.getFriend();
            } else {
                Adult a = miniNet.getAdult(name);
                if (relation.equals("colleague"))
                    nameList = a.getColleague();
                else if (relation.equals("couple"))
                    nameList.add(a.getCouple());
                else if (relation.equals("parent"))
                    nameList = a.getChildren();
                else if (relation.equals("classmate"))
                    nameList = a.getClassmate();
                else if (relation.equals("friend"))
                    nameList = a.getFriend();
            }
            ObservableList<String> strList = FXCollections.observableArrayList(nameList);
            listView.setItems(strList);
        }
    }

    private void removeFromColleague(List<String> colleague, String name) {
        for (String colleagueName : colleague) {
            for (Adult a : miniNet.getAdultList()) {
                if (a.getName().equals(colleagueName))
                    a.deleteColleague(name);
            }
        }
    }

    private void removeFromFriend(List<String> friend, String name, int type) {
        for (String friendName : friend) {
            if (type == 1)
                for (Child c : miniNet.getChildList()) {
                    if (c.getName().equals(friendName))
                        c.deleteFriend(name);
                }
            else
                for (Adult a : miniNet.getAdultList()) {
                    if (a.getName().equals(friendName))
                        a.deleteFriend(name);
                }
        }

    }

    private void removeFromClassmate(List<String> classmate, String name) {
        label1:
        for (String classmateName : classmate) {
            for (Child c : miniNet.getChildList()) {
                if (c.getName().equals(classmateName)) {
                    c.deleteClassmate(name);
                    continue label1;
                }
            }
            for (Adult a : miniNet.getAdultList()) {
                if (a.getName().equals(classmateName)) {
                    a.deleteClassmate(name);
                    continue label1;
                }
            }
        }
    }

    private void printError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Fields");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText(s);
        alert.showAndWait();
    }



    private void removeFromSibling(List<String> sibling, String name) {
        label1:
        for (String siblingName : sibling) {
            for (YoungChild y : miniNet.getYoungChildrenList()) {
                if (y.getName().equals(siblingName)) {
                    y.deleteSibling(name);
                    continue label1;
                }
            }
            for (Child c : miniNet.getChildList()) {
                if (c.getName().equals(siblingName)) {
                    c.deleteSibling(name);
                    continue label1;
                }
            }
        }
    }


    private void removeFromParent(List<String> parents, String name) {
        for (String parentName : parents) {
            for (Adult a : miniNet.getAdultList()) {
                if (a.getName().equals(parentName))
                    a.deleteChild(name);
            }
        }
    }
}
