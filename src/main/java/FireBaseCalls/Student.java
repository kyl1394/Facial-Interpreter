package FireBaseCalls;

/**
 * Created by bptrent on 9/12/2016.
 */
public class Student {
    public String name;
    public String notes;
    public String lastSeen;

    public Student(String Name, String Notes, String LastSeen){
        this.name = Name;
        this.notes = Notes;
        this.lastSeen = LastSeen;
    }

    public String toString(){
        String output = new String();

        output += "{name=" + name + ", notes=" + ", lastSeen="+ lastSeen + "}";

        return output;
    }
}
