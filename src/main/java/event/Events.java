package event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import common.Consoles;
import utils.ReflectUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: junjiexun
 * @date: 2020/10/19 9:11 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class Events {

    private static class EventBusThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("ginyu-event-thread-" + threadNumber.getAndIncrement());
            return t;
        }
    }

    static {
        try {
            registered();
        } catch (Exception e) {
            Consoles.error(e.getMessage());
            System.exit(1);
        }
    }

    private static void registered() throws IOException, ClassNotFoundException {
        Map<String, Object> boot = ReflectUtils.getMapFromPackage("",
                c -> {
                    for (Method method : c.getMethods()) {
                        Annotation[] annotations = method.getAnnotations();
                        return method.isAnnotationPresent(Subscribe.class);
                    }
                    return false;
                },
                c -> c.getName());
        System.out.println(boot);
    }

    private static ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 20, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20), new EventBusThreadFactory());
        return threadPoolExecutor;
    }

    private static EventBus eventBus = new AsyncEventBus("ginyu-event", threadPoolExecutor());


    public static void post(Object event) {
        eventBus.post(event);
    }
}
