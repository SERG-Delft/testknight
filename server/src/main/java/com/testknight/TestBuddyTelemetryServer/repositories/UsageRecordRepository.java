package com.testknight.TestBuddyTelemetryServer.repositories;

import com.testknight.TestBuddyTelemetryServer.domain.model.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import javax.transaction.*;

@Transactional
@Repository
public interface UsageRecordRepository extends CrudRepository<UsageRecord, Long> {
}
