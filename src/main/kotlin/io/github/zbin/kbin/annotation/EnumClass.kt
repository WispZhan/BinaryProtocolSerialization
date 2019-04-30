package io.github.zbin.kbin.annotation

import kotlin.reflect.KClass

/**
 *序列化工具注解，用于标记当前类型是枚举类型
 * @param type 当前枚举类型中,所有枚举成员域所映射的基础数据类型(如int、byte 等)
 * @param length 当类型为String时,必须指定长度
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnumClass(val type: KClass<*>, val length:Int = 0)