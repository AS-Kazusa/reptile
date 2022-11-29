package kazusa.service.field.utils;

public class utils {

    /**
     * @return 随机阻塞时间
     */
    public static long ramo() {
        return ((int) (Math.random() * 9)) * 1000L;
    }
}
