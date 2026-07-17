package com.framework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationChecker {

    public static boolean surClasse(Class<?> classe, Class<? extends Annotation> annotation) {
        return classe != null && classe.isAnnotationPresent(annotation);
    }

    public static boolean surMethode(Method methode, Class<? extends Annotation> annotation) {
        return methode != null && methode.isAnnotationPresent(annotation);
    }

    public static boolean surAttribut(Field attribut, Class<? extends Annotation> annotation) {
        return attribut != null && attribut.isAnnotationPresent(annotation);
    }

    /**
     * Version générique : fonctionne pour une classe, une méthode ou un champ,
     * puisque les trois implémentent AnnotatedElement.
     */
    public static boolean possede(AnnotatedElement emplacement, Class<? extends Annotation> annotation) {
        return emplacement != null && emplacement.isAnnotationPresent(annotation);
    }
}