package ru.pvasic.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.pvasic.restaurantvoting.HasIdAndEmail;
import ru.pvasic.restaurantvoting.util.validation.NoHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "restaurants_unique_email_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Restaurant extends NamedEntity implements HasIdAndEmail {

    @Column(name = "user_id")
    @NotNull
    private Integer userId;

    @Column(name = "address", nullable = false)
    @NotEmpty
    private String address;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    private String email;

    @Column(name = "created", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created = new Date();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private List<Dish> dishes;

    public Restaurant(Integer id, Integer userId, String name, String address, String email) {
        this(id, userId, name, address, email, new Date());
    }

    public Restaurant(Integer id, Integer userId, String name, String address, String email, Date created) {
        super(id, name);
        this.userId = userId;
        this.address = address;
        this.email = email;
        this.created = created;
    }
}
