package org.kdkbuilds.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.kdkbuilds.exceptions.InvalidDescriptionException;
import org.kdkbuilds.exceptions.InvalidIdException;

public final class Task {

    private final int id;
    private final String description;

    private static void validateTask(int id, String description) {
        if (id <= 0) {
            throw new InvalidIdException("ID must be a positive integer");
        }
        if (description == null || description.strip().isBlank()) {
            throw new InvalidDescriptionException("Description can not be null or empty!");
        }
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Task(@JsonProperty("id") int id, @JsonProperty("description") String description) {
        validateTask(id, description);
        this.id = id;
        this.description = description.strip();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + " : " + description;
    }
}
