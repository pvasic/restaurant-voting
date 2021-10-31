package ru.pvasic.restaurantvoting;

import ru.pvasic.restaurantvoting.model.Role;
import ru.pvasic.restaurantvoting.model.User;
import ru.pvasic.restaurantvoting.util.JsonUtil;

import java.util.Collections;
import java.util.Date;

public class UserTestData {
    public static final ru.pvasic.restaurantvoting.TestMatcher<User> USER_MATCHER = ru.pvasic.restaurantvoting.TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password", "restaurant", "votes");

    public static final int USER_ID = 1;
    public static final int MANAGER_ID = 2;
    public static final int ADMIN_ID = 3;
    public static final int NOT_FOUND = 100;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String MANAGER_MAIL = "manager@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User user = new User(USER_ID, "User_First", "User_Last", USER_MAIL, "userPassword", Role.USER);
    public static final User manager = new User(MANAGER_ID, "Manager_First", "Manager_Last", MANAGER_MAIL, "managerPassword", Role.USER, Role.MANAGER);
    public static final User admin = new User(ADMIN_ID, "Admin_First", "Admin_Last", ADMIN_MAIL, "adminPassword", Role.ADMIN, Role.MANAGER);

    public static User getNew() {
        return new User(null, "New_First","New_Last", "new@gmail.com", "newPassword",  false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setFirstName("UpdatedFirst");
        updated.setLastName("UpdatedLast");
        updated.setPassword("updatedPassword");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}
