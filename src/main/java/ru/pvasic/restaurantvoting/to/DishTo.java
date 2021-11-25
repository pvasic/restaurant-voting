package ru.pvasic.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.pvasic.restaurantvoting.model.BaseEntity;
import ru.pvasic.restaurantvoting.model.NamedEntity;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo {

    @Column(name = "price", nullable = false)
    @NotNull
    int price;

    @Column(name = "restaurant_id")
    @NotNull
    Integer restaurantId;

    public DishTo(Integer id, String name, int price, Integer restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }
}
