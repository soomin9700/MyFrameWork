package com.framework.core;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private String vue;
    private Map<String, Object> donnees = new HashMap<>();

    public ModelAndView() {
    }

    public ModelAndView(String vue) {
        this.vue = vue;
    }

    public String getVue() {
        return vue;
    }

    public void setVue(String vue) {
        this.vue = vue;
    }

    public Map<String, Object> getDonnees() {
        return donnees;
    }

    public void ajouterAttribut(String cle, Object valeur) {
        donnees.put(cle, valeur);
    }
}