package con.github.sparkmuse.flightalarm.repository;

import con.github.sparkmuse.flightalarm.entity.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PriceRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PriceRepository priceRepository;

    @Test
    public void save() {

        Price price = new Price(Price.ID, 2.0d, 1.0d);

        priceRepository.save(price);

        Price found = mongoTemplate.findById(Price.ID, Price.class);

        assertThat(found, is(price));
    }

    @Test
    public void updatePrice() {

        Price price = new Price(Price.ID, 2.0d, 3.0d);
        Price updated = new Price(Price.ID, 3.0d, 5.0d);

        priceRepository.save(price);

        priceRepository.updatePrice(5.0d);

        Price found = mongoTemplate.findById(Price.ID, Price.class);

        assertThat(found, is(updated));
    }

    @Test
    public void updatePriceSavesNewWhenNotFound() {

        Price price = new Price(Price.ID, 0.0d, 3.0d);

        priceRepository.updatePrice(3.0d);

        Price found = mongoTemplate.findById(Price.ID, Price.class);

        assertThat(found, is(price));
    }
}