package ru.pvasic.restaurantvoting.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "created"}, name = "dishes_unique_user_id_created_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = "user")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Vote extends BaseEntity implements Serializable {

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotAudited
    private User user;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private int restaurantId;

    @Column(name = "created", nullable = false)
    @NotNull
    private LocalDateTime created;
}
