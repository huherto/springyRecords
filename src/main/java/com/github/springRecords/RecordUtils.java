package com.github.springRecords;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class RecordUtils {

	public static Field autoIncrementField(Class<?> recClass) {
		for(Field field: recClass.getFields()) {
			int mod =recClass.getModifiers();
			if (Modifier.isPublic(mod) && !Modifier.isStatic(mod)) {
				for(Annotation a: field.getAnnotations()) {
					if (a.annotationType().isAssignableFrom(Autoincrement.class)) {
						return field;
					}
				}
			}
		}
		return null;
	}

}
