package con.github.sparkmuse.flightalarm.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Data
@Document(indexName = "priceresult")
public class PriceResult {

    @Id
    @Setter(AccessLevel.PRIVATE)
    private String id = UUID.randomUUID().toString();

    private Double price = 0.0;
    private ProxyResult proxyResult;
    private PriceResultType result;
}
