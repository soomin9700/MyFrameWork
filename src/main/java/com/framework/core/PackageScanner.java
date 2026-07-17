package com.framework.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    /**
     * Retourne toutes les classes trouvées dans le package donné (recursif).
     */
    public static List<Class<?>> scanPackage(String nomPackage) throws Exception {
        List<Class<?>> classesTrouvees = new ArrayList<>();

        if (nomPackage == null || nomPackage.isEmpty()) {
            return classesTrouvees;
        }

        String cheminRelatif = nomPackage.replace('.', '/');

        ClassLoader chargeur = Thread.currentThread().getContextClassLoader();
        URL ressource = chargeur.getResource(cheminRelatif);

        if (ressource == null) {
            return classesTrouvees;
        }

        File dossierRacine = new File(ressource.toURI());

        explorer(nomPackage, dossierRacine, classesTrouvees);

        return classesTrouvees;
    }

    private static void explorer(String packageCourant, File dossier, List<Class<?>> classes) throws ClassNotFoundException {
        File[] contenu = dossier.listFiles();

        if (contenu == null) {
            return;
        }

        for (File element : contenu) {
            if (element.isDirectory()) {
                explorer(packageCourant + "." + element.getName(), element, classes);
                continue;
            }

            if (element.getName().endsWith(".class")) {
                String nomFichier = element.getName();
                String nomClasse = packageCourant + "." + nomFichier.substring(0, nomFichier.length() - 6);
                classes.add(Class.forName(nomClasse));
            }
        }
    }
}