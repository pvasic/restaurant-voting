package ru.pvasic.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude ={"dishes"})
public class Restaurant extends NamedEntity {
    @Column(name = "address", nullable = false)
    @NotEmpty
    private String address;

    @Column(name = "votes_number")
    private int votesNumber;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime created;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String address, int votesNumber, LocalDateTime created) {
        super(id, name);
        this.address = address;
        this.votesNumber = votesNumber;
        this.created = created;
    }
}
