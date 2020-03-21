package guru.samples.rest.docs.web.controller;

import guru.samples.rest.docs.repository.BeerRepository;
import guru.samples.rest.docs.web.mapper.BeerMapper;
import guru.samples.rest.docs.web.model.BeerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static guru.samples.rest.docs.web.controller.BeerController.BASE_URL;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class BeerController {

    public static final String BASE_URL = "/api/v1/beer";

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @GetMapping("/{id}")
    public BeerDTO findById(@PathVariable UUID id) {
        return beerMapper.beerToBeerDTO(beerRepository.findById(id).orElse(null));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public BeerDTO create(@RequestBody @Validated BeerDTO beerDTO) {
        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)));
    }

    @PutMapping("/{id}")
    public BeerDTO update(@PathVariable UUID id, @RequestBody @Validated BeerDTO beerDTO) {
        beerDTO.setId(id);
        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)));
    }
}
