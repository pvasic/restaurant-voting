package ru.pvasic.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.util.ProxyUtils;
import ru.pvasic.restaurantvoting.HasIdAndEmail;
import ru.pvasic.restaurantvoting.util.validation.NoHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.pvasic.restaurantvoting.util.HashUtil.HASH_VALUE;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "restaurants_unique_email_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends AbstractBaseEntity implements HasIdAndEmail {
    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "address", nullable = false)
    @NotEmpty
    private String address;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    private String email;

    @Column(name = "vote_count", nullable = false, columnDefinition = "int default 0")
    @NotNull
    @Min(0)
    private int voteCount = 0;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    // TODO fix proxy (not passed test RestaurantControllerTest
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String address, String email, int voteCount, LocalDateTime dateTime) {
        super(id);
        this.name = name;
        this.address = address;
        this.email = email;
        this.voteCount = voteCount;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        Restaurant that = (Restaurant) o;

        if (id != null && id.equals(that.id)) {
            return getId().equals(that.getId()) && getName().equals(that.getName()) && getAddress() == (that.getAddress())
                    && getEmail().equals(that.getEmail()) && getVoteCount() == that.getVoteCount() && getDateTime().equals(that.getDateTime());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id == null ? HASH_VALUE : id, getName(), getName(), getAddress(), getEmail(), getVoteCount(), getDateTime());
    }
}
