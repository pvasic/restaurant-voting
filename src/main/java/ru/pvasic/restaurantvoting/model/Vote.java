package ru.pvasic.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"}, name = "votes_unique_user_id_restaurant_id")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Vote extends BaseEntity {

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private Integer restaurantId;

    @Column(name = "user_id")
    @NotNull
    private Integer userId;

    @Column(name = "date_time")
    @NotNull
    private LocalDateTime dateTime;

    public Vote(Integer id, Integer userId, Integer restaurantId) {
        this(id, userId, restaurantId, LocalDateTime.now());
    }

    public Vote(Integer id, Integer userId, Integer restaurantId, LocalDateTime dateTime) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }
}
