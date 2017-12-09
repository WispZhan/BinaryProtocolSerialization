package bin.tools.kbinconverter.element

import bin.tools.kbinconverter.annotation.ByteField
import java.lang.reflect.Field
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by WispZhan on 6/15/2016.
 * 值类型元素
 */
class ValueElement : IElement {
    override fun getLength(): Int {
        return length
    }

    private var length:Int=0

    override fun formByteArray(bytes: ByteArray): Any {
        when (field.type) {
            Int::class.java -> {
                length = Integer.BYTES
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return byteBuffer.int
            }
            Short::class.java -> {
                length = java.lang.Short.BYTES
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return byteBuffer.short
            }
            Long::class.java -> {
                length = java.lang.Long.BYTES
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return byteBuffer.long
            }
            Float::class.java -> {
                length = java.lang.Float.BYTES
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return byteBuffer.float
            }
            Double::class.java -> {
                length = java.lang.Double.BYTES
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return byteBuffer.double
            }
            String::class.java -> {
                length = field.getAnnotation(ByteField::class.java).length
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return String(byteBuffer.array())
            }
            Byte::class.java -> {
                length = java.lang.Byte.BYTES
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return byteBuffer.get()
            }
            Char::class.java -> {
                length = Character.BYTES
                val byteBuffer = ByteBuffer.wrap(bytes.copyOfRange(0, length)).order(ByteOrder.LITTLE_ENDIAN)
                return byteBuffer.char
            }
            else -> {
                throw Exception("")
            }
        }
    }

    private var field: Field
    private var obj: Any? = null

    constructor(field: Field) {
        this.field = field
    }

    constructor(field: Field, obj: Any) : this(field) {
        this.obj = obj
    }

    override fun toBytes(): ByteArray {
        var byteBuffer: ByteBuffer? = null
        when (field.type) {
            Int::class.java -> {
                byteBuffer = ByteBuffer.allocate(Integer.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.putInt(field.getInt(obj))
            }
            Short::class.java -> {
                byteBuffer = ByteBuffer.allocate(java.lang.Short.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.putShort(field.getShort(obj))
            }
            Long::class.java -> {
                byteBuffer = ByteBuffer.allocate(java.lang.Long.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.putLong(field.getLong(obj))
            }
            Float::class.java -> {
                byteBuffer = ByteBuffer.allocate(java.lang.Float.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.putFloat(field.getFloat(obj))
            }
            Double::class.java -> {
                byteBuffer = ByteBuffer.allocate(java.lang.Double.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.putDouble(field.getDouble(obj))
            }
            String::class.java -> {
                val fieldAnnotation = field.getAnnotation(ByteField::class.java)
                byteBuffer = ByteBuffer.allocate(fieldAnnotation.length)
                byteBuffer.put((field.get(obj) as String).toByteArray())
            }
            Byte::class.java -> {
                byteBuffer = ByteBuffer.allocate(java.lang.Byte.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.put(field.getByte(obj))
            }
            Char::class.java -> {
                byteBuffer = ByteBuffer.allocate(Character.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.putChar(field.getChar(obj))
            }
            else -> {
                throw Exception("123")
            }
        }
        var bytes = byteBuffer.array()
        length = bytes.size
        return bytes
    }
}