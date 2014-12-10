package src.persistence.models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by tkachdan on 08-Nov-14.
 */
@Entity
public class Bar extends Model implements Comparable<Bar> {

    public static Model.Finder<Integer, Bar> find = new Model.Finder<Integer, Bar>(
            Integer.class, Bar.class
    );
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String name;


    public Bar(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Bar() {

    }

    public static void update(int id, Bar task) {
        task.update(id); // updates this entity, by specifying the entity ID
    }

    @Override
    public int compareTo(Bar o) {
        return id - o.id;
    }
}
