package divers;

public class Couple<K, V> {
	private final K k;
	private final V v;
	
	
	public Couple(K k, V v) {
		this.k = k;
		this.v = v;
	}

	public K get1() {
		return k;
	}

	public V get2() {
		return v;
	}
	
	
}
