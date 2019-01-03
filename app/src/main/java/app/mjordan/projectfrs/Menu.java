package app.mjordan.projectfrs;

public class Menu {
    private String title;
    private String ID;
    private String Type;
    private String Product;
    private String Des;
    private String Price;
    private boolean IsTitle;
    private boolean IsDesc;

    Menu(boolean isDesc,boolean isTitle, String ID, String title, String type, String product, String des, String price) {
        this.IsDesc=isDesc;
        this.IsTitle = isTitle;
        this.ID=ID;
        this.title=title;
        this.Type=type;
        this.Product=product;
        this.Des=des;
        this.Price=price;
    }

    public boolean getIsTitle() {
        return IsTitle;
    }

    public void setIsTitle(boolean IsTitle) {
        this.IsTitle=IsTitle;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public boolean isDesc() {
        return IsDesc;
    }

    public void setDesc(boolean desc) {
        IsDesc = desc;
    }
}
