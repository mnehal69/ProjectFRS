package app.mjordan.projectfrs;

public class Rest_List {
        private String id;
        private String logo;
        private String Name;
        private Boolean Popular;
        private String background;
        private String Dest;
        private String Location;
        private String Time;
        private String Rating;

        Rest_List(String RID, String Rlogo, String Rname, Boolean Rpopular, String Rbackground, String RDest, String RLocation, String RTime, String Rrating){
            this.id=RID;
            this.logo=Rlogo;
            this.Name=Rname;
            this.Popular=Rpopular;
            this.background=Rbackground;
            this.Dest=RDest;
            this.Location=RLocation;
            this.Time=RTime;
            this.Rating=Rrating;
        }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getPopular() {
        return Popular;
    }

    public void setPopular(Boolean popular) {
        Popular = popular;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}
