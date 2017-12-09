package bin.tools.kbinconverter.element

import bin.tools.kbinconverter.EnumHelper
import bin.tools.kbinconverter.annotation.EnumClass
import bin.tools.kbinconverter.annotation.EnumField
import java.lang.reflect.Field
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by WispZhan on 6/15/2016.
 *枚举类型元素
 */
class EnumElement : IElement {

    override fun getLength(): Int {
        return length
    }

    val field: Field
    var obj: Any? = null
    private var length: Int = 0

    constructor(field: Field) {
        this.field = field

        //获取枚举类的注解
        byteEnum = field.type.getAnnotation(EnumClass::class.java)
        //通过枚举类型注解得到当前成员域实际的长度
        when (byteEnum.type) {
            Short::class -> {
                length = java.lang.Short.BYTES
            }
            Int::class -> {
                length = Integer.BYTES
            }
            Long::class -> {
                length = java.lang.Long.BYTES
            }
            Float::class -> {
                length = java.lang.Float.BYTES
            }
            Double::class -> {
                length = java.lang.Double.BYTES
            }
            Char::class -> {
                length = Character.BYTES
            }
            Byte::class -> {
                length = java.lang.Byte.BYTES
            }
            String::class -> {
                length = byteEnum.length
            }
        }
    }

    constructor(field: Field, obj: Any) : this(field) {
        this.obj = obj
    }

    /**
     * 枚举类型注解，包含该枚举映射的值，以及值对应长度
     */
    private var byteEnum: EnumClass

    override fun formByteArray(bytes: ByteArray): Any {
        val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
        return ConvertEnumValue(byteBuffer, field, byteEnum)
    }

    override fun toBytes(): ByteArray {
        val bytes = ConvertEnumValue(field, obj!!)
        return bytes
    }

    /**
     * 将枚举类型在注解上注册的值读取到 byteBuffer中。
     * 用于序列化过程
     * @param field 枚举类型注解
     * @param obj 枚举类型注解
     */
    private fun ConvertEnumValue(field: Field, obj: Any): ByteArray {
        val enumObj = field.get(obj)//获取当前枚举类型的有效对象
        val fieldClass = enumObj.javaClass.getDeclaredField(enumObj.toString())//找到枚举成员域
        val value = fieldClass.getAnnotation(EnumField::class.java).value//找到当前枚举对象
        val type = field.type.getAnnotation(EnumClass::class.java).type
        when (type) {
            Short::class -> {
                val s: Short?
                try {
                    s = value.toShort()
                } catch (e: NumberFormatException) {
                    s = java.lang.Short.decode(value)
                }
                return ByteBuffer.allocate(java.lang.Short.BYTES).order(ByteOrder.LITTLE_ENDIAN).putShort(s!!).array()
            }
            Int::class -> {
                val i: Int?
                try {
                    i = value.toInt()
                } catch (e: NumberFormatException) {
                    i = Integer.decode(value)
                }
                return ByteBuffer.allocate(Integer.BYTES).order(ByteOrder.LITTLE_ENDIAN).putInt(i!!).array()
            }
            Long::class -> {
                val l: Long?
                try {
                    l = value.toLong()
                } catch (e: NumberFormatException) {
                    l = java.lang.Long.decode(value)
                }
                return ByteBuffer.allocate(java.lang.Long.BYTES).order(ByteOrder.LITTLE_ENDIAN).putLong(l!!).array()
            }
            Byte::class -> {
                return ByteBuffer.allocate(java.lang.Byte.BYTES).order(ByteOrder.LITTLE_ENDIAN).put(value.toByte()).array()
            }
            Float::class -> {
                return ByteBuffer.allocate(java.lang.Float.BYTES).order(ByteOrder.LITTLE_ENDIAN).putFloat(value.toFloat()).array()
            }
            Double::class -> {
                return ByteBuffer.allocate(java.lang.Double.BYTES).order(ByteOrder.LITTLE_ENDIAN).putDouble(value.toDouble()).array()
            }
            String::class -> {
                val bytes = value.toString().toByteArray()
                return bytes
            }
            Char::class ->
                return ByteBuffer.allocate(Character.BYTES).order(ByteOrder.LITTLE_ENDIAN).putChar(value.toInt().toChar()).array()
            else -> {
                throw TypeCastException("不支持非基础类型")
            }
        }
    }

    /**
     * 将byte array 与指定枚举类型中的注解值比较，匹配则返回当前枚举的值
     * 用于反序列化过程
     * @param byteBuffer 字节缓冲对象，从对象中直接读取基本类型数据
     * @param field 当前枚举Field
     * @param annotation 当前枚举Field 的 ByteEnumField注解
     */
    private fun ConvertEnumValue(byteBuffer: ByteBuffer, field: Field, annotation: EnumClass): Enum<*> {
        val type = annotation.type

        val value: Any =
                when (type) {
                    Short::class ->
                        byteBuffer.short
                    Int::class ->
                        byteBuffer.int
                    Long::class ->
                        byteBuffer.long
                    Byte::class ->
                        byteBuffer.get()
                    Char::class ->
                        byteBuffer.char
                    String::class ->
                        String(byteBuffer.array())
                    Float::class ->
                        byteBuffer.float
                    Double::class ->
                        byteBuffer.double
                    else -> {
                        throw TypeCastException("不支持非基础类型")
                    }
                }

        val enumFields: Array<Field> = field.type.declaredFields
        for (enumField in enumFields) {
            if (!enumField.isEnumConstant) {
                continue
            }
            val EnumFieldAnnotation = enumField.getAnnotation(EnumField::class.java)
            val enumValue: Any = when (type) {
                Byte::class -> {
                    EnumFieldAnnotation.value.toByte()
                }
                Short::class -> {
                    try {
                        EnumFieldAnnotation.value.toShort()
                    } catch (e: NumberFormatException) {
                        java.lang.Short.decode(EnumFieldAnnotation.value)
                    }
                }
                Int::class -> {
                    try {
                        EnumFieldAnnotation.value.toInt()
                    } catch (e: NumberFormatException) {
                        Integer.decode(EnumFieldAnnotation.value)
                    }
                }
                Long::class -> {
                    try {
                        EnumFieldAnnotation.value.toLong()
                    } catch (e: NumberFormatException) {
                        java.lang.Long.decode(EnumFieldAnnotation.value)
                    }
                }
                Float::class -> {
                    EnumFieldAnnotation.value.toFloat()
                }
                Double::class -> {
                    EnumFieldAnnotation.value.toDouble()
                }
                String::class -> {
                    EnumFieldAnnotation.value.toString()
                }
                Boolean::class -> {
                    EnumFieldAnnotation.value.toBoolean()
                }
                Char::class -> {
                    EnumFieldAnnotation.value.toInt().toChar()
                }
                else -> {
                    throw ClassCastException("枚举注解不支持非基础类型")
                }
            }
            if (value.equals(enumValue)) {
                return EnumHelper.GetEnumInstance(field.type.name, enumField.name)
            }
        }
        throw ClassCastException("类型查找错误，无法转换当前byte数组为指定对象")
    }
}