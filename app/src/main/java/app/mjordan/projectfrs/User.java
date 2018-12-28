package app.mjordan.projectfrs;

public class User {
    public String ID;
    public String Name;
    public String Email;
    public String Address;
    public String Avatar;
    public String Contact;

    public boolean sameVal(User another){
        if(!another.Name.equals(this.Name)){
            return false;
        }
        if(!another.Address.equals(this.Address)){
            return false;
        }
        if(!another.Contact.equals(this.Contact)){
            return false;
        }
        return true;
    }
}
