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
import java.time.LocalDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"}, name = "vote_unique_user_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Vote extends AbstractBaseEntity {

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Integer userId;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private Integer restaurantId;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    public Vote(Integer id, @NotNull Integer userId, @NotNull Integer restaurantId, @NotNull LocalDateTime dateTime) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }
}
