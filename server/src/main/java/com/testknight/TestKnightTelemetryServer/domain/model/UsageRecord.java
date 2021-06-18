package com.testknight.TestKnightTelemetryServer.domain.model;

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

    /**
     * Creates a new UsageRecord object.
     *
     * @param userId the id of the user.
     * @param actionId the id of the action.
     * @param dateTime the date-time of the event.
     */
    public UsageRecord(String userId, Action actionId, LocalDateTime dateTime) {
        this.userId = userId;
        this.actionId = actionId;
        this.dateTime = dateTime;
    }

}
