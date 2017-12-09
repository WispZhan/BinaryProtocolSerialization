package bin.tools.kbinconverter.element

import bin.tools.kbinconverter.BinarySerialization
import bin.tools.kbinconverter.annotation.CollectionField
import bin.tools.kbinconverter.util.ReflectionUtils
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

/*
 * Created by WispZhan on 6/15/2016.
 * 集合类型元素
 */
class CollectionElement : IElement {
    override fun getLength(): Int {
        return length
    }

    var field: Field
    var obj: Any? = null
    private var length: Int = 0

    constructor(field: Field) {
        this.field = field
    }

    constructor(field: Field, obj: Any) : this(field) {
        this.obj = obj
    }

    override fun formByteArray(bytes: ByteArray): Any {
        // 截断bytes，获取正确的
        val annoColl = field.getAnnotation(CollectionField::class.java)

        val counterField = ReflectionUtils.getSuperClassFields(obj!!).find { it.name == annoColl.counterName }!!
        counterField.isAccessible = true
        val count: Int = counterField.getInt(obj)

        val genericType = (field.genericType as ParameterizedType).actualTypeArguments[0] as Class<*> //获取泛型集合中泛型类型

        if (Collection::class.java.isAssignableFrom(field.type)) {
            //集合类型
            val set = field.type.newInstance()
            val addMethod = set.javaClass.getMethod("add", Any::class.java)
            for (i in 1..count) {
                val bs = BinarySerialization()
                val genericTypeObj = bs.fromByteArray(bytes.copyOfRange(length, bytes.size), genericType)
                addMethod.invoke(set, genericTypeObj)
                length += bs.getLength()
            }
            return set
        } else {
            //数组类型
            val set = Array.newInstance(field.type, count)
            for (i in 1..count) {
                val bs = BinarySerialization()
                Array.set(set, i, bs.fromByteArray(bytes.copyOfRange(length, bytes.size), genericType))
                length += bs.getLength()
            }
            return set
        }
    }

    override fun toBytes(): ByteArray {
        val bs = BinarySerialization()
        val bytes = arrayListOf<Byte>()
        if (Collection::class.java.isAssignableFrom(field.type)) {
            for (it in (field.get(obj) as Collection<*>)) {
                if (it == null) {
                    continue
                }
                bytes.addAll(bs.toByteArray(it).toList())
            }
        } else {
            val length = Array.getLength(field.get(obj))
            for (i in 0..length) {
                bytes.addAll(bs.toByteArray(Array.get(field.get(obj), i)).toList())
            }
        }
        length = bytes.size
        return bytes.toByteArray()
    }
}
