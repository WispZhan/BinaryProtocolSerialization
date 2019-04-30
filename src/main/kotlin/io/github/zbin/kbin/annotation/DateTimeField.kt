package io.github.zbin.kbin.annotation

/**
 * Created by WispZhan on 6/15/2016.
 * 日期时间
 * @param type 序列化类型
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DateTimeField(val type: DateTimeType = DateTimeType.UnixTime)

