package com.demo.emoji;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 
 */
public class EmojiAnalyse {
	public static void main(String[] args) {
		readEmojis();
	}
	
	public static void readEmojis() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(EmojiAnalyse.class.getResource("full_emoji_list.txt").openStream()));
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(parseEmoji(line));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static String parseEmoji(String text) {
		String[] arr = text.replace("U+", "").split(" ");
		char[] src = new char[0];
		
		for (String item : arr) {
			int unicode = Integer.parseInt(item, 16);
			char[] chars = Character.toChars(unicode);
			char[] dest = new char[src.length + chars.length];
			System.arraycopy(src, 0, dest, 0, src.length);
			
			for (int i = 0; i < chars.length; i++) {
				dest[dest.length - chars.length + i] = chars[i];
			}
			
			src = dest;
		}
		
		return new String(src);
	}
}
