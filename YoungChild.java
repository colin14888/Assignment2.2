package model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * YoungChild's class extends from person
 */
public class YoungChild extends Person{

    private List<String> parents;
    private List<String> siblings;


    /**
     * constructor
     * @param name young child's name
     * @param image young child's image's path
     * @param status young child's status
     * @param age young child's age
     * @param sex young child's sex
     * @param state young child's state
     */
    public YoungChild(String name, String image, String status, Integer age, String sex, String state) {
        super(name, image, status, age, sex, state);
        this.setType(0);
        this.siblings=new ArrayList<>();
        this.parents=new ArrayList<>();
    }


    /**
     * return young child's siblings
     * @return
     */
    public List<String> getSiblings() {
        return siblings;
    }

    /**
     * return young child's parents
     * @return
     */
    public List<String> getParents() {
        return parents;
    }

    public void setSiblings(List<String> siblings) {
        this.siblings = siblings;
    }

    public void setParent(String parent){
        parents.add(parent);
    }

    /**
     * add a sibling when add a new Child or Young Child
     * @param name
     */
    public void addSibling(String name) {
        this.siblings.add(name);
    }

    /**
     * delete a sibling
     * @param targetName
     */
    public void deleteSibling(String targetName){
        Iterator<String> iterator=siblings.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(targetName)) {
                iterator.remove();
                return;
            }
        }
    }

}
