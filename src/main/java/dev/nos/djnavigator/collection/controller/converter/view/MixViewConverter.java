package dev.nos.djnavigator.collection.controller.converter.view;

import dev.nos.djnavigator.collection.dto.MixView;
import dev.nos.djnavigator.collection.dto.TurntableView;
import dev.nos.djnavigator.collection.model.Mix;
import org.springframework.stereotype.Service;

@Service
public class MixViewConverter {

    public static MixView toMixView(Mix mix) {
        return MixView.builder()
                .leftTurntable(
                        TurntableView.builder()
                                .trackId(mix.getLeftTurntable().getTrack().getId())
                                .trackName(mix.getLeftTurntable().getTrack().getName())
                                .pitch(mix.getLeftTurntable().getPitch())
                                .build()
                )
                .rightTurntable(
                        TurntableView.builder()
                                .trackId(mix.getRightTurntable().getTrack().getId())
                                .trackName(mix.getRightTurntable().getTrack().getName())
                                .pitch(mix.getRightTurntable().getPitch())
                                .build()
                )
                .build();
    }

}
