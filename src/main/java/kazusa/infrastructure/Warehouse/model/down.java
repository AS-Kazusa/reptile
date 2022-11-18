package kazusa.infrastructure.Warehouse.model;

import lombok.Data;

@Data
public class down {

    private down() {}

    private static down down = new down();

    public static down getDown() {
        return down;
    }

    /**
     * 资源保存路径
     */
    private String path;

    /**
     * 资源保存类型
     */
    private String type;
}
