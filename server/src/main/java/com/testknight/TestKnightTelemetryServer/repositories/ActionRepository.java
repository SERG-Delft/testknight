package com.testknight.TestKnightTelemetryServer.repositories;

import com.testknight.TestKnightTelemetryServer.domain.model.*;
import org.springframework.data.repository.*;

public interface ActionRepository extends CrudRepository<Action, String> {
}
