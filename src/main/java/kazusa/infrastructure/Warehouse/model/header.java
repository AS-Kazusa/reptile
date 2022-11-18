package kazusa.infrastructure.Warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class header {

    /**
     * 请求体设置是否开启
     */
    private boolean headerIF;

    /**
     * 请求头key
     */
    private String headerKey;

    /**
     * 请求头value
     */
    private String headerValue;
}
