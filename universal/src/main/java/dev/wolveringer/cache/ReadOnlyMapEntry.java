package dev.wolveringer.cache;

import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReadOnlyMapEntry<K, V> implements Entry<K, V> {
	private final K key;
	private final V value;

	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException("Value is final!");
	}
	
}
