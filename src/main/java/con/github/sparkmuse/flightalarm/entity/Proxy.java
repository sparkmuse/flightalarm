package con.github.sparkmuse.flightalarm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Proxy {
    private String host;
    private String port;
    private String anonymity;
    private Integer speed;

    public String getAddress() {
        return this.host + ":" + this.port;
    }
}
