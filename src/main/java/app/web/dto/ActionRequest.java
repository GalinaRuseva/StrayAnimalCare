package app.web.dto;

import app.action.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionRequest {

    @NotNull(message = "You must select an action type!")
    private Type type;

    @NotBlank(message = "Description cannot be empty.")
    private String description;
}
