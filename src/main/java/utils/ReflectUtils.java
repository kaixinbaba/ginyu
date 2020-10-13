package utils;

import cmd.Command;
import cmd.RedisCommand;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("all")
public abstract class ReflectUtils {

    public static Map<String, RedisCommand> getRedisCommandFromPackage(String basePackageName) throws IOException {
        return (Map<String, RedisCommand>) getRedisCommandFromPackage(basePackageName, new HashMap<String, RedisCommand>());
    }

    public static Map<String, RedisCommand> getRedisCommandFromPackage(String basePackageName, Map<String, RedisCommand> commandMap) throws IOException {
        // replace base package name to file path
        String packageDirName = basePackageName.replace('.', '/');
        Enumeration<URL> dirs = RedisCommand.class.getClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                // jar
                findAndAddClassesInPackageByJar(url, basePackageName, commandMap);
            } else {
                // file
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                findAndAddClassesInPackageByFile(basePackageName, filePath, commandMap);
            }
        }
        return commandMap;
    }

    private static void findAndAddClassesInPackageByJar(URL url, String basePackageName, Map<String, RedisCommand> commandMap) throws IOException {
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
            addCommandToList(clazzName, commandMap);
        }
    }

    private static void findAndAddClassesInPackageByFile(String basePackageName, String packagePath,
                                                         Map<String, RedisCommand> commandMap) {
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
                findAndAddClassesInPackageByFile(basePackageName + "." + file.getName(), file.getAbsolutePath(), commandMap);
            } else {
                // get the class name
                addCommandToList(basePackageName + '.' + file.getName(), commandMap);
            }
        }
    }

    private static boolean isRedisCommand(Class clazz) {
        return clazz.isAnnotationPresent(Command.class);
    }

    private static String getCommandName(Class clazz) {
        Command command = (Command) clazz.getAnnotation(Command.class);
        return command.name();
    }

    private static void addCommandToList(String classNameWithClassSuffix, Map<String, RedisCommand> commandMap) {
        String className = classNameWithClassSuffix.substring(0, classNameWithClassSuffix.length() - 6);
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            clazz.getInterfaces();
            if (isRedisCommand(clazz)) {
                String cmdName = getCommandName(clazz);
                commandMap.put(cmdName, (RedisCommand) clazz.newInstance());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }
    }
}
