package dev.lookforfps.oja.moderation.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.moderation.model.request.ModerationRequestDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StringArrayInput extends ModerationRequestDto {

    private List<String> input;
}
