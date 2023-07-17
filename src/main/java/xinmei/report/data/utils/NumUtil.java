package xinmei.report.data.utils;

import java.util.List;

public class NumUtil {
    // 将能够转换为数值的字符串值转换为数值类型
    public static void coverNumeric(List<List<Object>> list) {
        for (List<Object> innerList : list) {
            for (int i = 0; i < innerList.size(); i++) {
                Object obj = innerList.get(i);
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (isNumeric(str)) {
                        innerList.set(i, Double.parseDouble(str));
                    }
                }
            }
        }
    }

    // 判断字符串是否为数字
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
