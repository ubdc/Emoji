package com.demo.emoji;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * emoji来源参考: https://unicode.org/emoji/charts/full-emoji-list.html
 * emoji分类:
 * 1.单字符emoji，只有一个char
 * 2.⏩⏪#️⃣*️⃣0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣9️⃣
 * 3.多字符emoji，这种字符一般由两个或两个以上的字符组合而成，这种emoji一般由编码对组成，
 * 分为:
 *     高代理项代码单元、
 *     低代理项代码单元、
 *     零宽连接符(\u200d)一般用来连接变化选择器、
 *     变化选择器(https://codepoints.net/U+FE0F?lang=en)可用作emoji肤色、性别等
 * 
 */
public class EmojiUtils {
	private static final String ALL_SINGLE_CHARACTER_EMOJIS = "©®‼⏫⏬⏭⏮⏯⏰⏱⏲⏳⏸⏹⏺Ⓜ▪▫▶◀◻◼◽◾☀☁☂☃☄☎☑☔☕☘☝☠☢☣☦☪☮☯☸☹☺♀♂♈♉♊♋♌♍♎♏♐♑♒♓♟♠♣♥♦♨♻♾♿⚒⚓⚔⚕⚖⚗⚙⚛⚜⚠⚡⚧⚪⚫⚰⚱⚽⚾⛄⛅⛈⛎⛏⛑⛓⛔⛩⛪⛰⛱⛲⛳⛴⛵⛷⛸⛹⛺⛽✂✅✈✉✊✋✌✍✏✒✔✖✝✡✨✳✴❄❇❌❎❓❔❕❗❣❤➕➖➗➡➰➿⤴⤵⬅⬆⬇⬛⬜⭐⭕〰〽㊗㊙";
	private static final Set<Character> ALL_SINGLE_CHARACTER_EMOJI_SET = new HashSet<>();
	
	static {
		for (int i = 0; i < ALL_SINGLE_CHARACTER_EMOJIS.length(); i++) {
			ALL_SINGLE_CHARACTER_EMOJI_SET.add(ALL_SINGLE_CHARACTER_EMOJIS.charAt(i));
		}
	}
	
	public static List<EmojiRange> findEmojis(String s) {
		int length = s.length();
		int emojiStart = -1;
		int emojiEnd = -1;
		List<EmojiRange> ranges = new ArrayList<>();
		
		for (int i = 0; i< length; i++) {
			char c = s.charAt(i);
			
			if (Character.isHighSurrogate(c)) {
				if (emojiStart == -1) {
					emojiStart = i;
				}
			} else if (Character.isLowSurrogate(c)) {
				emojiEnd = i;
			} else {
				if (ALL_SINGLE_CHARACTER_EMOJI_SET.contains(c)) {
					// 1.是否是单字符emoji
					if (emojiStart == -1) {
						emojiStart = i;
					}
					
					emojiEnd = i;
				} else if (c >= '\u0023' && c <= '\u0039') {
					// 2.⏩⏪#️⃣*️⃣0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣9️⃣
					if (i < length - 2 && s.charAt(i + 1) == '\ufe0f' && s.charAt(i + 2) == '\u20e3') {
						if (emojiStart == -1) {
							emojiStart = i;
						}
						
						i += 2;						
						emojiEnd = i;
					} else {
						if (emojiStart != -1 && emojiEnd != -1) {
							ranges.add(new EmojiRange(s, emojiStart, emojiEnd + 1));
							emojiStart = -1;
							emojiEnd = -1;
						}
					}
				} else {
					if (c == '\u200d' || c == '\ufe0f') {
						if (emojiStart == -1) {
							emojiStart = i;
						}
						
						emojiEnd = i;
					} else {
						// 需要继续判断下一个字符
						if (i < length - 1) {
							char nextChar = s.charAt(i + 1);
							
							if (nextChar == '\u200d' || nextChar == '\ufe0f') {
								if (emojiStart == -1) {
									emojiStart = i;
								}
								
								i++;
								emojiEnd = i;
							} else {
								if (emojiStart != -1 && emojiEnd != -1) {
									ranges.add(new EmojiRange(s, emojiStart, emojiEnd + 1));
									emojiStart = -1;
									emojiEnd = -1;
								}
							}
						}
					}
				}
			}
		}
		
		if (emojiStart != -1 && emojiEnd != -1) {
			ranges.add(new EmojiRange(s, emojiStart, emojiEnd + 1));
			emojiStart = -1;
			emojiEnd = -1;
		}
		
		return ranges;
	}
	
	public static class EmojiRange {
		public final String text;
		public final int start;
		public final int end;
		
		public EmojiRange(String text, int start, int end) {
			this.text = text;
			this.start = start;
			this.end = end;
		}
		
		@Override
		public String toString() {
			return "[" + start + ", " + end + "]: " + text.substring(start, end);
		}
	}
	
	public static void main(String[] args) {
		List<EmojiRange> ranges = findEmojis("☺");
		for (EmojiRange range : ranges) {
			System.out.println(range);
		}
	}
}
