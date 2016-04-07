package com.browscap4j.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browscap4j.domain.BrowserCapabilities;

public final class ResourceBuilder {
	private static final Logger logger = LoggerFactory.getLogger(ResourceBuilder.class);
	private final File file;
	private final ParsingService parsingService = CsvParsingService.getInstance();

	public ResourceBuilder(final File file) {
		this.file = file;
	}

	public Map<String, String> getRegexNamePatternsMap() {
		List<String[]> records = null;
		try {
			records = parsingService.getRecords(file);
		} catch (final IOException ex) {
			logger.error("", ex);
		}
		// The NamePatterns should be first sorted based on ascending order of
		// length.
		return records
			.stream()
			.collect(Collectors.toMap(record -> record[0], record -> RegexResolver.toRegex(record[0])))
			.entrySet()
			.stream()
			.sorted((Entry<String, String> entry1, Entry<String, String> entry2) -> 
						Integer.compare(entry1.getKey().length(), entry2.getKey().length()))
			.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
	}

	public Map<String, BrowserCapabilities> getNamePatternsToBrowserCapabilitiesMap() {
		List<String[]> records = null;
		try {
			records = parsingService.getRecords(file);
		} catch (final IOException ex) {
			logger.error("", ex);
		}

		return records.stream()
				.collect(Collectors.toMap(record -> record[0], // Key
					record -> {
						final String namePattern = record[0];
						final String browser = record[5];
						final String deviceName = record[38];
						final String deviceType = record[40];
						final String deviceCodeName = record[42];
						final String deviceBrandName = record[43];
						final String platform = record[13];
						final String platformMaker = record[17];
						final Boolean isMobile = BooleanUtils.toBoolean(record[32]);
						final Boolean isTablet = BooleanUtils.toBoolean(record[33]);
						
						return new BrowserCapabilities.Builder().browser(browser).deviceCodeName(deviceCodeName)
								.deviceName(deviceName).deviceBrandName(deviceBrandName).deviceType(deviceType)
								.platform(platform).platformMaker(platformMaker).isTablet(isTablet).isMobile(isMobile)
								.build();
				}));
	}
}
