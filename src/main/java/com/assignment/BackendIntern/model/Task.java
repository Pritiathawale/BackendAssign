package com.assignment.BackendIntern.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@Schema(description = "Task entity")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Task ID", example = "1")
    private Long id;

    @Schema(description = "Task title", example = "Complete assignment")
    private String title;

    @Schema(description = "Task description", example = "Finish the backend intern task")
    private String description;

    @Schema(description = "Task status", example = "PENDING",
            allowableValues = {"PENDING", "IN_PROGRESS", "DONE"})
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore                    
    private User user;
}