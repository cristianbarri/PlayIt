package com.playit.playit.UtilsHTTP;


        import java.util.ArrayList;
        import java.util.List;
        import junit.framework.Assert;
        import org.junit.Test;

public class BCryptTest {


    @Test
    public void multiThreadHash() throws InterruptedException{
        List<Thread> threads = new ArrayList<Thread>();

        for(int i = 0; i<40; i++){
            threads.add(new Thread(){
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    while(System.currentTimeMillis()<start+12000){
                        String password = "sample";
                        String hash = BCrypt.gensalt(4);
                        String hashed = BCrypt.hashpw(password, hash);
                    }
                }

            });
        }

        for(Thread t: threads){
            t.start();
        }

        Thread.sleep(12200L);
    }

    @Test
    public static void multiThreadReproductibleHash() throws InterruptedException{

        List<Thread> threads = new ArrayList<Thread>();

        for(int i = 0; i<40; i++){
            threads.add(new Thread(){
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    while(System.currentTimeMillis()<start+12000){
                        String password = "sample";
                        String hash = "$2a$04$/YkrS2ifyAloNVUk5qAO7O";
                        String expected = "$2a$04$/YkrS2ifyAloNVUk5qAO7OlqIsp2ECTMDinOij9wvn7nXPRJCo8Gy";
                        String hashed = BCrypt.hashpw(password, hash);
                        Assert.assertEquals(expected, hashed);
                    }
                }

            });
        }

        for(Thread t: threads){
            t.start();
        }

        Thread.sleep(12200L);
    }

}
