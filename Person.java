package model;

import javafx.beans.property.*;

/**
 * Person Class, base class of Adult, Child, YoungChild
 * Provides the most basic property.
 */
public class Person {
    private StringProperty name;
    private IntegerProperty age;
    private StringProperty state;
    private StringProperty image;
    private StringProperty status;
    private StringProperty sex;
    private IntegerProperty type;


    /**
     * Constructor
     */
    public Person() {

    }

    /**
     * constructor
     * @param name person's name
     * @param image person's image's path
     * @param status person's status
     * @param age person's age
     * @param sex person's sex
     * @param state person's state
     */
    public Person(String name, String image, String status, Integer age, String sex, String state) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.state = new SimpleStringProperty(state);
        this.image = new SimpleStringProperty(image);
        this.status = new SimpleStringProperty(status);
        this.sex = new SimpleStringProperty(sex);
        if(age<=2){
            this.type=new SimpleIntegerProperty(0);
        }
        else if(age<=16&&age>2)
            this.type=new SimpleIntegerProperty(1);
        else
            this.type=new SimpleIntegerProperty(2);

    }

    /**
     * Retrun name
     * @return
     */
    public String getName() {
        return name.get();
    }

    /**
     * Return nameProperty
     * @return
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Return age
     * @return
     */
    public int getAge() {
        return age.get();
    }


    /**
     * Set age
     * @param age
     */
    public void setAge(int age) {
        this.age.set(age);
    }

    /**
     * Return state
     * @return
     */
    public String getState() {
        return state.get();
    }

    /**
     * Return image's path
     * @return
     */
    public String getImage() {
        return image.get();
    }

    /**
     * Return status
     * @return
     */
    public String getStatus() {
        return status.get();
    }


    /**
     * Return sex
     * @return
     */
    public String getSex() {
        return sex.get();
    }

    /**
     * Return person's type
     * 0 for young child
     * 1 for child
     * 2 for adult.
     * @return
     */
    public int getType() { return type.get(); }

    /**
     * Set person's type
     * @param type
     */
    public void setType(int type) { this.type.set(type); }
}
