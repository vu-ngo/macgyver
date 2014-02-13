package io.macgyver.core;

import io.macgyver.core.script.ScriptExecutor;

import java.io.File;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.TreeTraverser;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;

public class Startup implements InitializingBean {

	Logger logger = LoggerFactory.getLogger(Startup.class);

	@Autowired
	EventBus bus;

	@Autowired
	Kernel kernel;

	@Subscribe
	public void onStart(ContextRefreshedEvent event) {
		logger.info("STARTED: event");
		runInitScripts();

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		bus.register(this);
		;

	}

	public void runInitScripts() {

		File initRoot = new File(kernel.getExtensionDir(), "scripts/init");
		if (!initRoot.exists() || !initRoot.isDirectory()) {
			logger.info("init scripts dir does not exist: {}", initRoot);
			return;
		}
		TreeTraverser<File> traverser = Files.fileTreeTraverser();

		FluentIterable<File> t = traverser.preOrderTraversal(initRoot);
		Iterator<File> x = t.iterator();
		while (x.hasNext()) {
			File f = x.next();
			runInitScript(f);
		}

	}

	public void runInitScript(File f) {
		if (f.isDirectory()) {
			return;
		}
		if (f.exists() && f.getName().endsWith(".groovy")) {
			try {
				new ScriptExecutor().run(f, null, false);
			}
			catch (RuntimeException e) {
				Kernel.registerStartupError(e);
			}
		} else {
			logger.info("ignoring file in init script dir: {}", f);
		}
	}
}