package com.pc.skh.utils.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

public class IpTool {
	private static final String X_FORWARDER_FOR = "x-forwarder-for";

	private String cityName = "";

	private String provinceName = "";

	private String other = "";

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public static String getClientIp(final HttpServletRequest request)
			throws Exception {
		String ip = null;
		final String remoteIP = request.getRemoteAddr();
		final String originIP = request.getHeader(IpTool.X_FORWARDER_FOR);
		if (originIP == null || originIP.length() == 0) {
			ip = StringTool.trim(remoteIP);
		} else {
			ip = StringTool.trim(originIP);
		}
		return ip;
	}

	/**
	 * 通过网址抓取网页
	 * 
	 * @param urlPath
	 * @return
	 * @throws IOException
	 */
	private static StringBuffer createHtmlFile(final String urlPath)
			throws IOException {

		System.out.println(urlPath);
		StringBuffer tempHtml = null;
		URL url = null;
		BufferedReader in = null;
		String inputLine = null;
		URLConnection urlConnection = null;
		try {

			url = new URL(urlPath);
			tempHtml = new StringBuffer();

			urlConnection = url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((inputLine = in.readLine()) != null) {
				tempHtml.append(inputLine + "<br />");
				inputLine = null;
			}
			// System.out.println(tempHtml);
		}

		catch (IOException e) {
			throw e;
		} finally {
			url = null;
			in = null;
			inputLine = null;
			urlConnection = null;
		}
		return tempHtml;
	}

	public void ipToName(final String ip) throws IOException {
		// final String url =
		// "http://www.tongcha.com/index-ip1.php?ip=218.10.58.98";
		final String url = "http://www.tongcha.com/index-ip1.php?ip=" + ip;
		final String star = "<div id=\"IpAddress\">";
		final String end = "</div>";
		StringBuffer html = null;
		try {
			html = createHtmlFile(url);
			// System.out.println(html);
			if (null == html) {
				return;
			}
			final String reHtml = html.toString();
			final int a = reHtml.indexOf(star);
			final int b = reHtml.substring(a, reHtml.length()).indexOf(end);
			final String temp = reHtml.substring(a + star.length(), a + b);
			final int c = temp.indexOf("：");
			int d = temp.indexOf("省");
			int e = temp.indexOf("市");

			if (d == -1) {
				d = temp.indexOf("区");
				if (d == -1) {
					this.setOther(temp.substring(c + 1));
				} else {
					this.setProvinceName(temp.substring(c + 1, d + 1));
				}
			} else {
				this.setProvinceName(temp.substring(c + 1, d + 1));
			}
			if (e == -1) {
				e = temp.indexOf("州");
				if (e == -1) {
					e = temp.indexOf("盟");
					if (e == -1) {
						e = temp.indexOf("区");
						if (e == -1) {
							this.setOther(temp.substring(c + 1));
						}
					}
				}
			}
			if (e != -1) {
				if (e < d) {
					this.setCityName(temp.substring(c + 1, e));
				} else {
					// this.setCityName(temp.substring(d + 1, e ));]
					this.setCityName(temp.substring(e - 2, e));
				}
			}

			// this.setProvinceName(temp.substring(c + 1, d + 1));

		} catch (IOException e1) {
			throw e1;
		}
	}
}
