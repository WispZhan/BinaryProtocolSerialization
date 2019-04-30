package io.github.zbin.kbin.annotation

/**
 *集合类型序列化注解
 * @param counterName 当前集合类型的计数器字段名称，用于反序列化时提供定位支持
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CollectionField(val counterName: String)