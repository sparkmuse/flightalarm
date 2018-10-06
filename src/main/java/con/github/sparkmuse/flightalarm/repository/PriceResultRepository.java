package con.github.sparkmuse.flightalarm.repository;

import con.github.sparkmuse.flightalarm.entity.PriceResult;
import con.github.sparkmuse.flightalarm.entity.ProxyResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceResultRepository extends ElasticsearchCrudRepository<PriceResult, String> {
}
