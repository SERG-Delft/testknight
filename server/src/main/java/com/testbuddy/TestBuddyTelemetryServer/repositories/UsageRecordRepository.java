package com.testbuddy.TestBuddyTelemetryServer.repositories;

import com.testbuddy.TestBuddyTelemetryServer.model.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import javax.transaction.*;

@Transactional
@Repository
public interface UsageRecordRepository extends CrudRepository<UsageRecord, Long> {
}
