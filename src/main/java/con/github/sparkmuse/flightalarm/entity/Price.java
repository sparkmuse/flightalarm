package con.github.sparkmuse.flightalarm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class Price {

    public final static String ID = "id";

    @Id
    private String id;
    private Double priorPrice;
    private Double newPrice;
}
