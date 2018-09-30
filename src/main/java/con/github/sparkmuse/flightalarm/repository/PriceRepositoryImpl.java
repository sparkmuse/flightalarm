package con.github.sparkmuse.flightalarm.repository;

import con.github.sparkmuse.flightalarm.entity.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class PriceRepositoryImpl implements PriceRepositoryCustom {

    @Autowired
    private final MongoTemplate mongoTemplate;

    @Override
    public Price updatePrice(Double price) {

        Price priceObject = mongoTemplate.findById(Price.ID, Price.class);
        Double oldPrice = priceObject == null ? 0.0d : priceObject.getNewPrice();

        Price priceUpdated = new Price(Price.ID, oldPrice, price);

        mongoTemplate.save(priceUpdated);

        return priceUpdated;
    }
}
