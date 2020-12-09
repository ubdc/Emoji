package com.demo.emoji;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmojiAnalyse {
	public static void main(String[] args) {
		readEmojis();
	}
	
	public static void readEmojis() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(EmojiAnalyse.class.getResource("full_emoji_list.txt").openStream()));
			String line;
			List<String> singleCharEmoji = new ArrayList<>();
			List<String> unkonwCharEmoji = new ArrayList<>();
			
			while ((line = bufferedReader.readLine()) != null) {
				String emoji = parseEmoji(line);
				
				if (emoji.length() >= 2) {
					for (int i = 0; i < emoji.length(); i++) {
						char c = emoji.charAt(i);
						
						if (!Character.isHighSurrogate(c) && !Character.isLowSurrogate(c)) {
							unkonwCharEmoji.add(emoji);
						}
					}					
				} else {
					singleCharEmoji.add(emoji);
				}
			}
			
			Collections.sort(singleCharEmoji);
			Collections.sort(unkonwCharEmoji);

			for (int i = 0; i < singleCharEmoji.size(); i++) {
				System.out.print(singleCharEmoji.get(i));
			}
			
			System.out.println("\n");
			
			for (String emoji : unkonwCharEmoji) {
				System.out.print(emoji);
				for (int i = 0; i < emoji.length(); i++) {
					System.out.print(" \\u" + Integer.toHexString(emoji.charAt(i)));
				}
				System.out.println();
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
