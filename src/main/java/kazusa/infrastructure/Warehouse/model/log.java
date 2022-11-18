package kazusa.infrastructure.Warehouse.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class log {

    private String date;

    private int code;

    private String uri;

    @Override
    public String toString() {
        return "log:data:" + date + ",code=" + code + ",uri=" + uri;
    }
}
