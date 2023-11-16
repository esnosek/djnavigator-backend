package dev.nos.djnavigator.analysis.repository;

import dev.nos.djnavigator.analysis.model.AudioAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AudioAnalysisRepository {

    public AudioAnalysis save(AudioAnalysis audioAnalysis) {
        log.info(String.format("AudioAnalysis %s has been saved", audioAnalysis.toString()));
        return audioAnalysis;
    }

}
