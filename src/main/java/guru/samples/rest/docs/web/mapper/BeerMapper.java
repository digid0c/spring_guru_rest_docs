package guru.samples.rest.docs.web.mapper;

import guru.samples.rest.docs.domain.Beer;
import guru.samples.rest.docs.web.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDTO beerToBeerDTO(Beer beer);

    Beer beerDTOToBeer(BeerDTO beerDTO);
}
