package dev.nos.djnavigator.event;

import dev.nos.djnavigator.analysis.listener.GetAudioAnalysisOnTrackAddedEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.event.EventType.TRACK_ADDED_EVENT;

@Service
public class EventManagerFactory {

    private final GetAudioAnalysisOnTrackAddedEventListener getAudioAnalysisOnTrackAddedEventListener;

    @Autowired
    EventManagerFactory(GetAudioAnalysisOnTrackAddedEventListener getAudioAnalysisOnTrackAddedEventListener) {
        this.getAudioAnalysisOnTrackAddedEventListener = getAudioAnalysisOnTrackAddedEventListener;
    }

    EventManager createEventManager() {
        final var eventManager = new EventManager();
        eventManager.subscribe(TRACK_ADDED_EVENT, getAudioAnalysisOnTrackAddedEventListener);
        return eventManager;
    }
}
