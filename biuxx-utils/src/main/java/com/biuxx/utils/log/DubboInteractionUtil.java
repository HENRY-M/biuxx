package com.biuxx.utils.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class DubboInteractionUtil {

    public static final String INTERACTION_UUID = "interaction_uuid";

    private static final Logger logger = LoggerFactory.getLogger(DubboInteractionUtil.class);

    private static final String HIDE_DISPLAY_STR = "[HIDE_FIELD]";

    private static final char MARK_CHAR = '*';

    protected static Object[] preDealForLogging(Object[] arguments, Annotation[][] annotations) {
        if (arguments == null)
            return null;
        Object[] preDealtObjs = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            preDealtObjs[i] = preDealForLogging(arguments[i], annotations[i]);
        }
        return preDealtObjs;
    }

    protected static Object preDealForLogging(Object obj, Annotation[] annotations) {
        return preDealForLogging(obj, annotations, new HashMap<Object, Object>());
    }

    private static Object preDealForLogging(Object obj, Annotation[] annotations, Map<Object, Object> dealtObjSet) {

        if (obj == null)
            return null;

        boolean toDesensitize = false;
        boolean toHide = false;
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Desensitize) {
                    toDesensitize = true;
                } else if (annotation instanceof Hide) {
                    toHide = true;
                }
            }
        }

        boolean toTotallyDeal = canBeTotallyDealt(obj);
        if (toTotallyDeal) {
            if (toHide) {
                return displayToHideObj(obj);
            } else if (toDesensitize) {
                return diplayToDesensitizeObj(obj);
            } else
                return obj;
        }

        Class<?> objClass = obj.getClass();
        if (Map.class.isAssignableFrom(objClass)) {
            return preDealMapValue((Map<?, ?>) obj, dealtObjSet);
        } else if (Collection.class.isAssignableFrom(objClass)) {
            return preDealCollectionValue((Collection<?>) obj, dealtObjSet);
        } else {
            return preDealBeanObj(obj, dealtObjSet);
        }
    }

    private static Object preDealBeanObj(Object obj, Map<Object, Object> dealtObjSet) {

        Map<String, Object> retObj = new HashMap<String, Object>();
        Class<?> objClass = obj.getClass();
        do {
            for (Field field : objClass.getDeclaredFields()) {

                boolean originalAccessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(obj);
                    Object dealtFieldValue = null;
                    if(!dealtObjSet.containsKey(fieldValue)) {
                        dealtObjSet.put(fieldValue, null);
                        dealtFieldValue = preDealForLogging(fieldValue, field.getAnnotations(), dealtObjSet);
                        dealtObjSet.put(fieldValue, dealtFieldValue);
                    } else {
                        dealtFieldValue = dealtObjSet.get(fieldValue);
                    }
                    String fieldName = field.getName();
                    if(!retObj.containsKey(fieldName))
                        retObj.put(fieldName, dealtFieldValue);
                } catch (Exception e) {
                    logger.error("dealt faild[" + field.getName() + "] of class[" + objClass.getName() + "] failed", e);
                } finally {
                    field.setAccessible(originalAccessible);
                }
            }
            objClass = objClass.getSuperclass();
        } while(objClass != null);

        return retObj;
    }

    private static Collection<?> preDealCollectionValue(Collection<?> collection, Map<Object, Object> dealtObjSet) {

        List<Object> dealtCollection = new ArrayList<Object>();

        Iterator<?> elements = collection.iterator();
        while (elements.hasNext()) {
            Object elementObj = elements.next();
            dealtCollection.add(preDealForLogging(elementObj, null, dealtObjSet));

        }
        return dealtCollection;
    }

    private static Map<?, ?> preDealMapValue(Map<?, ?> map, Map<Object, Object> dealtObjSet) {

        Map<Object, Object> dealtMap = new HashMap<Object, Object>();

        for (Entry<?, ?> entry : map.entrySet()) {
            dealtMap.put(entry.getKey(), preDealForLogging(entry.getValue(), null, dealtObjSet));
        }

        return dealtMap;
    }

    private static boolean canBeTotallyDealt(Object obj) {

        if (String.class.equals(obj.getClass()))
                return true;

        if (obj.getClass().isPrimitive())
            return true;

        if (obj.getClass().isEnum())
            return true;

        if (obj.getClass().equals(Boolean.class))
            return true;

        if (obj.getClass().equals(Character.class))
            return true;

        if (Number.class.isAssignableFrom(obj.getClass()))
            return true;

        if (obj.getClass().isArray()) {
            Class<?> componentClass = obj.getClass().getComponentType();
            return
                    componentClass.isPrimitive() ||
                            componentClass.equals(String.class) ||
                            componentClass.isEnum() ||
                            componentClass.equals(Boolean.class) ||
                            componentClass.equals(Character.class) ||
                            Number.class.isAssignableFrom(componentClass);
        } else
            return false;
    }

    private static Object displayToHideObj(Object obj) {
        if (obj.getClass().isArray() && Array.getLength(obj) == 0)
            return obj;
        return HIDE_DISPLAY_STR;
    }

    private static Object diplayToDesensitizeObj(Object obj) {

        String objStr = null;
        if (obj.getClass().isArray()) {
            if (Array.getLength(obj) == 0)
                return obj;
            else
                objStr = toStrForArray(obj);
        } else {
            objStr = obj.toString();
        }

        return markStr(objStr);
    }

    private static String markStr(String objStr) {
        if ("".equals(objStr))
            return objStr;

        StringBuilder markStr = new StringBuilder(objStr);
        char[] chars = objStr.toCharArray();
        int markCharCount = chars.length / 3 + 1;
        int startMarkIndex = chars.length - chars.length * 2 / 3 - 1;
        for (int i = 0; i < markCharCount; i++) {
            markStr.setCharAt(startMarkIndex + i, MARK_CHAR);
        }

        return markStr.toString();
    }

    private static String toStrForArray(Object arrayObj) {

        StringBuilder arrayStr = new StringBuilder();
        Class<?> componentType = arrayObj.getClass().getComponentType();
        int arraySize = Array.getLength(arrayObj);
        if (componentType.equals(byte.class)) {
            for (int i = 0; i < arraySize; i++) {
                String byteStr = Integer.toHexString(0xff & Array.getByte(arrayObj, i));
                if (byteStr.length() < 2)
                    arrayStr.append('0');
                arrayStr.append(byteStr);
            }
        } else {
            for (int i = 0; i < arraySize; i++) {
                arrayStr.append(Array.get(arrayObj, i));
            }
        }

        return arrayStr.toString();
    }

    protected static InvocationAnnotations getAnnotations(Class<?> interfaceDefine, String methodName,
                                                          Class<?>[] parameterTypes) {
        Class<?> thisClass = interfaceDefine;
        while (true) {
            Method method;
            try {
                method = thisClass.getDeclaredMethod(methodName, parameterTypes);
                return new InvocationAnnotations(method.getParameterAnnotations(), method.getAnnotations());
            } catch (NoSuchMethodException e) {
                thisClass = thisClass.getSuperclass();
                if (thisClass == null) {
                    logger.error("not found method", e);
                    break;
                }
            } catch (SecurityException e) {
                logger.error("anlysis invocation method faild", e);
                break;
            }
        }
        return new InvocationAnnotations(new Annotation[parameterTypes.length][0], new Annotation[0]);
    }
}

class InvocationAnnotations {

    private Annotation[][] argumentAnnotations;

    private Annotation[] resultAnnotations;

    InvocationAnnotations(Annotation[][] argumentAnnotations, Annotation[] resultAnnotations) {
        this.argumentAnnotations = argumentAnnotations;
        this.resultAnnotations = resultAnnotations;
    }

    Annotation[][] getArgumentAnnotations() {
        return argumentAnnotations;
    }

    Annotation[] getResultAnnotations() {
        return resultAnnotations;
    }

}