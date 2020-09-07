package org.eshishkin.edu.sender.service;

import java.util.UUID;
import org.eshishkin.edu.sender.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, UUID> {
}
