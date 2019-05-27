package com.dhu.common.utils;

import org.dozer.DozerBeanMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//对象拷贝工具类
public class BeanUtil {

    private static final DozerBeanMapper mapper = new DozerBeanMapper();

    public static <S, T> List<T> copyProperties(List<S> origLst, Class<T> destClz) {
        List<T> destLst = new ArrayList<>();
        if(origLst == null || origLst.isEmpty()) return destLst;

        for(S orig : origLst) {
            T dest = copyProperties(orig, destClz);
            destLst.add(dest);
        }
        return destLst;
    }

    public static <S, T> T copyProperties(S orig, T dest)  {
        if(orig == null) return null;

        mapper.map(orig, dest);
        fixCopyProperties(orig,dest);
        return dest;
    }

    //对象深拷贝
    public static <S, T> T copyProperties(S orig, Class<T> destClz) {
        if(orig == null) return null;
        T t = mapper.map(orig, destClz);
        fixCopyProperties(orig,t);
        return t;
    }

    /**
     * 处理boolean转换失败以及 list集合无法转换问题
     */
    private static void fixCopyProperties(Object source,Object dest){
        if (source == null)return;
        Class<?> classType = source.getClass();
        Field[] fields = classType.getDeclaredFields();
        for (Field field:fields){
            try {
                if (!isWrapType(field)){//处理对象、集合类型转换
                    copyListProperties(field,source,dest);
                }else {//处理boolean无法转换问题
                    copyBooleanProperties(field,source,dest);
                }
            }catch (Exception e){
                continue;
            }
        }

    }


    private static void copyListProperties(Field field,Object source,Object dest)throws Exception{
        String name = field.getName();
        String firstLetter = name.substring(0,1).toUpperCase();
        String getMethodName = "get" + firstLetter + name.substring(1);
        Method getMethods = source.getClass().getMethod(getMethodName, new Class[]{});
        Method getMethodd = dest.getClass().getMethod(getMethodName, new Class[]{});
        Object subitems = getMethods.invoke(source,new Object[]{});
        Object subitemd = getMethodd.invoke(dest,new Object[]{});
        if (field.getType().getName().equals("java.util.List") && subitems != null){
            if (subitemd == null){
                subitemd = new ArrayList<>();
                String setMethodName = "set" + firstLetter + name.substring(1);
                Method setMethod = dest.getClass().getMethod(setMethodName, new Class[]{List.class});
                setMethod.invoke(dest, new Object[]{subitemd});
            }
            fixCopyProperties(subitems,subitemd);
        }else if (subitems != null && subitemd != null){
            fixCopyProperties(subitems,subitemd);
        }
    }

    private static void copyBooleanProperties(Field field,Object source,Object dest)throws Exception{
        String name = field.getName();
        String firstLetter = name.substring(0,1).toUpperCase();
        if (field.getType().getName().equals("boolean")||field.getType().getName().equals("java.lang.Boolean")){
            String getMethodName = "is" + firstLetter + name.substring(1);
            String setMethodName = "set" + firstLetter + name.substring(1);
            //获取方法对象
            Method getMethod = source.getClass().getMethod(getMethodName, new Class[]{});
            Method setMethod = dest.getClass().getMethod(setMethodName, new Class[]{field.getType()});
            if (setMethod==null){
                if (field.getType().getName().equals("java.lang.Boolean")){
                    setMethod=dest.getClass().getMethod(setMethodName, new Class[]{boolean.class});
                }else {
                    setMethod=dest.getClass().getMethod(setMethodName, new Class[]{Boolean.class});
                }
            }
            Object value = getMethod.invoke(source, new Object[]{});
            setMethod.invoke(dest, new Object[]{value});
        }
    }



    /**
     * 是否是基础类型还是实体对象
     */
    private static boolean isWrapType(Field field) {
        String[] types = { "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long",
                "java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Char", "java.lang.String", "int",
                "double", "long", "short", "byte", "boolean", "char", "float","javax.xml.datatype.XMLGregorianCalendar","java.math.BigDecimal","java.util.Calendar" };
        List<String> typeList = Arrays.asList(types);
        return typeList.contains(field.getType().getName()) ? true : false;
    }


}
