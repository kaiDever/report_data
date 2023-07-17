package xinmei.report.data.enums;

public enum ValueInputOption {
    //表示输入的值是原始文本，不会被解析或格式化
    RAW,
    //表示输入的值会被解析和格式化，例如公式和日期格式
    USER_ENTERED,
    //表示输入的值选项未指定，将使用默认选项
    INPUT_VALUE_OPTION_UNSPECIFIED
}