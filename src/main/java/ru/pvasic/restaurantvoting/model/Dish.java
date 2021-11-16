package ru.pvasic.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity(name = "Dish")
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "date_time"}, name = "dishes_unique_name_date_time_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Dish extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "price", nullable = false)
    @NotNull
    private int price;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "restaurant_id", nullable = false)
    private Integer restaurantId;

    public Dish(Integer id, String name, int price, LocalDateTime dateTime, Integer restaurantId) {
        super(id);
        this.name = name;
        this.price = price;
        this.dateTime = dateTime;
        this.restaurantId = restaurantId;
    }
}
