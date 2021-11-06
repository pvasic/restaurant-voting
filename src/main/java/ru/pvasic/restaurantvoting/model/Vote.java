package ru.pvasic.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.util.ProxyUtils;
import ru.pvasic.restaurantvoting.HasId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.pvasic.restaurantvoting.util.HashUtil.HASH_VALUE;

@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Vote extends AbstractBaseEntity implements Persistable<Integer>, HasId {

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private Integer restaurantId;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    public Vote(Integer id, @NotNull Integer restaurantId, @NotNull LocalDateTime dateTime) {
        super(id);
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        Vote that = (Vote) o;
        if (id != null && id.equals(that.id)) {
            return getId().equals(that.getId()) && getRestaurantId().equals(that.getRestaurantId()) && getDateTime().equals(that.getDateTime());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id == null ? HASH_VALUE : id, getRestaurantId(), getDateTime());
    }
}
