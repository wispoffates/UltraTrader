package com.thedemgel.ultratrader.util;

import java.io.File;
import java.io.FilenameFilter;

public class YamlFilenameFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(".yml");
	}
}
