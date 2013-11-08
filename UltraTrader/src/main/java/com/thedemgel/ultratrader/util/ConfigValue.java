
package com.thedemgel.ultratrader.util;


public class ConfigValue<V> {
	private V value;
	
	public ConfigValue(V value) {
		this.value = value;
	}
	
	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
}
