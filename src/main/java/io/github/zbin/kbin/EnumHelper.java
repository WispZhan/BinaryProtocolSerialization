package io.github.zbin.kbin;

/**
 * Created by WispZhan on 5/16/2016.
 * Enum reflection helper tool class
 */
public class EnumHelper {

    public static Enum GetEnumInstance(String EnumFullName,String valueName) {
        try {
            Class aClass = Class.forName(EnumFullName);
            return Enum.valueOf(aClass, valueName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static  Enum<?> GetEnumInstance(String enumFullName) {
        // see http://stackoverflow.com/questions/4545937/
        String[] x = enumFullName.split("\\.(?=[^\\.]+$)");
        if (x.length == 2)
        {
            String enumClassName = x[0];
            String enumName = x[1];
            try {
                Class cl = Class.forName(enumClassName);
                // #1

                return Enum.valueOf(cl, enumName);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
