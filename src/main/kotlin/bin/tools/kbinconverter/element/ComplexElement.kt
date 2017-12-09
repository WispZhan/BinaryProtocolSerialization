package bin.tools.kbinconverter.element

import bin.tools.kbinconverter.BinarySerialization
import java.lang.reflect.Field
import java.nio.ByteBuffer

/**
 * Created by WispZ on 7/3/2016.
 * 复杂类型元素
 */
class ComplexElement : IElement {
    override fun getLength(): Int {
        return length
    }

    val field: Field
    var obj: Any? = null
    private var length: Int = 0

    constructor(field: Field) {
        this.field = field
    }

    constructor(field: Field, obj: Any) : this(field) {
        this.obj = obj
    }

    override fun formByteArray(bytes: ByteArray): Any {
        val byteBuffer = ByteBuffer.wrap(bytes)
        val bs = BinarySerialization()
        val result = bs.fromByteArray(byteBuffer.array(), field.type)
        length = bs.getLength()
        return result
    }

    override fun toBytes(): ByteArray {
        val bs = BinarySerialization()
        val bytes = bs.toByteArray(field.get(obj))
        length = bytes.size
        return bytes
    }


}