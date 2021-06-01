package com.testbuddy.TestBuddyTelemetryServer.repositories;

import com.testbuddy.TestBuddyTelemetryServer.model.*;
import org.springframework.data.repository.*;

public interface ActionRepository extends CrudRepository<Action, String> {
}
