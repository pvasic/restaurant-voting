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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "Dish")
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "date"}, name = "dishes_unique_name_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    private int price;

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDate date = LocalDate.now();

    @Column(name = "restaurant_id")
    @NotNull
    private Integer restaurantId;

    @Column(name = "user_id")
    @NotNull
    private Integer userId;

    public Dish(Integer id, String name, int price, Integer restaurantId, Integer userId) {
        this(id, name, price, LocalDate.now(), restaurantId, userId);
    }

    public Dish(Integer id, String name, int price, LocalDate date, Integer restaurantId, Integer userId) {
        super(id, name);
        this.price = price;
        this.date = date;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }
}
