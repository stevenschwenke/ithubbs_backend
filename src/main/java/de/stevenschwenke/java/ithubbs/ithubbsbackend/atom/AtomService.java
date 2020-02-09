package de.stevenschwenke.java.ithubbs.ithubbsbackend.atom;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class AtomService {

    private EventRepository eventRepository;

    public AtomService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    Feed createFeed() {
        Feed feed = new Feed();
        feed.setFeedType("atom_1.0");
        feed.setTitle("IT Hub Braunschweig Veranstaltungen");
        feed.setId("https://www.ithubbs.de/current");

        Content subtitle = new Content();
        subtitle.setType("text/plain");
        subtitle.setValue("Aktuelle IT-Veranstaltungen in Braunschweig");
        feed.setSubtitle(subtitle);

        feed.setUpdated(new Date());

        Stream<Event> eventStream = StreamSupport.stream(eventRepository.findAll().spliterator(), false);
        List<Entry> entries = eventStream.map(this::createEntry).collect(Collectors.toList());

        feed.setEntries(entries);
        return feed;
    }

    public Feed createFeedGeneralPublic() {

        Feed feed = new Feed();
        feed.setFeedType("atom_1.0");
        feed.setTitle("IT Hub Braunschweig nicht-technische Veranstaltungen");
        feed.setId("https://www.ithubbs.de/current");

        Content subtitle = new Content();
        subtitle.setType("text/plain");
        subtitle.setValue("Aktuelle nicht-technische IT-Veranstaltungen in Braunschweig");
        feed.setSubtitle(subtitle);

        feed.setUpdated(new Date());

        Stream<Event> eventStream = eventRepository.findAllGeneralPublic().stream();
        List<Entry> entries = eventStream.map(this::createEntry).collect(Collectors.toList());

        feed.setEntries(entries);
        return feed;
    }

    private Entry createEntry(Event event) {

        Entry entry = new Entry();

        Link link = new Link();
        link.setHref(event.getUrl());
        entry.setAlternateLinks(Collections.singletonList(link));
        entry.setId(event.getUrl());
        entry.setTitle(event.getDatetime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + ": " + event.getName());

        Content summary = new Content();
        summary.setType("text/plain");
        summary.setValue(event.getDatetime() + ": " + event.getUrl());
        entry.setSummary(summary);
        return entry;
    }
}
