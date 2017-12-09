package bin.tools.kbinconverter.element

import bin.tools.kbinconverter.exception.NotSupportDataTypeException
import java.lang.reflect.Field
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Created by WispZhan on 6/15/2016.
 *日期时间元素
 */
class DateTimeElement : IElement {

    override fun getLength(): Int {
        return length
    }

    val field: Field
    var obj: Any? = null
    private var length: Int=0

    constructor(field: Field) {
        this.field = field
    }

    constructor(field: Field, obj: Any) : this(field) {
        this.obj = obj
    }

    override fun formByteArray(bytes: ByteArray): Any {
        val byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val epochSecond = byteBuffer.int.toLong()
        val instant: java.time.Instant = java.time.Instant.ofEpochSecond(epochSecond)
        length=4
        when (field.type) {
            java.sql.Time::class.java -> {
                return java.sql.Time.from(instant)
            }
            java.sql.Date::class.java -> {
                return java.sql.Date.from(instant)
            }
            java.util.Date::class.java -> {
                return java.util.Date(epochSecond * 1000L)
            }
            java.time.Instant::class.java -> {
                return instant
            }
            java.time.LocalTime::class.java -> {
                return java.time.LocalDateTime.ofInstant(instant, ZoneId.of("UTC")).toLocalTime()
            }
            java.time.LocalDate::class.java -> {
                return java.time.LocalDateTime.ofInstant(instant, ZoneId.of("UTC")).toLocalDate()
            }
            java.time.LocalDateTime::class.java -> {
                return java.time.LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
            }
            java.util.Calendar::class.java->{
                val time=java.util.Calendar.getInstance()
                time.timeInMillis = (epochSecond * 1000L)
                return time
            }
            java.util.GregorianCalendar::class.java->{
                val time=java.util.GregorianCalendar.getInstance()
                time.timeInMillis = (epochSecond * 1000L)
                return time
            }
            String::class.java->{
                return instant.toString()
            }
            else -> {
                throw NotSupportDataTypeException("当前序列化方法不支持类型：" + field.type.name)
            }
        }
    }

    override fun toBytes(): ByteArray {
        var time: Int = 0
        when (field.type) {
            java.sql.Time::class.java -> {
                time = ((field.get(obj) as java.sql.Time).time / 1000L).toInt()
            }
            java.sql.Date::class.java -> {
                time = ((field.get(obj) as java.sql.Date).time / 1000L).toInt()
            }
            java.util.Date::class.java -> {
                time = ((field.get(obj) as java.util.Date).time / 1000L).toInt()
            }
            java.time.Instant::class.java -> {
                time = ((field.get(obj) as java.time.Instant).epochSecond).toInt()
            }
            java.time.LocalTime::class.java -> {
                val localTime = (field.get(obj) as java.time.LocalTime)
                time = java.time.LocalDateTime.of(java.time.LocalDate.now(), localTime).toEpochSecond(ZoneOffset.UTC).toInt()
            }
            java.time.LocalDate::class.java -> {
                val localDate = (field.get(obj) as java.time.LocalDate)
                time = java.time.LocalDateTime.of(localDate, java.time.LocalTime.now()).toEpochSecond(ZoneOffset.UTC).toInt()
            }
            java.time.LocalDateTime::class.java -> {
                time = ((field.get(obj) as java.time.LocalDateTime).toEpochSecond(ZoneOffset.UTC)).toInt()
            }
            java.util.Calendar::class.java -> {
                time = ((field.get(obj) as java.util.Calendar).timeInMillis / 1000L).toInt()
            }
            java.util.GregorianCalendar::class.java -> {
                time = ((field.get(obj) as java.util.GregorianCalendar).timeInMillis / 1000L).toInt()
            }
            String::class.java -> {
                time = java.time.Instant.parse(field.get(obj) as String).epochSecond.toInt()
            }
            else -> {
                throw NotSupportDataTypeException("当前序列化方法不支持类型：" + field.type.name)
            }
        }
        length=4
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(time).array()
    }

}