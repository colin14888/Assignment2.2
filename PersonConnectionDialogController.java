package view;

import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import main.MiniNet;
import model.Adult;
import model.Child;
import model.Person;
import model.YoungChild;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller controlls dialog find out whether two people are directly connected in some ways or
 * define relation between two people e.g. friends, parent, classmates
 */
public class PersonConnectionDialogController {
    @FXML
    private TextField name1Field;
    @FXML
    private TextField name2Field;
    @FXML
    private TextField parentField;
    @FXML
    private ChoiceBox relationBox;
    @FXML
    private ListView listView;


    private final List<String> relations = Arrays.asList("", "parent", "classmate", "friend", "colleague", "couple");
    private String selectedRelation = new String();
    private MiniNet miniNet;
    private Stage dialogStage;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {


        relationBox.setItems(FXCollections.observableArrayList(relations));

        relationBox.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> getSelectedString(relations.get(newValue.intValue())));
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
     * Set miniNet to get its lists.
     * @param net
     */
    public void setNet(MiniNet net) {
        this.miniNet = net;
    }

    /**
     * Called when the user clicks Add New Relation
     */
    @FXML
    private void handleAdd() {
        if(isInput2Valid()){
            printOK();
        }else{
            return;
        }
    }


    /**
     * Called when the user clicks Find Relation
     */
    @FXML
    private void handleFind() throws Exception {
        if(isInput1Valid()){
            String name1=name1Field.getText();
            String name2=name2Field.getText();
            Person person=miniNet.getPerson(name1);
            if(person==null){
                printError("No people named "+ name1);
                return;
            }
            List<String> relationsList=new ArrayList<>();
            if(person.getAge()<=2){
                YoungChild youngChild=miniNet.getYoungChild(name1);
                for(String name: youngChild.getParents())
                    if(name.equals(name2))
                        relationsList.add("parent");
                for(String name: youngChild.getSiblings())
                    if(name.equals(name2))
                        relationsList.add("sibling");
            }else if(person.getAge()>2&&person.getAge()<17){
                Child child=miniNet.getChild(name1);
                for(String name: child.getParents())
                    if(name.equals(name2))
                        relationsList.add("parent");
                for(String name: child.getSiblings())
                    if(name.equals(name2))
                        relationsList.add("sibling");
                for(String name:child.getClassmate())
                    if(name.equals(name2))
                        relationsList.add("classmate");
                for(String name:child.getFriend())
                    if(name.equals(name2))
                        relationsList.add("friend");
            }else{
                Adult adult=miniNet.getAdult(name1);
                for(String name: adult.getParents())
                    if(name.equals(name2))
                        relationsList.add("parent");
                for(String name:adult.getClassmate())
                    if(name.equals(name2))
                        relationsList.add("classmate");
                for(String name:adult.getFriend())
                    if(name.equals(name2))
                        relationsList.add("friend");
                    else
                        System.out.println(name+":::"+name2);
                if(adult.getCouple()!=null&&adult.getCouple().length()!=0&&adult.getCouple().equals(name2))
                    relationsList.add("couple");
                for(String name:adult.getColleague())
                    if(name.equals(name2))
                        relationsList.add("colleague");
            }
            if(relationsList.size()==0)
                relationsList.add("No relation.");
            ObservableList<String> strList = FXCollections.observableArrayList(relationsList);
            listView.setItems(strList);
        }
    }

    /**
     * call when  the user clicks Close
     */
    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    //check if input is valid to find relation
    private boolean isInput1Valid() {
        StringBuffer errorMessage=new StringBuffer();
        isInputValid(errorMessage, true);
        if (errorMessage.length() == 0) {
            return true;
        } else {
            printError(errorMessage.toString());
            return false;
        }
    }

    //check if input is valud to add relation
    private boolean isInput2Valid() {
        StringBuffer errorMessage = new StringBuffer();
        isInputValid(errorMessage,false);
        if (selectedRelation.equals("parent"))
            isValidToBeParent(name1Field.getText(), name2Field.getText(), parentField.getText(), errorMessage);
        else if (selectedRelation.equals("colleague"))
            isValidToBeColleague(name1Field.getText(), name2Field.getText(), errorMessage);
        else if (selectedRelation.equals("couple"))
            isValidToBeCouple(name1Field.getText(), name2Field.getText(), errorMessage);
        else if (selectedRelation.equals("classmate"))
            isValidToBeClassmate(name1Field.getText(), name2Field.getText(), errorMessage);
        else if (selectedRelation.equals("friend"))
            isValidToBeFriend(name1Field.getText(), name2Field.getText(), errorMessage);
         if (errorMessage.length() == 0) {
            return true;
        } else {
            printError(errorMessage.toString());
            return false;
        }
    }

    private void isInputValid(StringBuffer errorMessage, Boolean flag) {
        if (name1Field.getText() == null || name1Field.getText().length() == 0) {
            errorMessage.append("No valid name1!\n") ;
        }
        if (name2Field.getText() == null || name2Field.getText().length() == 0) {
            errorMessage.append("No valid name2!\n");
        }
        if (!flag&& selectedRelation.length() == 0) {
            errorMessage.append("No selected Relation!\n");
        }
        return;
    }


    private void isValidToBeParent(String name1, String name2, String name3, StringBuffer error) {
       Person person=miniNet.getPerson(name1);
       Person person1=miniNet.getPerson(name2);
       if(person==null){
           error.append("No people named "+name1);
           return;
       }if(person1==null){
            error.append("No people named "+name2);
            return;
        }

       if(person.getAge()<17&&person1.getAge()<17){
           error.append("No adult here.");
           return;
       }else if(person.getAge()>17){
           beParent(name1,name3, name2, error);
       }else if(person1.getAge()>17){
           beParent(name2,name3,name1,error);
       }
    }

    private void beParent(String parent1, String parent2, String child, StringBuffer error){
        Adult adult=miniNet.getAdult(parent1);
        try {
            //only one parent
            if(parent2.length()==0)
                throw new NoParentException();
            //two adults are couple
            if (adult.getCouple().equals(parent2)) {
                adult.addChildren(child);
                Adult adult1 = miniNet.getAdult(parent2);
                adult1.addChildren(child);
                YoungChild youngChild=miniNet.getYoungChild(child);
                if(youngChild!=null) {
                    youngChild.setParent(parent1);
                    youngChild.setParent(parent2);
                } else {
                    Child c = miniNet.getChild(child);
                    c.setParent(parent1);
                    c.setParent(parent2);
                }
            }
            //not couple
             else{
                throw  new NoParentException();
            }
        }catch (NoParentException pa){
            error.append("This  child or young child has no parent or has only one parent\n");
        }
    }

    private void isValidToBeColleague(String name1, String name2, StringBuffer error) {
        Adult adult = miniNet.getAdult(name1);
        Adult adult1 = miniNet.getAdult(name2);
        try {
            if (adult == null || adult1 == null)
                throw new NotToBeColleaguesException();
            else {
                adult.addColleague(name2);
                adult1.addColleague(name1);
            }
        } catch (NotToBeColleaguesException co) {
            error.append("People below 16 cannot be colleague!\n");
            return;
        }
    }

    private void isValidToBeCouple(String name1, String name2, StringBuffer error) {
        Adult adult = miniNet.getAdult(name1);
        Adult adult1 = miniNet.getAdult(name2);
        try {
            if (adult == null || adult1 == null)
                throw new NotToBeCoupleException();
            else if (adult.getCouple() != null || adult1.getCouple() != null) {
                throw new NoAvailableException();
            }
            adult.setCouple(name2);
            adult1.setCouple(name1);
        } catch (NotToBeCoupleException co) {
            error.append("People below 16 cannot be couples!\n");
            return;
        } catch (NoAvailableException av) {
            error.append("One of them is not available to be couple!\n");
            return;
        }
    }

    private void isValidToBeClassmate(String name1, String name2, StringBuffer error) {
        try {
            Person person = miniNet.getPerson(name1);
            Person person1 = miniNet.getPerson(name2);
            if (person == null) {
                error.append("No people named " + name1 + "\n");
                return;
            }
            if (person1 == null){
                error.append( "No people named " + name1 + "\n");
                return;
            }
        if (person.getAge() < 3 || person1.getAge() < 3)
                throw new NotToBeClassmatesException();
            beClassmate(name1, name2, person);
            beClassmate(name2, name1, person1);
        } catch (NotToBeClassmatesException cl) {
            error.append("Young Child cannot have classmate.\n");
            return;
        }
    }

    private void beClassmate(String name1, String name2, Person person) {
        if (person.getAge() < 17) {
            Child child = miniNet.getChild(name1);
            child.addClassmate(name2);
        } else {
            Adult adult = miniNet.getAdult(name1);
            adult.addClassmate(name2);
        }
    }

    private void isValidToBeFriend(String name1, String name2, StringBuffer error) {
        try {
            Person person = miniNet.getPerson(name1);
            Person person1 = miniNet.getPerson(name2);
            if (person == null){
                error.append("No people named " + name1 + "\n");
                return;
            }
            if (person1 == null) {
                error.append("No people named " + name1 + "\n");
                return;
            }
            if (person.getAge() < 3 || person1.getAge() < 3)
                throw new TooYoungException();
            if ((person.getAge() > 16 && person1.getAge() < 16) || (person.getAge() < 16 && Math.abs(person.getAge() - person1.getAge()) > 3))
                throw new NotToBeFriendException();
            if(person.getAge()>16){
                Adult adult=miniNet.getAdult(name1);
                Adult adult1=miniNet.getAdult(name2);
                adult.addFriend(name2);
                adult1.addFriend(name1);
            }else if(person.getAge()<17){
                Child child=miniNet.getChild(name1);
                Child child1=miniNet.getChild(name2);
                child.addFriend(name2);
                child.addFriend(name1);
            }
        } catch (TooYoungException ty) {
            error.append("Young Child is too young to have friend.\n");
            return;
        } catch (NotToBeFriendException f) {
            error.append("Adult and child or children with an age gpa larger than 3 can't be friends\n");
            return;
        }
    }


    private void printError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Fields");
        alert.setHeaderText("Please correct invalid fields");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private void printOK(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connction done.");
        alert.setHeaderText("Connection done.");
        alert.showAndWait();
    }

    private void getSelectedString(String relation) {
        this.selectedRelation = relation;
    }


}


