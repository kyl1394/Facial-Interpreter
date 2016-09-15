/**
 * Created by bptrent on 9/12/2016.
 */
public class Student {
    public String Name;
    public String Notes;
    public String LastSeen;

    public Student(String Name, String Notes, String LastSeen){
        this.Name = Name;
        this.Notes = Notes;
        this.LastSeen = LastSeen;
    }

    public String toString(){
        String output = new String();

        output += "{Name=" + Name + ", Notes=" + ", LastSeen="+ LastSeen + "}";

        return output;
    }
}
