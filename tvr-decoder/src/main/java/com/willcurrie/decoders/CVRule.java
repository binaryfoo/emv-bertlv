package com.willcurrie.decoders;

public class CVRule {
	private boolean failIfUnsucessful;
	private CVRule.VerificationMethod verificationMethod;
	private CVRule.ConditionCode conditionCode;
	
	public CVRule(String hexString) {
		int leftByte = Integer.parseInt(hexString.substring(0, 2), 16);
		failIfUnsucessful = (leftByte & 0x40) == 0;
		verificationMethod = VerificationMethod.fromCode(leftByte & 0x3F); 
		int rightByte = Integer.parseInt(hexString.substring(2, 4), 16);
		conditionCode = ConditionCode.fromCode(rightByte);
	}

	public String getDescription(int x, int y) {
		String baseRule = verificationMethod.getDescription() + ", " + conditionCode.getDescription() + ", " + (failIfUnsucessful ? "FAIL" : "next");
		if (conditionCode == ConditionCode.TxLessThanX || conditionCode == ConditionCode.TxMoreThanX) {
			baseRule += " (x = " + x + ")";
		} else if (conditionCode == ConditionCode.TxLessThanY || conditionCode == ConditionCode.TxMoreThanY) {
			baseRule += " (y = " + y + ")";
		}
		return baseRule;
	}
	
	public String getVerificationMethodDescription() {
		return verificationMethod == null ? "Unknown" : verificationMethod.getDescription();
	}
	
	public String getConditionCodeDescription() {
		return conditionCode == null ? "Unknown" : conditionCode.getDescription();
	}
	
	public enum VerificationMethod {
		Fail(0x00, "Fail"),
		PlainPinByIcc(0x01, "Plain PIN by ICC"),
		EncryptedPinOnline(0x02, "Encrytped PIN online"),
		PlainPinByIccPlusSignature(0x03, "Plain PIN by ICC + signature"),
		EncryptedPinByIcc(0x04, "Encrytped PIN by ICC"),
		EncryptedPinByIccPlusSignature(0x05, "Encrytped PIN by ICC + signature"),
		Signature(0x1E, "Signature"),
		NoCvmRequired(0x1F, "No CVM required");
		
		private final int code;
		private final String description;
		
		private VerificationMethod(int code, String description) {
			this.code = code;
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
		
		public static CVRule.VerificationMethod fromCode(int code) {
			for (CVRule.VerificationMethod verificationMethod : VerificationMethod.values()) {
				if (verificationMethod.code == code) {
					return verificationMethod;
				}
			}
			return null;
		}
	}
	
	public enum ConditionCode {
		Always(0x00, "Always"),
		UnattendedCash(0x01, "If unattended cash"),
		NotStuff(0x02, "If not (unattended cash, manual cash, purchase + cash)"),
		TerminalSupports(0x03, "If terminal supports CVM"),
		ManualCash(0x04, "If manual cash"),
		PurchasePlusCash(0x05, "If purchase + cash"),
		TxLessThanX(0x06, "If transaction in application currency and < X"),
		TxMoreThanX(0x07, "If transaction in application currency and >= X"),
		TxLessThanY(0x08, "If transaction in application currency and < Y"),
		TxMoreThanY(0x09, "If transaction in application currency and >= Y");
		
		private final int code;
		private final String description;
		
		private ConditionCode(int code, String description) {
			this.code = code;
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
		
		public static CVRule.ConditionCode fromCode(int code) {
			for (CVRule.ConditionCode conditionCode : ConditionCode.values()) {
				if (conditionCode.code == code) {
					return conditionCode;
				}
			}
			return null;
		}
	}
	
}