package ru.pvasic.restaurantvoting.util.validation;

import lombok.experimental.UtilityClass;
import ru.pvasic.restaurantvoting.HasId;
import ru.pvasic.restaurantvoting.error.IllegalRequestDataException;
import ru.pvasic.restaurantvoting.error.NotFoundException;
import ru.pvasic.restaurantvoting.model.Vote;

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

    public static void checkSingleModification(int count, String msg) {
        if (count != 1) {
            throw new NotFoundException(msg);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    public static void checkVoteTime(Vote vote) {
        if (!vote.getDateTime().toLocalTime().isBefore(DEFAULT_VOTE_TIME)) {
            throw new IllegalRequestDataException("Vote time =" + vote.getDateTime() + " should be less " + DEFAULT_VOTE_TIME + " for entity with id=" + vote.getId());
        }
    }

    public static int checkPositive(int voteCount) {
        if (voteCount >= 0) {
            return voteCount;
        } else {
            throw new IllegalRequestDataException("Vote count =" + voteCount + "must be greater 0");
        }
    }
}