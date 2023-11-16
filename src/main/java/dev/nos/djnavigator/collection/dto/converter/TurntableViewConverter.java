package dev.nos.djnavigator.collection.dto.converter;

import dev.nos.djnavigator.collection.dto.TurntableView;
import dev.nos.djnavigator.collection.model.Turntable;

public class TurntableViewConverter {

    public static TurntableView turntableView(Turntable mix) {
        return TurntableView.builder()
                .trackId(mix.getTrack().getId())
                .trackName(mix.getTrack().getName())
                .pitch(mix.getPitch())
                .build();
    }
}
