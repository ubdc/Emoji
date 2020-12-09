package com.demo.emoji;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有emoji列表: https://unicode.org/emoji/charts/full-emoji-list.html
 * 
 * 由于没有emoji的文字下载链接，需要自己去获取。查看html源码，能找到对应的emoji都是在
 * >U+xxxx <这样的格式，我们只需要截取><之间的文本，就可以找到所有的emoji，下面是代码实现
 */
public class ListAllEmojis {
	public static void main(String[] args) {
		getFullEmojiList();
	}
	
	public static List<String> getFullEmojiList() {
		List<String> emojis = new ArrayList<>();
		long s = System.currentTimeMillis();
		try {
			URLConnection conn = new URL("https://unicode.org/emoji/charts/full-emoji-list.html").openConnection();
			conn.connect();
			try (InputStream is = conn.getInputStream()) {
				byte[] buf = new byte[1024 * 8];
				int index = 0;
				int len;
				
				while ((len = is.read(buf, index, buf.length - index)) != -1) {
					int end = index + len;
					int copyIndex = -1;
					int prefixTagIndex = -1;
					
					for (int i = 0; i < end; i++) {
						if ((i == end - 1 && buf[i] == '>') 
								|| (i == end - 2 && buf[i] == '>' && buf[i + 1] == 'U')
								|| (i == end - 3 && buf[i] == '>' && buf[i + 1] == 'U' && buf[i + 2] == '+')) {
							copyIndex = i;
							i = end - 1;
						} else {
							if (prefixTagIndex == -1) {
								if (i < end - 3 && buf[i] == '>' && buf[i + 1] == 'U' && buf[i + 2] == '+') {
									prefixTagIndex = i + 1;
									copyIndex = i;
									i = i + 3;
								}
							} else {
								if (buf[i] == '<') {
									String emoji = new String(buf, prefixTagIndex, i - prefixTagIndex, "UTF-8");
									System.out.println(emoji);
									emojis.add(emoji);
									prefixTagIndex = -1;
									copyIndex = -1;
								}
							}
						}
					}
					
					if (copyIndex != -1) {
						System.arraycopy(buf, copyIndex, buf, 0, end - copyIndex);
						index = end - copyIndex;
					} else {
						index = 0;
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done: take " + (System.currentTimeMillis() - s) + " ms, find " + emojis.size() + " emojis");
		}
		
		return emojis;
	}
}
