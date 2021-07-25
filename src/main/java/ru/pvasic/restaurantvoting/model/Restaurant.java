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
import ru.pvasic.restaurantvoting.HasIdAndEmail;
import ru.pvasic.restaurantvoting.util.validation.NoHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"dishes", "user"})
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonBackReference(value = "user-restaurant")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference(value = "restaurant-dish")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference(value = "restaurant-vote")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> votes;

    public Restaurant(Integer id, String name, String address, String email, int voteCount, LocalDateTime dateTime) {
        super(id);
        this.name = name;
        this.address = address;
        this.email = email;
        this.voteCount = voteCount;
        this.dateTime = dateTime;
    }
}
