package bin.tools.kbinconverter.util

import java.lang.reflect.Field
import java.util.*

/**
 * Created by WispZhan on 7/5/2016.
 * 反射工具类
 */
class ReflectionUtils {
    companion object{
        /**
         * 获取继承树上所有类型的所有成员域
         * @param obj 目标对象
         * @return 返回目标类型中所有成员域，包含自继承类，私有成员等
         */
        fun getSuperClassFields(obj: Any): List<Field> {
            return getSuperClassFields(obj.javaClass)
        }

        /**
         * 获取继承树上所有类型的所有成员域
         * @param aClass 目标类型
         * @return 返回目标类型中所有成员域，包含自继承类，私有成员等
         */
        fun getSuperClassFields(aClass: Class<*>): List<Field> {
            val fields: ArrayList<Field> = arrayListOf()
            if (aClass.superclass != null) {
                fields.addAll(getSuperClassFields(aClass.superclass))
            }
            fields.addAll(aClass.declaredFields)
            return fields
        }
    }
}