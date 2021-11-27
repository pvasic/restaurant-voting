package ru.pvasic.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ru.pvasic.restaurantvoting.model.Role;
import ru.pvasic.restaurantvoting.model.User;
import ru.pvasic.restaurantvoting.to.UserTo;

import java.util.Optional;
import java.util.Set;

@UtilityClass
public class UserUtil {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getLastName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setLastName(userTo.getLastName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? PASSWORD_ENCODER.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

    public static Optional<User> updateRole(User oldUser) {
        Set<Role> roles = oldUser.getRoles();
        if (!roles.contains(Role.MANAGER)) {
            roles.add(Role.MANAGER);
            oldUser.setRoles(roles);
            return Optional.of(oldUser);
        }
        return Optional.empty();
    }
}