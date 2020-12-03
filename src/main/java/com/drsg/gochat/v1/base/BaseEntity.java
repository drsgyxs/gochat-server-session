package com.drsg.gochat.v1.base;

import org.springframework.core.style.ToStringCreator;

import java.io.Serializable;
import java.lang.reflect.Field;

public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 4441236828547625093L;

    @Override
    public String toString() {
        ToStringCreator creator = new ToStringCreator(this);
        Class<? extends BaseEntity> aClass = this.getClass();
        try {
            for (Field field : aClass.getDeclaredFields()) {
                field.setAccessible(true);
                creator.append(field.getName(), field.get(this));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return creator.toString();
    }
}
