package com.ibero.web.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;

	private int totalPaginas;
	private int numElementosPorPagina, paginaActual;

	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		this.url = url;
	    this.page = page;
	    this.paginas = new ArrayList<>();

		numElementosPorPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber();
		
		int desde, hasta;
		if (totalPaginas <= numElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (paginaActual < numElementosPorPagina / 2) {
	            desde = 0; // Cambia de 1 a 0
	            hasta = numElementosPorPagina;
	        } else if (paginaActual >= totalPaginas - numElementosPorPagina / 2) {
	            desde = totalPaginas - numElementosPorPagina;
	            hasta = totalPaginas;
	        } else {
	            desde = paginaActual - numElementosPorPagina / 2;
	            hasta = paginaActual + numElementosPorPagina / 2;
	        }
		}
		for (int i = desde; i < hasta; i++) {
	        paginas.add(new PageItem(i + 1, paginaActual == i)); // Se suma 1 solo para mostrarlo
	    }
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	public String getUrl() {
		return url;
	}

	public Page<T> getPage() {
		return page;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getNumElementosPorPagina() {
		return numElementosPorPagina;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}
}
