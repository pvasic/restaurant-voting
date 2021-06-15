package ru.pvasic.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.pvasic.restaurantvoting.HasId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity(name = "Dish")
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "date_time"}, name = "dishes_unique_name_datetime_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Dish extends BaseEntity implements Persistable<Integer>, HasId {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "price", nullable = false)
    @NotNull
    private int price;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotAudited
    private Restaurant restaurant;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private int userId;

    public Dish(Integer id, String name, int price, LocalDateTime created, int userId) {
        super(id);
        this.name = name;
        this.price = price;
        this.created = created;
        this.userId = userId;
    }
}
