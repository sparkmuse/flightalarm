package con.github.sparkmuse.flightalarm.repository;

import con.github.sparkmuse.flightalarm.entity.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends MongoRepository<Price, String>, PriceRepositoryCustom {
}
