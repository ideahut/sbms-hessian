package com.github.ideahut.sbms.hessian.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianMetaInfoAPI;
import com.caucho.hessian.client.HessianRuntimeException;
import com.caucho.hessian.io.HessianRemoteObject;

public class HessianProxyFactory extends com.caucho.hessian.client.HessianProxyFactory {
	
	private final ClassLoader _loader;
	
	public HessianProxyFactory() {
		this(Thread.currentThread().getContextClassLoader());
	}

	public HessianProxyFactory(ClassLoader loader) {
		super(loader);
		_loader = loader;
	}

	@Override
	public Object create(String url) throws MalformedURLException, ClassNotFoundException {
		HessianMetaInfoAPI metaInfo = (HessianMetaInfoAPI) create(HessianMetaInfoAPI.class, url);
	    String apiClassName = (String) metaInfo._hessian_getAttribute("java.api.class");
	    if (apiClassName == null) {
	    	throw new HessianRuntimeException(url + " has an unknown api.");
	    }
	    Class<?> apiClass = Class.forName(apiClassName, false, _loader);
	    return create(apiClass, url);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object create(Class api, String urlName) throws MalformedURLException {
		return create(api, urlName, _loader);
	}

	@Override
	public Object create(Class<?> api, String urlName, ClassLoader loader) throws MalformedURLException {
		URL url = new URL(urlName);	    
	    return create(api, url, loader);
	}

	@Override
	public Object create(Class<?> api, URL url, ClassLoader loader) {
		if (api == null) {
			throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
		}
		InvocationHandler handler = new HessianProxy(url, this, api);
		return Proxy.newProxyInstance(loader, new Class[] { api, HessianRemoteObject.class }, handler);
	}	
	
	
	
	private class HessianProxy extends com.caucho.hessian.client.HessianProxy {		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7141419695713941106L;

		public HessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
			super(url, factory, type);
		}

		public HessianProxy(URL url, HessianProxyFactory factory) {
			super(url, factory);
		}
		
		@Override
		protected void addRequestHeaders(HessianConnection conn) {
			Map<String, String> headers = HessianRequestHeader.get();
			if (headers != null) {
				for (String key : headers.keySet()) {
					conn.addHeader(key, headers.get(key));
				}
			}
			super.addRequestHeaders(conn);
			HessianRequestHeader.remove();
		}
	}	
}
