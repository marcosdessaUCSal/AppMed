package com.pi.upmed.model;

import com.pi.upmed.model.ClassesDB;

import java.util.ArrayList;


// Esta classe estabelece um meio para conter notas feitas por pacientes.
// Poderão ser pacientes diferentes. A responsabilidade das escolhas cabe à rotina externa que
// acionar o método 'addNotaPac. A única exigência é que a entrada seja um objeto 'NotaPaciente'

//

public class EstruturaNotasPac {

    private ArrayList<ClassesDB.NotaPaciente> arrayList;

    public EstruturaNotasPac() {
        this.arrayList = new ArrayList<ClassesDB.NotaPaciente>();
    }

    // Retorna o array list atualizado até o momento
    public ArrayList<ClassesDB.NotaPaciente> getNotasPac () {
        return this.arrayList;
    }

    public void addNotaPac (ClassesDB.NotaPaciente nota) {
        this.arrayList.add(nota);
    }

    // Informa o número de elementos do array list
    public int getTamanho() {
        return this.arrayList.size();
    }

    // Retorna um elemento do array list a partir de um índice
    public ClassesDB.NotaPaciente getElemento(int elemento) {
        return this.arrayList.get(elemento);
    }

    // Apaga todos os dados no array list
    public void limpaRegistros() {
        this.arrayList.clear();
    }
}
