package com.example.mall.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum RegisterResultEnum implements Serializable {

    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    NAME_ALREADY_USED("NAME_ALREADY_USED");

    private final String desc;

    RegisterResultEnum(String desc) {
        this.desc = desc;
    }

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static RegisterResultEnum fromName(@JsonProperty("name") String name) {
        if (name == null) {
            throw new IllegalArgumentException("枚举 name 不能为空");
        }
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的注册结果枚举 name: " + name);
        }
    }

    public static RegisterResultEnum getByDesc(String desc) {
        if (desc == null) {
            return null;
        }
        for (RegisterResultEnum e : values()) {
            if (e.desc.equals(desc)) {
                return e;
            }
        }
        return null;
    }

    private Object readResolve() {
        return valueOf(name());
    }

    @Override
    public String toString() {
        return "RegisterResultEnum{name='" + name() + "', desc='" + desc + "'}";
    }
}