package com.metazion.jgd.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.metazion.jm.util.FileUtil;

public class JgdLogger {

	private static class ResourceHolder {
		static {
			String path = FileUtil.getAbsolutePath("data/config/log4j2.xml");
			try {
				InputStream is = new FileInputStream(path);
				BufferedInputStream in = new BufferedInputStream(is);
				ConfigurationSource source = new ConfigurationSource(in);
				Configurator.initialize(null, source);
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static Logger resource = LogManager.getLogger(JgdLogger.class.getName());
	}

	public static Logger getLogger() {
		return ResourceHolder.resource;
	}
}