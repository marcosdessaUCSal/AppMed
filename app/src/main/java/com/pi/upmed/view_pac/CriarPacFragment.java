package com.pi.upmed.view_pac;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pi.upmed.DAO.BancoGeralHelper;
import com.pi.upmed.DAO.Crud;
import com.pi.upmed.R;
import com.pi.upmed.model.Agora;
import com.pi.upmed.persist_estado.EstadoAtualPac;


/**
 * A simple {@link Fragment} subclass.
 */
public class CriarPacFragment extends Fragment {

    public CriarPacFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criar_pac, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Obtendo referência ao estado
        final EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        // Reação ao botão ENVIAR
        Button buttEnviar = getActivity().findViewById(R.id.pac_criar_butt_enviar);
        buttEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String digitadoNome;
                String digitadoLogin;
                String digitadoSenha1;
                String digitadoSenha2;

                // Esta variável referencia todos os campos, um de cada vez, ao captar textos
                EditText captura;

                // captura nome digitado
                captura = getActivity().findViewById(R.id.pac_criar_nome);
                digitadoNome = captura.getText().toString();

                // captura login digitado
                captura = getActivity().findViewById(R.id.pac_inserir_login);
                digitadoLogin = captura.getText().toString();

                // captura primeira senha
                captura = getActivity().findViewById(R.id.pac_criar_senha);
                digitadoSenha1 = captura.getText().toString();

                // captura segunda senha
                captura = getActivity().findViewById(R.id.pac_repetir_senha);
                digitadoSenha2 = captura.getText().toString();

                // Habilita o banco de dados geral
                SQLiteOpenHelper helperGeral = new BancoGeralHelper(getContext());
                SQLiteDatabase dbGeral = helperGeral.getWritableDatabase();

                // Verifica o que não foi digitado
                //--------------------------------

                if (digitadoNome.length() == 0) {
                    Toast toast = Toast.makeText(getContext(), "Digite o nome", Toast.LENGTH_SHORT);
                    toast.show();
                    dbGeral.close();
                    return;
                }

                if (digitadoLogin.length() == 0) {
                    Toast toast = Toast.makeText(getContext(), "Digite o login", Toast.LENGTH_SHORT);
                    toast.show();
                    dbGeral.close();
                    return;
                }

                if (digitadoSenha1.length() == 0) {
                    Toast toast = Toast.makeText(getContext(), "Digite a senha", Toast.LENGTH_SHORT);
                    toast.show();
                    dbGeral.close();
                    return;
                }

                if (digitadoSenha2.length() == 0) {
                    Toast toast = Toast.makeText(getContext(), "Repita a senha", Toast.LENGTH_SHORT);
                    toast.show();
                    dbGeral.close();
                    return;
                }

                // VERIFICAÇÃO: o login já existe?
                if (Crud.existePaciente(dbGeral, digitadoLogin)) {
                    Toast toast = Toast.makeText(getContext(), "O login já existe", Toast.LENGTH_SHORT);
                    toast.show();
                    dbGeral.close();
                    return;
                }

                // VERIFICAÇÃO: as duas senhas são iguais?
                if (!digitadoSenha1.equals(digitadoSenha2)) {
                    Toast toast = Toast.makeText(getContext(), "Senhas diferentes", Toast.LENGTH_SHORT);
                    toast.show();
                    dbGeral.close();
                    return;
                }


                // Alert dialog para confirmação da criação do usuário
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Novo usuário...");
                StringBuilder texto = new StringBuilder();
                texto.append("Nome: " + digitadoNome + "\n");
                texto.append("Login: " + digitadoLogin + "\n");
                texto.append("Criado em " + Agora.getData());
                builder.setMessage(texto.toString());

                // Botão cancelar
                builder.setNegativeButton("Cancelar", null);

                // Botão confirmar
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


                // ------------------------------------------------
                // A partir daqui, tudo pronto para criar o usuário
                // ------------------------------------------------


                /// IMPLEMENTAR !!! REGISTRAR NO BANCO O NOVO USUÁRIO
                // LEMBRE QUE É NECESSÁRIO REFERENCIAR DATA E HORA ATUAIS NO REGISTRO


                // Não esquecer de fechar o acesso ao banco!
                dbGeral.close();

            }
        });
    }
}
