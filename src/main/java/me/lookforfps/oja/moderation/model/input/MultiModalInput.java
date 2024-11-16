package me.lookforfps.oja.moderation.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.moderation.model.input.inputentry.InputEntry;
import me.lookforfps.oja.moderation.model.request.ModerationRequestDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MultiModalInput extends ModerationRequestDto {

    private List<InputEntry> input;
}
