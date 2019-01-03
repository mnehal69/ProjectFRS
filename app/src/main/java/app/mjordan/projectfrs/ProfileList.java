package app.mjordan.projectfrs;

public class ProfileList {
    private String title;
    private String value;
    private int type;

    ProfileList(String title, String value,int type){
        this.title=title;
        if(value.equals("null")) {
            this.value = "No "+this.title.toLowerCase()+" Specified";
        }else{
            this.value = value;
        }
        this.type=type;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
