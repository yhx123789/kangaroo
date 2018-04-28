package org.kangaroo.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UtilCollection {
    public UtilCollection() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static String getUUIDWithoutStrike() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getDateString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static <T> List<T> multiObjectConvertToList(T... objects) {
        if (null == objects) {
            return null;
        } else {
            List<T> result = new ArrayList();

            for(int i = 0; i < objects.length; ++i) {
                result.add(objects[i]);
            }

            return result;
        }
    }

    public static <T> List<T> singleObjectConvertToList(T object) {
        if (null == object) {
            return null;
        } else {
            List<T> result = new ArrayList();
            result.add(object);
            return result;
        }
    }

    public static <T> Set<T> singleObjectConvertToSet(T object) {
        if (null == object) {
            return null;
        } else {
            Set<T> result = new HashSet();
            result.add(object);
            return result;
        }
    }

    public static <K, V> Map<K, V> newMap(K key, V value) {
        Map<K, V> result = new HashMap();
        result.put(key, value);
        return result;
    }
}
