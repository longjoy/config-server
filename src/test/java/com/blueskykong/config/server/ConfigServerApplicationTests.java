package com.blueskykong.config.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.Cleaner;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ConfigServerApplicationTests {

    @Test
    public void testBits() throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        System.out.println("maxMemoryValue:" + sun.misc.VM.maxDirectMemory());
        ByteBuffer buffer = ByteBuffer.allocateDirect(0);
        Class c = Class.forName("java.nio.Bits");
        Field maxMemory = c.getDeclaredField("maxMemory");
        maxMemory.setAccessible(true);
        synchronized (c) {
            Long maxMemoryValue = (Long) maxMemory.get(null);
            System.out.println("maxMemoryValue:" + maxMemoryValue);
        }
    }

    @Test
    public void testAllocateDirector() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        buffer.get(0);
        Field cleanerField = buffer.getClass().getDeclaredField("cleaner");
        cleanerField.setAccessible(true);
        Cleaner cleaner = (Cleaner) cleanerField.get(buffer);
        cleaner.clean();
    }

    @Test
    public void testSoft() {
        SoftReference<String> sr = new SoftReference<>("hello");
        System.out.println(sr.get());
    }

    @Test
    public void testWeak() throws InterruptedException {
        WeakReference<String> wr = new WeakReference<>("hello");

        System.out.println(wr.get());
        System.gc();                //通知JVM的gc进行垃圾回收
        Thread.sleep(1000);
        System.out.println(wr.get());
    }


    public static void main(String[] args) {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        PhantomReference<String> pr = new PhantomReference<>("hello", queue);
        System.out.println(pr.get());
    }
}
