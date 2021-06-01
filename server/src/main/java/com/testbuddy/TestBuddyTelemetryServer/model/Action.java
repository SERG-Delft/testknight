package com.testbuddy.TestBuddyTelemetryServer.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ACTIONS")
public class Action {
    @Id
    @Column(name = "action_Id")
    private String actionId;
}
