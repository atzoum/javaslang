package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collector;

public class LinkedHashMapTest extends AbstractMapTest {

    @Override
    protected String className() {
        return "LinkedHashMap";
    }

    @Override
    protected <T1, T2> LinkedHashMap<T1, T2> emptyMap() {
        return LinkedHashMap.empty();
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector() {
        return LinkedHashMap.<Integer, T> collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Map<K, V> mapOf(Tuple2<? extends K, ? extends V>... entries) {
        return LinkedHashMap.ofAll(entries);
    }

    @Override
    protected <K extends Comparable<? super K>, V> Map<K, V> mapOf(K key, V value) {
        return LinkedHashMap.of(key, value);
    }

    @Test
    public void shouldKeepOrder() {
        CharSeq actual = LinkedHashMap.<Integer, Character> empty().put(3, 'a').put(2, 'b').put(1, 'c').foldLeft(CharSeq.empty(), (s, t) -> s.append(t._2));
        assertThat(actual).isEqualTo(CharSeq.of("abc"));
    }

    // -- scan, scanLeft, scanRight

    @Test
    public void shouldScan() {
        final Map<Integer, String> map = this.<Integer, String> emptyMap()
                .put(Tuple.of(1, "a"))
                .put(Tuple.of(2, "b"))
                .put(Tuple.of(3, "c"))
                .put(Tuple.of(4, "d"));
        final Map<Integer, String> result = map.scan(Tuple.of(0, "x"), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(LinkedHashMap.empty()
                .put(0, "x")
                .put(1, "xa")
                .put(3, "xab")
                .put(6, "xabc")
                .put(10, "xabcd"));
    }

    @Test
    public void shouldScanLeft() {
        final Map<Integer, String> map = this.<Integer, String> emptyMap()
                .put(Tuple.of(1, "a"))
                .put(Tuple.of(2, "b"))
                .put(Tuple.of(3, "c"))
                .put(Tuple.of(4, "d"));
        final Seq<Tuple2<Integer, String>> result = map.scanLeft(Tuple.of(0, "x"), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(List.of(
                Tuple.of(0, "x"),
                Tuple.of(1, "xa"),
                Tuple.of(3, "xab"),
                Tuple.of(6, "xabc"),
                Tuple.of(10, "xabcd")));
    }

    @Test
    public void shouldScanRight() {
        final Map<Integer, String> map = this.<Integer, String> emptyMap()
                .put(Tuple.of(1, "a"))
                .put(Tuple.of(2, "b"))
                .put(Tuple.of(3, "c"))
                .put(Tuple.of(4, "d"));
        final Seq<Tuple2<Integer, String>> result = map.scanRight(Tuple.of(0, "x"), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(List.of(
                Tuple.of(10, "abcdx"),
                Tuple.of(9, "bcdx"),
                Tuple.of(7, "cdx"),
                Tuple.of(4, "dx"),
                Tuple.of(0, "x")));
    }

    @Test
    public void shouldWrapMap() {
        java.util.Map<Integer, Integer> source = new java.util.HashMap<>();
        source.put(1, 2);
        source.put(3, 4);
        assertThat(LinkedHashMap.ofAll(source)).isEqualTo(emptyIntInt().put(1, 2).put(3, 4));
    }
}
