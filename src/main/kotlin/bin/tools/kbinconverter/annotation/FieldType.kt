package bin.tools.kbinconverter.annotation

/**
 * Created by WispZhan on 6/14/2016.
 * 成员域类型
 */
enum class FieldType {
    /**
     * 枚举
     */
    ENUM,
    /**
     *集合类型
     */
    COLLECTION,
    /**
     *基础类型
     */
    VALUE,
    /**
     * 复杂类型
     */
    COMPLEX,
    /**
     * 日期时间类型
     */
    DATETIME,
}