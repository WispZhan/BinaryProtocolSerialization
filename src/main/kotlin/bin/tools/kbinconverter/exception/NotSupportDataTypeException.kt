package bin.tools.kbinconverter.exception

/**
 * Created by WispZhan on 7/5/2016.
 * 不支持的数据类型异常
 */
class NotSupportDataTypeException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String,cause:Throwable) : super(message, cause)
    constructor(cause:Throwable) : super(cause)
    constructor(message: String,cause:Throwable,enableSuppression:Boolean,writableStackTrace:Boolean) : super(message, cause,enableSuppression,writableStackTrace)

}