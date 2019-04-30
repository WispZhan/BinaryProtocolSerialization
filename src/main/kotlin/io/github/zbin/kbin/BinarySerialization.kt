package io.github.zbin.kbin

import io.github.zbin.kbin.annotation.ByteField
import io.github.zbin.kbin.annotation.FieldType
import bin.tools.kbin.element.*
import io.github.zbin.kbin.element.CollectionElement
import io.github.zbin.kbin.element.ComplexElement
import io.github.zbin.kbin.element.DateTimeElement
import io.github.zbin.kbin.element.EnumElement
import io.github.zbin.kbin.element.IElement
import io.github.zbin.kbin.element.ValueElement
import io.github.zbin.kbin.utils.ReflectionUtils
import java.util.*

/**
 * Created by WispZhan on 6/14/2016.
 * 二进制序列化
 */
class BinarySerialization {

    /**
     * 1.当前成员域起始索引
     * 2.循环完成后即当前对象最大有效长度
     */
    private var index: Int = 0

    /**
     *将 目标对象 转换为 字节数组，用于二进制协议传输使用
     * 转换规则定于工具注解中
     */
    fun toByteArray(obj: Any): ByteArray {
        //获取当前对象所有成员域，过滤不满足序列化条件的成员域，并依据顺序排序
        val fields =
                ReflectionUtils.getSuperClassFields(obj).filter { it.getAnnotation(ByteField::class.java) != null }.sortedBy {
                    it.getAnnotation(ByteField::class.java).order
                }
        //对所有满足条件的成员域执行对应操作，获取成员bytes
        val bytes: ArrayList<Byte> = arrayListOf()

        for (field in fields) {
            //保存成员域 的可访问状态，在操作结束后还原
            val oldIsAccessible = field.isAccessible
            if (!field.isAccessible) {
                field.isAccessible = true
            }
            val fieldAnnotation = field.getAnnotation(ByteField::class.java)
            var element: IElement
            when (fieldAnnotation.type) {
                FieldType.VALUE -> element = ValueElement(field, obj)
                FieldType.COLLECTION -> element = CollectionElement(field, obj)
                FieldType.COMPLEX -> element = ComplexElement(field, obj)
                FieldType.DATETIME -> element = DateTimeElement(field, obj)
                FieldType.ENUM -> element = EnumElement(field, obj)
                else -> {
                    throw Exception("not support type!!!")
                }
            }
            bytes.addAll(element.toBytes().toList())
            field.isAccessible = oldIsAccessible
        }
        return bytes.toByteArray()
    }


    /**
     * 从ByteArray总获取指定类型对象
     */
    fun <T> fromByteArray(bytes: ByteArray, aClass: Class<T>): T {
        //过滤成员域
        val fields = ReflectionUtils.getSuperClassFields(aClass).filter { it.getAnnotation(ByteField::class.java) != null }.sortedBy { it.getAnnotation(ByteField::class.java).order }
        //创建当前制定类型的对象
        val obj = aClass.newInstance()
        //遍历成员域，分别判断每个成员域类型，并赋值给对象中对应的成员
        for (field in fields) {
            val oldIsAccessible = field.isAccessible
            if (!oldIsAccessible) {
                field.isAccessible = true
            }
            val fieldAnnotation = field.getAnnotation(ByteField::class.java)
            var element: IElement
            //判断字段类型
            when (fieldAnnotation.type) {
                FieldType.VALUE -> element = ValueElement(field)
                FieldType.COLLECTION -> element = CollectionElement(field, obj!!)
                FieldType.COMPLEX -> element = ComplexElement(field)
                FieldType.DATETIME -> element = DateTimeElement(field)
                FieldType.ENUM -> element = EnumElement(field)
            }
            val value = element.formByteArray(bytes.copyOfRange(index, bytes.size))
            index += element.getLength()
            field.set(obj, value)
            field.isAccessible = oldIsAccessible
        }
        return obj
    }

    /**
     * 获取有效长度
     */
    fun getLength(): Int {
        return index
    }
}

