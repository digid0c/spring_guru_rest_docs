package guru.samples.rest.docs.web.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BeerPage extends PageImpl<BeerDTO> {

    public BeerPage(List<BeerDTO> content) {
        super(content);
    }

    public BeerPage(List<BeerDTO> content, Pageable pageable, Long total) {
        super(content, pageable, total);
    }
}
