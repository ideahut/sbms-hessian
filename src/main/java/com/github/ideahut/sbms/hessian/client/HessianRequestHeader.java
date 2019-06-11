package com.github.ideahut.sbms.hessian.client;

import java.util.HashMap;
import java.util.Map;

public abstract class HessianRequestHeader {
	
	private static final ThreadLocal<Map<String, String>> holder = new ThreadLocal<Map<String, String>>();
	
	public static void remove() {
		holder.remove();
	}
	
	public static void set(Map<String, String> headers) {
		if (headers == null) {
			holder.remove();
		} else {
			holder.set(headers);
		}
	}
	
	public static void set(String name, String value) {
		Map<String, String> headers = holder.get();
		if (headers == null) {
			holder.set(new HashMap<String, String>());
			headers = holder.get();
		}
		headers.put(name, value);
	}
	
	public static Map<String, String> get() {
		return holder.get();
	}

}
