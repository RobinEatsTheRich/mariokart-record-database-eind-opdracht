package Robin.MariokartBackend.inputDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class RemoteControllerInputDto {

    private Long id;
    private String compatibleWith;
    @NotNull
    @Size(min=1, max=128)
    private String name;
    @NotNull
    @Size(min=1, max=128)
    private String brand;
    @Positive
    private Double price;
    @PositiveOrZero
    private Integer originalStock;
    @PositiveOrZero
    private Integer sold;

    public RemoteControllerInputDto(Long id, String compatibleWith, String name, String brand, Double price, Integer originalStock, Integer sold) {
        this.id = id;
        this.compatibleWith = compatibleWith;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.originalStock = originalStock;
        this.sold = sold;
    }

    public Long getId() {
        return id;
    }

    public String getCompatibleWith() {
        return compatibleWith;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getOriginalStock() {
        return originalStock;
    }

    public Integer getSold() {
        return sold;
    }
}
