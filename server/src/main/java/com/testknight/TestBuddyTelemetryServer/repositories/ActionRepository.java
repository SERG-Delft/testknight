package com.testknight.TestBuddyTelemetryServer.repositories;

import com.testknight.TestBuddyTelemetryServer.domain.model.*;
import org.springframework.data.repository.*;

public interface ActionRepository extends CrudRepository<Action, String> {
}
