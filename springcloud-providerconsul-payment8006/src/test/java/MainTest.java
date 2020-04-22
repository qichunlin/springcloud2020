import org.apache.commons.lang.StringUtils;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/22
 */
public class MainTest {

    public static void main(String[] args) {
        String str = "HK521200079999";
        //0001
        String suffix = StringUtils.substring(str, 10);
        String prefix = StringUtils.substring(str, 0,10);


        int seqNum = Integer.parseInt(suffix);
        int newSeqNum = seqNum + 1;
        String result = prefix + String.format("%04d",newSeqNum);

        System.out.println(suffix);
        System.out.println(prefix);
        System.out.println(result);
    }
}
