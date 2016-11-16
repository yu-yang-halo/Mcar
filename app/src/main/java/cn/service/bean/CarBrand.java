package cn.service.bean;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CarBrand {
    private int id;
    private String letter;
    private String brand;//车品牌
    private String carLine;//车系
    private String name;//车具体信息,排量...


    @Override
    public String toString() {
        return "CarBrand{" +
                "id=" + id +
                ", letter='" + letter + '\'' +
                ", brand='" + brand + '\'' +
                ", carLine='" + carLine + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public CarBrand(int id, String letter, String brand, String carLine, String name) {
        this.id = id;
        this.letter = letter;
        this.brand = brand;
        this.carLine = carLine;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;

    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarLine() {
        return carLine;
    }

    public void setCarLine(String carLine) {
        this.carLine = carLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
