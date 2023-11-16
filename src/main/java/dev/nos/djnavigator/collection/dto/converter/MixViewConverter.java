package dev.nos.djnavigator.collection.dto.converter;

import dev.nos.djnavigator.collection.dto.MixView;
import dev.nos.djnavigator.collection.model.Mix;

import static dev.nos.djnavigator.collection.dto.converter.TurntableViewConverter.turntableView;

public class MixViewConverter {

    public static MixView mixView(Mix mix) {
        return MixView.builder()
                .leftTurntable(turntableView(mix.getLeftTurntable()))
                .rightTurntable(turntableView(mix.getRightTurntable()))
                .build();
    }
}
