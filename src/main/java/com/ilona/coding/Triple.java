package com.ilona.coding;

public class Triple<K, V, L> {
    K k;
    V v;
    L l;

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public L getL() {
        return l;
    }

    public void setL(L l) {
        this.l = l;
    }

    public Triple(K k, V v, L l) {

        this.k = k;
        this.v = v;
        this.l = l;
    }
}
