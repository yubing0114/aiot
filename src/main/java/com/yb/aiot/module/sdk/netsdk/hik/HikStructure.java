package com.yb.aiot.module.sdk.netsdk.hik;

import com.sun.jna.Structure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author author
 * @date 2022/5/6 11:00
 */
public class HikStructure extends Structure {

    protected List<String> getFieldOrder() {
        List<String> fieldOrderList = new ArrayList();
        for (Class<?> cls = getClass(); !cls.equals(HikStructure.class); cls = cls.getSuperclass()) {
            Field[] fields = cls.getDeclaredFields();
            int modifiers;
            for (Field field : fields) {
                modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
                    continue;
                }
                fieldOrderList.add(field.getName());
            }
        }
        return fieldOrderList;
    }

    @Override
    public int fieldOffset(String name) {
        return super.fieldOffset(name);
    }

}
