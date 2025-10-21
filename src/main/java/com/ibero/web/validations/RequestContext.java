package com.ibero.web.validations;

public class RequestContext {
	
	private static ThreadLocal<Integer> idActual = new ThreadLocal<>();
	
	public static void setIdActual(Integer id) {
		idActual.set(id);
	}
	
	public static Integer getIdActual() {
		return idActual.get();
	}
	
	public static void clear() {
		idActual.remove();
	}

}
