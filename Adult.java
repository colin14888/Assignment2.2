package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Adult class extends from child
 */
public class Adult extends Child {

    private List<String> colleague;
    private String couple;
    private List<String> children;

    /**
     * constructer
     * @param name adult's name
     * @param image adult's image' path
     * @param status adult's status
     * @param age adult's age
     * @param sex adult's sex
     * @param state adult's state
     */
    public Adult(String name, String image, String status, Integer age, String sex, String state) {
        super(name, image, status, age, sex, state);
        super.setType(2);
        this.children=new ArrayList<>();
        this.colleague=new ArrayList<>();
        String couple="";
    }


    /**
     * @return the list of colleagues
     */
    public List<String> getColleague() {
        return colleague;
    }

    /**
     * Add a colleague to colleagues
     * @param name name of the one to be added
     */
    public void addColleague(String name){ this.colleague.add(name); }


    /**
     * Return adult's couple's name
     * @return
     */
    public String getCouple() {
        return couple;
    }

    /**
     * Set adult's couple's name
     * @param couple
     */
    public void setCouple(String couple) {
        this.couple = couple;
    }

    /**
     * Return adult's children's names
     * @return
     */
    public List<String> getChildren() {
        return children;
    }

    /**
     * Add a child to children
     * @param name name of the one to be added
     */
    public void addChildren(String name){
        children.add(name);
    }

    /**
     * delete a child
     * @param name
     */
    public void deleteChild(String name){
        Iterator<String> iterator=children.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(name)) {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * delete a colleague
     * @param name
     */
    public void deleteColleague(String name){
        Iterator<String> iterator=colleague.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(name)) {
                iterator.remove();
                return;
            }
        }
    }
}
