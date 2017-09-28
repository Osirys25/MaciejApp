package org.maciejowka.events.data;

import org.maciejowka.events.model.EventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciej on 28.09.17.
 */
public class WordpressEventsParser {
    private final List<WordpressEvent> wordpressEvents;

    public WordpressEventsParser(List<WordpressEvent> wordpressEvents) {
        this.wordpressEvents = wordpressEvents;
    }

    public List<EventModel> parseToEventModels(){
        List<EventModel> events = new ArrayList<>();
        for (WordpressEvent wpEvent : wordpressEvents){
            events.add(wpEvent.toEventModel());
        }
        return events;
    }
}
