package no.avec;

import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by avec on 28/07/16.
 */
public class App {

    private Jedis jedis;

    public static void main(String[] args) {
        App app = new App();

        app.connect();

        try {

            app.testString();
            app.testSet();
            app.testOrderedSet();


        } finally {
            app.close();
        }
    }

    private void testString() {
        jedis.set("key", "Hello, Redis!");
        System.out.println("key = " + jedis.get("key"));
        jedis.del("key");
        System.out.println("key = " + jedis.get("key"));

    }

    private void testSet() {
        Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));

        // add set to redis
        set.stream().forEach(e -> jedis.sadd("myset", e));

        // get non ordered set from redis
        System.out.println(jedis.smembers("myset"));

        // removing set "myset"
        jedis.del("myset");

        // looking for "myset" once more
        System.out.println(jedis.smembers("myset"));

    }

    private void testOrderedSet() {
        Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));

        // list set to verify order
        System.out.println(set);

        // add set to redis
        set.stream().forEach(e -> jedis.zadd("myorderedset", 0, e));

        // get ordered set from redis (0 to n)
        System.out.println(jedis.zrange("myorderedset", 0, -1));

    }

    private void connect() {
        jedis = new Jedis("192.168.99.100", 6379);
    }

    private void close() {
        jedis.close();
    }


}
