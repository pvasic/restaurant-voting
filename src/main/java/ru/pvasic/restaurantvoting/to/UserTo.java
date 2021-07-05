package ru.pvasic.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.pvasic.restaurantvoting.HasIdAndEmail;
import ru.pvasic.restaurantvoting.util.validation.NoHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserTo extends BaseTo implements HasIdAndEmail, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 128)
    @NoHtml
    String firstName;

    @NotBlank
    @Size(max = 128)
    @NoHtml
    String lastName;

    @Email
    @NotBlank
    @Size(max = 100)
    @NoHtml // https://stackoverflow.com/questions/17480809
    String email;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;

    public UserTo(Integer id, String firstName, String lastName, String email, String password) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
