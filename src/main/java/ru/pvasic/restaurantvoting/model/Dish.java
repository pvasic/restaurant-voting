package ru.pvasic.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "Dish")
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "created"}, name = "dishes_unique_name_created_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Dish extends BaseEntity implements Serializable{

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "price", nullable = false)
    @NotNull
    private int price;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotAudited
    @JsonBackReference
    private Restaurant restaurant;

    public Dish(Integer id, String name, int price, LocalDateTime created, int userId) {
        super(id);
        this.name = name;
        this.price = price;
        this.created = created;
        this.userId = userId;
    }
}
