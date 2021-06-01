package com.testbuddy.TestBuddyTelemetryServer.model;

import lombok.*;

import javax.persistence.*;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usageRecords")
public class UsageRecord {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="userId")
    private String userId;

    @Column(name="actionId")
    private String actionId;

    @Column(name="dateTime")
    private LocalDateTime dateTime;

    public UsageRecord(String userId, String actionId, LocalDateTime dateTime) {
        this.userId = userId;
        this.actionId = actionId;
        this.dateTime = dateTime;
    }

}
