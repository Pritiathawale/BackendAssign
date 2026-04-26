package com.assignment.BackendIntern.dto;

import com.assignment.BackendIntern.constant.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Task request body")
public class TaskDTO {

    @NotBlank(message = AppConstants.TITLE_REQUIRED)
    @Size(min = 2, max = 100, message = AppConstants.TITLE_SIZE)
    @Schema(description = "Task title", example = "Complete assignment")
    private String title;

    @Size(max = 500, message = AppConstants.DESC_SIZE)
    @Schema(description = "Task description", example = "Finish the backend intern task")
    private String description;

    @Pattern(regexp = AppConstants.STATUS_PATTERN, message = AppConstants.STATUS_INVALID)
    @Schema(description = "Task status", example = "PENDING",
            allowableValues = {"PENDING", "IN_PROGRESS", "DONE"})
    private String status;
}