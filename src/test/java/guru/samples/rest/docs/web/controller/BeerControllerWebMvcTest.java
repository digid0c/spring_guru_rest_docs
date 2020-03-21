package guru.samples.rest.docs.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.samples.rest.docs.domain.Beer;
import guru.samples.rest.docs.repository.BeerRepository;
import guru.samples.rest.docs.web.model.BeerDTO;
import guru.samples.rest.docs.web.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static guru.samples.rest.docs.web.controller.BeerController.BASE_URL;
import static guru.samples.rest.docs.web.model.BeerStyle.IPA;
import static java.math.BigDecimal.valueOf;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "guru.samples.rest.docs.web.mapper")
public class BeerControllerWebMvcTest {

    private static final UUID BEER_ID = randomUUID();
    private static final String BASE_URL_WITH_BEER_ID = BASE_URL + "/" + BEER_ID;

    private static final String BEER_NAME = "Mango Bobs";
    private static final BeerStyle BEER_STYLE = IPA;
    private static final Long BEER_UPC = 337010000001L;
    private static final BigDecimal BEER_PRICE = valueOf(11.95);
    private static final Integer BEER_MIN_ON_HAND = 12;
    private static final Integer BEER_QUANTITY_TO_BREW = 200;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeerRepository beerRepository;

    @Test
    public void shouldFindBeerById() throws Exception {
        Beer beer = getBeer();
        when(beerRepository.findById(BEER_ID)).thenReturn(Optional.of(beer));

        mockMvc.perform(get(BASE_URL_WITH_BEER_ID)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(BEER_ID.toString()))))
                .andExpect(jsonPath("$.name", is(equalTo(BEER_NAME))))
                .andExpect(jsonPath("$.style", is(equalTo(BEER_STYLE.name()))));

        verify(beerRepository).findById(BEER_ID);
    }

    @Test
    public void shouldCreateNewBeer() throws Exception {
        BeerDTO beerToCreate = getBeerDTO();
        Beer beerToReturn = getBeer();
        when(beerRepository.save(any(Beer.class))).thenReturn(beerToReturn);

        mockMvc.perform(post(BASE_URL)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(BEER_ID.toString()))))
                .andExpect(jsonPath("$.name", is(equalTo(BEER_NAME))))
                .andExpect(jsonPath("$.style", is(equalTo(BEER_STYLE.name()))));

        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    public void shouldUpdateExistingBeer() throws Exception {
        BeerDTO beerToUpdate = getBeerDTO();
        Beer beerToReturn = getBeer();
        when(beerRepository.save(any(Beer.class))).thenReturn(beerToReturn);

        mockMvc.perform(put(BASE_URL_WITH_BEER_ID)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(BEER_ID.toString()))))
                .andExpect(jsonPath("$.name", is(equalTo(BEER_NAME))))
                .andExpect(jsonPath("$.style", is(equalTo(BEER_STYLE.name()))));

        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    public void shouldNotCreateNewBeer() throws Exception {
        BeerDTO invalidBeer = getInvalidBeer();

        mockMvc.perform(post(BASE_URL)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBeer)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(beerRepository);
    }

    private BeerDTO getBeerDTO() {
        return BeerDTO.builder()
                .name(BEER_NAME)
                .style(BEER_STYLE)
                .upc(BEER_UPC)
                .price(BEER_PRICE)
                .minOnHand(BEER_MIN_ON_HAND)
                .quantityToBrew(BEER_QUANTITY_TO_BREW)
                .build();
    }

    private Beer getBeer() {
        return Beer.builder()
                .id(BEER_ID)
                .name(BEER_NAME)
                .style(BEER_STYLE.name())
                .build();
    }

    private BeerDTO getInvalidBeer() {
        return BeerDTO.builder()
                .name(BEER_NAME)
                .style(BEER_STYLE)
                .build();
    }
}
