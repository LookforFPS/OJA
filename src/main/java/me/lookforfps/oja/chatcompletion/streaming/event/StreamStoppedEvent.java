package me.lookforfps.oja.chatcompletion.streaming.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamStoppedEvent {

    private String reason;
}
