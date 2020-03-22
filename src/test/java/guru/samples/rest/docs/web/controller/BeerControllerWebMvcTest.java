package guru.samples.rest.docs.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.samples.rest.docs.domain.Beer;
import guru.samples.rest.docs.repository.BeerRepository;
import guru.samples.rest.docs.web.model.BeerDTO;
import guru.samples.rest.docs.web.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.samples.rest.docs.web.controller.BeerController.BASE_URL;
import static guru.samples.rest.docs.web.model.BeerStyle.IPA;
import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "guru.samples.rest.docs.web.mapper")
public class BeerControllerWebMvcTest {

    private static final UUID BEER_ID = randomUUID();
    private static final String BEER_ID_AS_PATH_PARAMETER = "id";
    private static final String BASE_URL_WITH_BEER_ID = BASE_URL + "/{" + BEER_ID_AS_PATH_PARAMETER + "}";
    private static final String TEST_QUERY_PARAMETER = "isCold";

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

        mockMvc.perform(get(BASE_URL_WITH_BEER_ID, BEER_ID)
                .accept(APPLICATION_JSON)
                .param(TEST_QUERY_PARAMETER, "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(BEER_ID.toString()))))
                .andExpect(jsonPath("$.name", is(equalTo(BEER_NAME))))
                .andExpect(jsonPath("$.style", is(equalTo(BEER_STYLE.name()))))
                .andDo(document(BASE_URL,
                        pathParameters(
                                parameterWithName(BEER_ID_AS_PATH_PARAMETER).description("UUID of desired beer to get.")
                        ),
                        requestParameters(
                                parameterWithName(TEST_QUERY_PARAMETER).description("Indicates whether desired beer should be cold or not.")
                        ),
                        responseFields(
                                getResponseFields()
                        )
                ));

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
                .andExpect(jsonPath("$.style", is(equalTo(BEER_STYLE.name()))))
                .andDo(document(BASE_URL,
                        responseFields(
                                getResponseFields()
                        ),
                        requestFields(
                                getRequestFields()
                        )
                 ));

        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    public void shouldUpdateExistingBeer() throws Exception {
        BeerDTO beerToUpdate = getBeerDTO();
        Beer beerToReturn = getBeer();
        when(beerRepository.save(any(Beer.class))).thenReturn(beerToReturn);

        mockMvc.perform(put(BASE_URL_WITH_BEER_ID, BEER_ID)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(BEER_ID.toString()))))
                .andExpect(jsonPath("$.name", is(equalTo(BEER_NAME))))
                .andExpect(jsonPath("$.style", is(equalTo(BEER_STYLE.name()))))
                .andDo(document(BASE_URL,
                        pathParameters(
                                parameterWithName(BEER_ID_AS_PATH_PARAMETER).description("UUID of desired beer to update.")
                        ),
                        responseFields(
                                getResponseFields()
                        ),
                        requestFields(
                                getRequestFields()
                        )
                ));

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

    private List<FieldDescriptor> getResponseFields() {
        return asList(
                fieldWithPath("id").description("Beer ID, generated as UUID by DB sequence generator"),
                fieldWithPath("version").description("Version number"),
                fieldWithPath("createdDate").description("Date when beer entity was first registered in system"),
                fieldWithPath("lastModifiedDate").description("Last date when beer entity was modified"),
                fieldWithPath("name").description("Beer name"),
                fieldWithPath("style").description("Beer style"),
                fieldWithPath("upc").description("UPC of beer"),
                fieldWithPath("price").description("Beer price"),
                fieldWithPath("minOnHand").description("Minimal quantity of beer to have on hand"),
                fieldWithPath("quantityToBrew").description("Quantity of beer to brew in a single order")
        );
    }

    private List<FieldDescriptor> getRequestFields() {
        ConstrainedFields fields = new ConstrainedFields(BeerDTO.class);
        return asList(
                fields.withPath("id").ignored(),
                fields.withPath("version").ignored(),
                fields.withPath("createdDate").ignored(),
                fields.withPath("lastModifiedDate").ignored(),
                fields.withPath("name").description("Beer name"),
                fields.withPath("style").description("Beer style"),
                fields.withPath("upc").description("UPC of beer"),
                fields.withPath("price").description("Beer price"),
                fields.withPath("minOnHand").description("Minimal quantity of beer to have on hand"),
                fields.withPath("quantityToBrew").description("Quantity of beer to brew in a single order")
        );
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path)
                    .attributes(key("constraints")
                            .value(collectionToDelimitedString(this.constraintDescriptions.descriptionsForProperty(path), ". ")));
        }
    }
}
