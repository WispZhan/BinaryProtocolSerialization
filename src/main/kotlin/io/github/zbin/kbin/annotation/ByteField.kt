package io.github.zbin.kbin.annotation

/**
 *  字节序列化工具
 * @param length 序列化字节长度,若类型为集合或数组,长度则为元素长度;若内省为复杂类型，则不表明长度
 * @param order 属性序列化顺序
 * @param type 成员域类型
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ByteField(val order: Int,val length: Int=0, val type: FieldType = FieldType.VALUE)


