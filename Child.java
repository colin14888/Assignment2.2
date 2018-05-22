package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Child class extends from YoungChild
 */
public class Child extends YoungChild {

    private List<String> classmate;
    private List<String> friend;

    /**
     * constructor
     * @param name child's name
     * @param image child's image's path
     * @param status child's status
     * @param age child's age
     * @param sex child's sex
     * @param state child's state
     */
    public Child(String name, String image, String status, Integer age, String sex, String state) {
        super(name, image, status, age, sex, state);
        super.setType(1);
        this.classmate=new ArrayList<>();
        this.friend=new ArrayList<>();
    }

    /**
     * Get child's classmates
     * @return
     */
    public List<String> getClassmate() {
        return classmate;
    }

    /**
     * Get child's friends
     * @return
     */
    public List<String> getFriend() {
        return friend;
    }

    /**
     * Add a friend
     * @param name
     */
    public void addFriend(String name){
        this.friend.add(name);
    }

    /**
     * Add a classmate
     * @param name
     */
    public void addClassmate(String name){
        this.classmate.add(name);
    }

    /**
     * Delete a friend
     * @param name
     */
    public void deleteFriend(String name){
        Iterator<String> iterator=friend.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(name)) {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * Delete a classmate
     * @param name
     */
    public void deleteClassmate(String name){
        Iterator<String> iterator=classmate.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(name)) {
                iterator.remove();
                return;
            }
        }
    }


}
