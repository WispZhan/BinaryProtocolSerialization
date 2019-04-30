package io.github.zbin.kbin.annotation

/**
 * byte序列化工具，枚举类型注册注解
 * 为枚举类型提供额外的值访问方法
 * @param value 值访问方法，默认为空
 * @param order 枚举成员顺序，默认为0
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnumField(val value: String = "", val order: Int)