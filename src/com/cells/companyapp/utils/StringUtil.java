package com.cells.companyapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author leo
 * 
 */
public class StringUtil {

	/**
	 * 是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return str != null && !"".equals(str.trim());
	}

	/**
	 * 是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {

		if (email.contains("@")) {
			Pattern pattern = Pattern
					.compile("^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 两次密码输入相符性验证
	 * 
	 * 
	 * @param pwd
	 * @param pwdAgain
	 * @return
	 */
	public static boolean pwdVal(String pwd, String pwdAgain) {
		if (pwd.equals(pwdAgain))
			return true;
		else {
			return false;
		}
	}

	/**
	 * 手机号码验证
	 * 
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("[0-9]{11}");
		Matcher m = p.matcher(mobiles);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验银行卡卡号
	 * 
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId
				.substring(0, cardId.length() - 1));
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("//d+")) {
			throw new IllegalArgumentException("Bank card code must be number!");
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

}
