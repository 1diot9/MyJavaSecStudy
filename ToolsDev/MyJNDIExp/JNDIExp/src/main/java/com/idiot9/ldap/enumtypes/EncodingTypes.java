package com.idiot9.ldap.enumtypes;

public enum EncodingTypes {
    BASE64("base64"),
    BIN("bin"),
    BASE64URL("base64url"),
    HEX("hex");

    public static final String SUPPORTED_ENCODINGS;

    private final String encode;
    EncodingTypes(String encode) {
        this.encode = encode;
    }

    static {
        StringBuilder sb = new StringBuilder();
        for (EncodingTypes type : EncodingTypes.values()) {
            sb.append(type.encode + ", ");
        }
        SUPPORTED_ENCODINGS = sb.substring(0, sb.length() - 2);
    }



}
