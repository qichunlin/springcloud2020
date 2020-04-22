import java.time.ZonedDateTime;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/22
 */
public class TestZone {
    public static void main(String[] args) {
        ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
        System.out.println(zbj);

        //2020-02-21T15:51:37.485+08:00[Asia/Shanghai]
    }
}
