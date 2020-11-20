package com.youtubesearch.web.model;

public class YTSearchResponse {

		String msg;

		/**
		 * @param msg
		 */
		public YTSearchResponse(String msg) {
			this.msg = msg;
		}
		
		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
}
