package net.hyperic.sigar.util;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceMap extends AbstractMap {

    private static final boolean SOFTCACHE =
        ! "false".equals(System.getProperty("net.hyperic.sigar.softcache"));

    protected ReferenceQueue queue;
    protected Map map;

    public ReferenceMap() {
        this(new HashMap());
    }

    public ReferenceMap(Map map) {
        this.map = map;
        this.queue = new ReferenceQueue();
    }

    public static Map synchronizedMap() {
        Map map = Collections.synchronizedMap(new HashMap());
        return newInstance(map);
    }

    public static Map newInstance() {
        return newInstance(new HashMap());
    }

    public static Map newInstance(Map map) {
        if (SOFTCACHE) {
            return new ReferenceMap(map);
        }
        else {
            return map;
        }
    }

    public Object get(Object key) {
        Reference ref = (Reference)this.map.get(key);
        if (ref == null) {
            return null;
        }
        else {
            Object o = ref.get();
            if (o == null) {
                this.map.remove(key);
            }
            return o;
        }
    }

    public Object put(Object key, Object value) {
        poll();
        return this.map.put(key, new SoftValue(key, value, queue));
    }

    public Object remove(Object key) {
        poll();
        return this.map.remove(key);
    }

    public void clear() {
        poll();
        this.map.clear();
    }

    public int size() {
        poll();
        return this.map.size();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    protected void poll() {
        MapReference ref;

        while ((ref = (MapReference)this.queue.poll()) != null) {
            this.map.remove(ref.getKey());
        }
    }

    protected interface MapReference {
        public Object getKey();
    }

    protected static final class SoftValue
        extends SoftReference
        implements MapReference {

        private Object key;

        public Object getKey() {
            return this.key;
        }

        private SoftValue(Object key,
                          Object value,
                          ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }
    }

    protected static final class WeakValue
        extends WeakReference
        implements MapReference {

        private Object key;

        public Object getKey() {
            return this.key;
        }

        protected WeakValue(Object key,
                            Object value,
                            ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }
    }
}
