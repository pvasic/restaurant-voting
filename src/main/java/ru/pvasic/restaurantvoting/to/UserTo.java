package ru.pvasic.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.pvasic.restaurantvoting.HasIdAndEmail;
import ru.pvasic.restaurantvoting.util.validation.NoHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserTo extends NamedTo implements HasIdAndEmail {

    @Email
    @NotBlank
    @Size(max = 100)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String email;

    @NotBlank
    @Size(min = 2, max = 100)
    @NoHtml
    String lastName;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;

    public UserTo(Integer id, String name, String lastName, String email, String password) {
        super(id, name);
        this.email = email;
        this.lastName = lastName;
        this.password = password;
    }
}
