package Robin.MariokartBackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Remote_controllers")
public class RemoteController {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "compatible_with")
    private String compatibleWith;
    private String name;
    private String brand;
    private Double price;
    @Column(name = "original_stock")
    private Integer originalStock;
    private Integer sold;

    @OneToOne(mappedBy = "remoteController")
    private Television television;

    public RemoteController(Long id, String compatibleWith, String name, String brand, Double price, Integer originalStock, Integer sold, Television television) {
        this.id = id;
        this.compatibleWith = compatibleWith;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.originalStock = originalStock;
        this.sold = sold;
        this.television = television;
    }

    public RemoteController() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompatibleWith() {
        return compatibleWith;
    }

    public void setCompatibleWith(String compatibleWith) {
        this.compatibleWith = compatibleWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getOriginalStock() {
        return originalStock;
    }

    public void setOriginalStock(Integer originalStock) {
        this.originalStock = originalStock;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Television getTelevision() {
        return television;
    }

    public void setTelevision(Television television) {
        this.television = television;
    }
}
