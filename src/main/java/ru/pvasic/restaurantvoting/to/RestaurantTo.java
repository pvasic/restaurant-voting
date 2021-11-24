package ru.pvasic.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.pvasic.restaurantvoting.model.NamedEntity;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    Integer restaurantId;

    public RestaurantTo(Integer id, String name, Integer restaurantId) {
        super(id, name);
        this.restaurantId = restaurantId;
    }
}
