package me.lookforfps.oja.moderation.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.moderation.model.request.ModerationRequestDto;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StringInput extends ModerationRequestDto {

    private String input;
}
