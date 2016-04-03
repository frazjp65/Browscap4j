package com.browscap4j.service;

import org.apache.commons.lang3.CharUtils;

public final class RegexResolver {

	protected static String toRegex(String namePattern) {
		final StringBuilder patternBuilder = new StringBuilder();
		for (final char c : namePattern.toCharArray()) {
			switch (c) {
				case '*':
					patternBuilder.append(".*?");
					break;
				case '?':
					patternBuilder.append(".");
					break;
				default:
					if(CharUtils.isAsciiAlphanumeric(c) || c==' '){
						//The char c is either an alphabet,or a number or a whitespace,and NOT a regex wildcard.
						patternBuilder.append(c);
					}else {
						patternBuilder.append("\\").append(c);
					}
			}
		}
		final String pattern = patternBuilder.toString();
		return pattern;
	}
}