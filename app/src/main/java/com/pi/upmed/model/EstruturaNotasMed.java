package com.pi.upmed.model;


import com.pi.upmed.model.ClassesDB;

import java.util.ArrayList;


// Esta classe estabelece um meio para conter notas feitas por médicos.

public class EstruturaNotasMed {

    private ArrayList<ClassesDB.NotaMedico> arrayList;

    public EstruturaNotasMed() {
        this.arrayList = new ArrayList<>();
    }

    // Retorna o array list atualizado até o momento
    public ArrayList<ClassesDB.NotaMedico> getNotasMed () {
        return this.arrayList;
    }

    public void addNotaMed (ClassesDB.NotaMedico nota) {
        this.arrayList.add(nota);
    }

    // Informa o número de elementos do array list
    public int getTamanho() {
        return this.arrayList.size();
    }

    // Retorna um elemento do array list a partir de um índice
    public ClassesDB.NotaMedico getElemento(int elemento) {
        return this.arrayList.get(elemento);
    }

    // Apaga todos os dados no array list
    public void limpaRegistros() {
        this.arrayList.clear();
    }
}
