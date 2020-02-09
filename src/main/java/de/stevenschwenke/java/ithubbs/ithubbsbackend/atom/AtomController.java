package de.stevenschwenke.java.ithubbs.ithubbsbackend.atom;

import com.rometools.rome.feed.atom.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atom")
public class AtomController {

    private final AtomService atomService;

    @Autowired
    public AtomController(AtomService atomService) {
        this.atomService = atomService;
    }

    @GetMapping(path = "")
    public Feed atom() {
        return atomService.createFeed();
    }

    @GetMapping(path = "/generalPublic")
    public Feed atomGeneralPublic() {
        return atomService.createFeedGeneralPublic();
    }
}
