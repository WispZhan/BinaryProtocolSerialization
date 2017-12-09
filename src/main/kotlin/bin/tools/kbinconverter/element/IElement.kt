package bin.tools.kbinconverter.element

/**
 * Created by WispZ on 7/3/2016.
 * 序列化元素接口
 */
interface IElement {
    fun toBytes(): ByteArray

    fun formByteArray(bytes: ByteArray): Any

    fun getLength():Int
}