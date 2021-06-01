package com.testbuddy.TestBuddyTelemetryServer.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USAGE_RECORDS")
public class UsageRecord {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_Id")
    private String userId;

    @ManyToOne
    private Action actionId;

    @Column(name = "datetime")
    private LocalDateTime dateTime;

    public UsageRecord(String userId, Action actionId, LocalDateTime dateTime) {
        this.userId = userId;
        this.actionId = actionId;
        this.dateTime = dateTime;
    }

}
