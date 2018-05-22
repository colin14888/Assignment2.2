package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Adult;
import model.Child;
import model.Person;
import model.YoungChild;
import view.PersonConnectionDialogController;
import view.PersonEditDialogController;
import view.PersonOverviewController;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Main class of this assignment.
 * Application starts here.
 */
public class MiniNet extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Person> personList = FXCollections.observableArrayList();
    private List<Adult> adultList = new ArrayList<>();
    private List<Child> childList = new ArrayList<>();
    private List<YoungChild> youngChildrenList = new ArrayList<>();

    /**
     * Application starts from here.
     * Load stage.
     * @param primaryStage  primary stage we see
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MiniNet");
        this.primaryStage.resizableProperty().setValue(Boolean.FALSE);
        this.primaryStage.setResizable(false);
        initRootLayout();
        showPersonOverview();
    }


    /**
     * Returns the list of persons in the memory.
     * @return List<Person> in the memory
     */
    public ObservableList<Person> getPersonList() {
        return personList;
    }


    /**
     * Returns the list of adults in the memory.
     * @return List<Adult> in the memory
     */
    public List<Adult> getAdultList() {
        return adultList;
    }


    /**
     * Returns the list of children in the memory.
     * @return List<Child> in the memory
     */
    public List<Child> getChildList() {
        return childList;
    }

    /**
     * Returns the list of young children in the memory.
     * @return List<YoungChild> in the memory
     */
    public List<YoungChild> getYoungChildrenList() {
        return youngChildrenList;
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MiniNet.class.getResource("/view/MainWindow.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Show selected person's overview on the right pane of the main window.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MiniNet.class.getResource("/view/PersonOverview.fxml"));
            AnchorPane personOverview = loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the exceptions access to the main app.
            PersonOverviewController controller = loader.getController();
            controller.setTable(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a new Dialog to add people into the network.
     * @param person new Person to be added.
     */
    public void showPersonEditDialog(Person person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MiniNet.class.getResource("/view/PersonEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            controller.setNet(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a new Dialog to find out whether two people are directly connected in some ways
     * as well as define relation between two people e.g. friends, parent, classmates, colleagues, couples
     * @return for further use, not used for now.
     */
    public boolean showPersonConnectionDialog(){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(MiniNet.class.getResource("/view/IsPersonConnectedDialog.fxml"));
            AnchorPane page=loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Find and add relation");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            PersonConnectionDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setNet(this);
            dialogStage.showAndWait();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void readFromTxt(String fileName, boolean flag) {
        Path path = Paths.get(fileName);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty()) {
                String[] attributes = line.split(",", -1);
                if (flag)
                    parsePeopleData(attributes);
                else
                    parseRelationData(attributes);
                line = bufferedReader.readLine();
            }
            if(flag){
                personList.addAll(youngChildrenList);
                personList.addAll(childList);
                personList.addAll(adultList);
            }

        }catch(NoSuchFileException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("NO data");
            alert.setHeaderText("Please correct file path");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parsePeopleData(String[] attributes) {
        String name = attributes[0].trim();
        String image = attributes[1].substring(1, attributes[1].length() - 1).trim();
        String status = attributes[2].substring(1, attributes[2].length() - 1).trim();
        String sex;
        if (attributes[3].trim().equals("M")) {
            sex = "Male";
        } else
            sex = "Female";
        Integer age = Integer.parseInt(attributes[4]);
        String state = attributes[5].trim();
        //System.out.println(attributes.length + ":" + name + "/" + image + "/" + status + "/" + sex + "/" + age + "/" + state);

        if (age <= 2) {
            youngChildrenList.add(new YoungChild(name, image, status, age, sex, state));
        } else if (age <= 16) {
            childList.add(new Child(name, image, status, age, sex, state));
        } else
            adultList.add(new Adult(name, image, status, age, sex, state));
    }


    private void parseRelationData(String[] attributes) {
        String firstName = attributes[0].trim();
        String secondName = attributes[1].trim();
        String relation = attributes[2].trim();
        int count = 0;
        for (Person p : personList) {
            if (p.getName().equals(firstName)) {
                searchAndSet(firstName, secondName, relation, p.getType());
                count += 1;
            } else if (p.getName().equals(secondName)) {
                searchAndSet(secondName, firstName, relation, p.getType());
                count += 1;
            }
            if (count == 2) break;
        }
    }

    private void searchAndSet(String name, String rName, String relation, int type) {
        if (type == 0) {
            for (YoungChild y : youngChildrenList) {
                if (y.getName().equals(name)) {
                    if (relation.equals("parent")) {
                        y.setParent(rName);
                    } else if (relation.equals("siblings")) {
                        y.addSibling(rName);
                    }
                    return;
                }
            }
        } else if (type == 1) {
            for (Child c : childList) {
                if (c.getName().equals(name)) {
                    if (relation.equals("parent")) {
                        c.setParent(rName);
                    } else if (relation.equals("siblings")) {
                        c.addSibling(rName);
                    } else if (relation.equals("classmate")) {
                        c.addClassmate(rName);
                    } else if (relation.equals("friends")) {
                        c.addFriend(rName);
                    }
                    return;
                }

            }
        } else if (type == 2) {
            for (Adult a : adultList) {
                if (a.getName().equals(name)) {
                    if (relation.equals("classmate")) {
                        a.addClassmate(rName);
                    } else if (relation.equals("colleague")) {
                        a.addColleague(rName);
                    } else if (relation.equals("couple")) {
                        a.setCouple(rName);
                    } else if (relation.equals("parent")) {
                        a.addChildren(rName);
                    } else if (relation.equals("friends")) {
                        a.addFriend(rName);
                    }else{
                        System.out.println(relation);
                    }
                    return;
                }
            }
        }
    }

    /**
     * Get the specified Person with name
     * @param name Person you want to find
     * @return Person found.
     */
    public Person getPerson(String name){
        System.out.println("get "+name);
        for(Person p: personList){
            if(p.getName().equals(name)){
                System.out.println("found");
                return p;
            }else
                System.out.println(p.getName());
        }
        System.out.println("------------------");
        return null;
    }


    /**
     * Get the specified Young Child with name
     * @param name Young Child you want to find
     * @return Young Child found.
     */
    public YoungChild getYoungChild(String name){
        for(YoungChild y: youngChildrenList){
            if(y.getName().equals(name))
                return y;
        }
        return null;
    }

    /**
     * Get the specified Child with name
     * @param name Child you want to find
     * @return  Child found.
     */
    public Child getChild(String name){
        for(Child c:childList){
            if(c.getName().equals(name))
                return c;
        }
        return null;
    }

    /**
     * Get the specified Adult with name
     * @param name Adult you want to find
     * @return Adult found.
     */
    public Adult getAdult(String name){
        for(Adult a:adultList){
            if(a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }

    /**
     * Judge if the two persons can be parents together.
     * @param father father's name
     * @param mother mother's name
     * @return
     */
    public boolean isParentRight(String father, String mother){
        for(Adult a:adultList){
            if(a.getName().equals(father)){
                if(a.getCouple().equals(mother)){
                    return true;
                }else{
                    System.out.println(a.getName()+"::"+a.getCouple());
                    return false;
                }
            }else if(a.getName().equals(mother)){
                if(a.getCouple().equals(father)){
                    return true;
                }else{
                    System.out.println(a.getName()+"::"+a.getCouple());
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * Construcor
     */
    public MiniNet() {
        readFromTxt("src/people1.txt", true);
        readFromTxt("src/relations1.txt", false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
