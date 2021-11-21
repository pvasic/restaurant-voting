package ru.pvasic.restaurantvoting.util.validation;

import lombok.experimental.UtilityClass;
import ru.pvasic.restaurantvoting.HasId;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.error.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.pvasic.restaurantvoting.util.VoteUtil.DEFAULT_VOTE_TIME;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    public static void checkVoteDateTime(Integer voteId, LocalDate oldVoteDate) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        boolean check = oldVoteDate.atStartOfDay().toLocalDate().equals(dateTimeNow.toLocalDate());
        if (check) {
            if (!dateTimeNow.toLocalTime().isBefore(DEFAULT_VOTE_TIME)) {
                throw new IllegalRequestDataException("Time now =" + dateTimeNow + ". You can only vote up to " + DEFAULT_VOTE_TIME + ", for Vote with id=" + voteId);
            }
        }
    }
}