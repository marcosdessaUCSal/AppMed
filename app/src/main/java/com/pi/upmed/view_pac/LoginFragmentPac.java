package com.pi.upmed.view_pac;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.pi.upmed.DAO.BancoGeralHelper;
import com.pi.upmed.DAO.Crud;
import com.pi.upmed.R;
import com.pi.upmed.persist_estado.EstadoAtualPac;


public class LoginFragmentPac extends Fragment{

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_pac, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        // Referencia o estado do app
        final EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        // Referencia o FAB (para enviar os dados)
        FloatingActionButton fap = getActivity().findViewById(R.id.fab_login_pac);

        // Cria um listener para iniciar o processamento que define se o login foi feito
        fap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazLogin(v);
            }
        });



    }

    public void fazLogin(View view) {
        String digitadoLogin, digitadoSenha;
        long id;
        boolean lembrar;
        EditText captura;
        CheckBox box;
        EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance(); // note que a classe é singleton
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        // captura login digitado
        captura = (EditText) getActivity().findViewById(R.id.pac_inserir_login);
        digitadoLogin = captura.getText().toString();

        // captura senha digitada
        captura = (EditText) getActivity().findViewById(R.id.pac_inserir_senha);
        digitadoSenha = captura.getText().toString();

        // verifica se o botão 'manter-se logado' está selecionado
        box = (CheckBox) getActivity().findViewById(R.id.pac_manter_logado);
        lembrar = box.isChecked();

        // Habilita o banco de dados geral
        SQLiteOpenHelper helperGeral = new BancoGeralHelper(getContext());
        SQLiteDatabase dbGeral = helperGeral.getReadableDatabase();

        // Verifica login
        if (!Crud.existePaciente(dbGeral, digitadoLogin)) {
            // Se não existir o login, segue um toast, depois retorna
            Toast toast = Toast.makeText(getContext(), "Login não existe!", Toast.LENGTH_SHORT);
            toast.show();

            dbGeral.close();
            return;

        }

        // Acha o id pra conferir a senha no banco com a que foi digitada.
        id = Crud.achaIdPaciente(dbGeral, digitadoLogin);
        if (!Crud.confereSenhaPac(dbGeral, id, digitadoSenha)) {
            // Se a senha não confere, segue um toast, depois retorna
            Toast toast = Toast.makeText(getContext(), "Senha incorreta!", Toast.LENGTH_SHORT);
            toast.show();

            dbGeral.close();
            return;
        }

        // Fecha o acesso ao banco
        dbGeral.close();

        // A partir daqui, login e senha conferem ----
        // O login é registrado
        estadoAtualPac.fazLogin(getContext(), id, digitadoLogin, digitadoSenha, lembrar);


        Toast toast = Toast.makeText(getContext(), "Login efetuado!", Toast.LENGTH_SHORT);
        toast.show();

        // Atualiza o menu de acordo com o estado de logado
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        refreshMenu(true, navigationView);


        Fragment fragment = new WelcomeFragmentPac();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        toolbar.setTitle(getString(R.string.app_name) + " Paciente");
        ft.replace(R.id.content_frame, fragment);
        ft.disallowAddToBackStack();
        ft.commit();
    }

    public void refreshMenu(boolean on, NavigationView navigationView) {

        if (on) {
            ligaDesligaItensMenu(navigationView,true);
        } else {
            ligaDesligaItensMenu(navigationView,false);
        }

    }

    // Isto aqui ativa/desativa itens do menu drawer conforme o usuário esteja ou não logado
    private void ligaDesligaItensMenu(NavigationView navigationView, boolean on) {
        navigationView.getMenu().findItem(R.id.item_pac_login).setVisible(!on);
        navigationView.getMenu().findItem(R.id.item_pac_logoff).setVisible(on);
        navigationView.getMenu().findItem(R.id.item_pac_criar_conta).setVisible(!on);
        navigationView.getMenu().findItem(R.id.item_pac_grupos).setVisible(on);
        navigationView.getMenu().findItem(R.id.item_pac_remedios).setVisible(on);
        navigationView.getMenu().findItem(R.id.item_pac_notas).setVisible(on);
    }
}
