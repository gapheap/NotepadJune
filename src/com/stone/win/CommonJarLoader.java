package com.stone.win;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * jar加载器
 * 
 * @author: stone
 * @date 2023-6-6 13:42:17
 */
public class CommonJarLoader extends URLClassLoader {
    private static CommonJarLoader loader = null;

    private CommonJarLoader() {
        super(new URL[0], CommonJarLoader.class.getClassLoader());
    }

    public static CommonJarLoader getInstance() {
        if (loader == null)
            loader = new CommonJarLoader();
        return loader;
    }

    public void addURL(String url) throws MalformedURLException {
        this.addURL(new URL(url));
    }

}
