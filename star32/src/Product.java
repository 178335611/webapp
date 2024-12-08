
public class Product {
    private int pno;
    private String pname;
    private int sno;
    private String description;
    private double price;

    public Product(int pno,int sno,String pname, String description, double price) {
        this.pno = pno;
        this.sno = sno;
        this.pname = pname;
        this.description = description;
        this.price = price;
    }

    // 省略 getter 和 setter 方法
}
