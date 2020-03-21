package guru.samples.rest.docs.bootstrap;

import guru.samples.rest.docs.domain.Beer;
import guru.samples.rest.docs.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerDataLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) {
        loadBeers();
    }

    private void loadBeers() {
        if (beerRepository.count() != 0) {
            return;
        }
        log.debug("Loading beers...");

        Beer beer = Beer.builder()
                .name("Mango Bobs")
                .style("IPA")
                .quantityToBrew(200)
                .minOnHand(12)
                .upc(337010000001L)
                .price(valueOf(12.95))
                .build();

        Beer anotherBeer = Beer.builder()
                .name("Galaxy Cat")
                .style("PALE_ALE")
                .quantityToBrew(300)
                .minOnHand(15)
                .upc(337010000002L)
                .price(valueOf(11.95))
                .build();

        beerRepository.saveAll(asList(beer, anotherBeer));
        log.debug("Beers loaded!");
    }
}
