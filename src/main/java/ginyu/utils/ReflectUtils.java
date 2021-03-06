package ginyu.utils;

import ginyu.cmd.RedisCommand;
import lombok.NonNull;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("all")
public abstract class ReflectUtils {

    public static <T> Map<String, T> getMapFromPackage(String basePackageName,
                                                       Predicate<Class> classMatcher,
                                                       Function<Class, String> keyFunc) throws IOException {
        return (Map<String, T>) getMapFromPackage(basePackageName,
                new HashMap<String, T>(), classMatcher, keyFunc);
    }

    public static <T> Map<String, T> getMapFromPackage(String basePackageName,
                                                       Map<String, T> commandMap,
                                                       Predicate<Class> classMatcher,
                                                       Function<Class, String> keyFunc) throws IOException {
        // replace base package name to file path
        String packageDirName = basePackageName.replace('.', '/');
        Enumeration<URL> dirs = RedisCommand.class.getClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                // jar
                findAndAddClassesInPackageByJar(url, basePackageName, commandMap, classMatcher, keyFunc);
            } else {
                // file
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                findAndAddClassesInPackageByFile(basePackageName, filePath, commandMap, classMatcher, keyFunc);
            }
        }
        return commandMap;
    }

    private static <T> void findAndAddClassesInPackageByJar(URL url, String basePackageName,
                                                            Map<String, T> commandMap,
                                                            Predicate<Class> classMatcher,
                                                            Function<Class, String> keyFunc) throws IOException {
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        if (connection == null) {
            throw new RuntimeException("Can't get JarURL connection : " + url.toString());
        }
        JarFile jarFile = connection.getJarFile();
        if (jarFile == null) {
            throw new RuntimeException("jarFile is null!");
        }
        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
        while (jarEntryEnumeration.hasMoreElements()) {
            JarEntry entry = jarEntryEnumeration.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            String clazzName = entry.getName().replace("/", ".");
            if (!clazzName.startsWith(basePackageName)) {
                continue;
            }
            addToList(clazzName, commandMap, classMatcher, keyFunc);
        }
    }

    private static <T> void findAndAddClassesInPackageByFile(String basePackageName, String packagePath,
                                                             Map<String, T> commandMap,
                                                             Predicate<Class> classMatcher,
                                                             Function<Class, String> keyFunc) {
        File[] dirfiles = null;
        File dir = new File(packagePath);
        if (!dir.exists()) {
            return;
        }
        if (!dir.isDirectory()) {
            return;
        }
        dirfiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || (file.getName().endsWith(".class") && !file.getName().endsWith("Test.class"));
            }
        });
        if (dirfiles == null || dirfiles.length == 0) {
            return;
        }
        // loop all files
        for (File file : dirfiles) {
            // if is directory do recursion
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(basePackageName + "." + file.getName(), file.getAbsolutePath(),
                        commandMap, classMatcher, keyFunc);
            } else {
                // get the class name
                addToList(basePackageName + '.' + file.getName(), commandMap, classMatcher, keyFunc);
            }
        }
    }


    private static <T> void addToList(String classNameWithClassSuffix,
                                      Map<String, T> commandMap,
                                      Predicate<Class> classMatcher,
                                      Function<Class, String> keyFunc) {
        String className = classNameWithClassSuffix.substring(0, classNameWithClassSuffix.length() - 6);
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            if (classMatcher.test(clazz)) {
                commandMap.put(keyFunc.apply(clazz), (T) clazz.newInstance());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过反射获取目标字段值
     *
     * @param fieldName 字段名
     * @param o         对象
     * @param <T>       返回值泛型
     * @return
     */
    public static <T> T getFieldValue(@NonNull String fieldName, @NonNull Object o) {
        try {
            Class clazz = o.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 通过反射设置目标字段值
     *
     * @param fieldName 字段名
     * @param o         对象
     * @return
     */
    public static void setFieldValue(@NonNull String fieldName, @NonNull Object o, Object value) {
        try {
            Class clazz = o.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(o, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }

    public static Class getFirstGenericTypeOnInterface(@NonNull Object o) {
        return getGenericTypeOnSuperClassOrInterface(o.getClass(), 0, false);
    }

    public static Class getFirstGenericTypeOnInterface(Class clazz) {
        return getGenericTypeOnSuperClassOrInterface(clazz, 0, false);
    }

    public static Class getGenericTypeOnInterface(@NonNull Object o, int index) {
        return getGenericTypeOnSuperClassOrInterface(o.getClass(), index, false);
    }

    public static Class getFirstGenericTypeOnSuperClass(@NonNull Object o) {
        return getGenericTypeOnSuperClassOrInterface(o.getClass(), 0, true);
    }

    public static Class getFirstGenericTypeOnSuperClass(Class clazz) {
        return getGenericTypeOnSuperClassOrInterface(clazz, 0, true);
    }

    public static Class getGenericTypeOnSuperClass(@NonNull Object o, int index) {
        return getGenericTypeOnSuperClassOrInterface(o.getClass(), index, true);
    }

    public static Class getGenericTypeOnSuperClassOrInterface(@NonNull Class clazz, int index, boolean isClass) {
        try {
            ParameterizedType type;
            if (isClass) {
                type = (ParameterizedType) clazz.getGenericSuperclass();
            } else {
                // FIXME hardcode first interface
                type = (ParameterizedType) clazz.getGenericInterfaces()[0];
            }
            Type genericType = type.getActualTypeArguments()[index];
            return Class.forName(genericType.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
