package mini.springframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author daffupman
 * @since 2020/3/22
 */
@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";

    /**
     * 获取包下的类集合
     */
    public static Set<Class<?>> extractClassFromPackage(String packageName) {
        // 1、获取类加载器
        ClassLoader classLoader = getClassLoader();
        // 2、通过类加载器获取到加载的资源信息
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            log.warn("unable to retrieve anything from package: " + packageName);
            return null;
        }
        // 3、依据不同的资源类型，采用不同的方式获取资源的集合
        Set<Class<?>> classSet = null;
        // 过滤出文件类型的资源
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            // 处理协议为file的
            classSet = new HashSet<>();
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet, packageDirectory, packageName);
        }
        // TODO 对其他资源的处理
        return classSet;
    }

    /**
     * 在指定的路径下，提取出Class对象，放入Set集合中
     *
     * @param emptyClassSet 装载目标类的集合
     * @param fileSource 文件或目录
     * @param packageName 包名
     */
    private static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName) {
        if (!fileSource.isDirectory()) {
            // 如果当前文件资源不是文件夹，在停止递归
            return;
        }

        File[] files = fileSource.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                // 过滤文件
                if (file.isDirectory()) {
                    return true;
                } else {
                    // 获取文件的绝对值路径
                    String absolutePath = file.getAbsolutePath();
                    if (absolutePath.endsWith(".class")) {
                        // 若是class文件，则直接加载
                        addToClassSet(absolutePath);
                    }
                }
                return false;
            }

            // 根据class文件的绝对路径，获取并生成class对象，并放入classSet中
            private void addToClassSet(String absolutePath) {
                // 1、从class文件的绝对值路径里提取出包含package的类名
                // 使用.替换掉斜杠
                absolutePath = absolutePath.replace(File.separator, ".");
                // 截取类名（含文件拓展名）
                String className = absolutePath.substring(absolutePath.indexOf(packageName));
                // 去除文件拓展名
                className = className.substring(0, className.lastIndexOf("."));
                // 2、通过反射机制获取对应的Class对象并加入到classSet里
                Class<?> targetClass = loadClass(className);
                emptyClassSet.add(targetClass);
            }
        });

        if (files != null) {
            // 增强for必须判空
            for (File f : files) {
                extractClassFile(emptyClassSet, f, packageName);
            }
        }
    }

    public static <T> T newInstance(Class<?> clazz, Boolean accessible) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            return (T)constructor.newInstance();
        } catch (Exception e) {
            log.error("newInstance error", e);
            throw new RuntimeException(e);
        }
    }

    public static ClassLoader getClassLoader() {
        // 程序通过线程来执行，而获取到当前执行该方法的线程，便可以通过线程所属的类加载器获取到程序资源信息了
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取Class对象
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error:", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        extractClassFromPackage("io.daff.entity");
    }
}
