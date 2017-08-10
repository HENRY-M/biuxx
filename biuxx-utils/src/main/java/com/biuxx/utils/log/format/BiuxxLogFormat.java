package com.biuxx.utils.log.format;

public class BiuxxLogFormat extends LogFormat {

	private static final String BIUXX_HEAD = "BIUXX";

	public static final String HEAD = BIUXX_HEAD + LogFormat.INFO_DIV;

	public static enum Style {

		DEFAULT(HEAD + "DEFAULT" + INFO_DIV + LOG_ARGUMENT);

		private final String format;

		private Style(String format) {
			this.format = format;
		}

		public String getFormat() {
			return format;
		}

		public String getFormat(String format) {
			return HEAD + this.name() + INFO_DIV + format;
		}
	}
}
