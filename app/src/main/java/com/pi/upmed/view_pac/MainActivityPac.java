package com.pi.upmed.view_pac;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.pi.upmed.DAO.BancoConfigHelperPac;
import com.pi.upmed.DAO.BancoGeralHelper;
import com.pi.upmed.R;
import com.pi.upmed.StartActivity;
import com.pi.upmed.persist_estado.EstadoAtualPac;

public class MainActivityPac extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pac);

        // Inicializando os bancos, caso não tenham sido criados.
        SQLiteOpenHelper helper = new BancoGeralHelper(this);
        helper.close();
        helper = new BancoConfigHelperPac(this);
        helper.close();

        // Inicializa o estado atual de configurações e associa ao contexto
        EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();
        estadoAtualPac.inicializar(this);

        // Isto configura a toolbar como a appbar, fazendo referência ao elemento 'toolbar'
        // configurado no arquivo 'activity_main_pac'
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Atualiza o estado das opções no menu drawer
        refreshMenu(estadoAtualPac.estaLogado(), navigationView);


        // Inicia o app pelo fragmento de boas vindas
        Fragment fragment = new WelcomeFragmentPac();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment);
        ft.commit();

    }

    // Este método é chamado a cada vez que um item do menu drawer é acionado
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;

        // Obtém o estado atual do app
        EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        // A variável local 'toolbar' faz referência à tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Estabelece uma referência aos itens do menu para acessar suas propriedades
        NavigationView itemMenu = (NavigationView) findViewById(R.id.nav_view);

        // Faz referência à barra de botões extra (tela das notas)
        FrameLayout frame = (FrameLayout) findViewById(R.id.botoes_base);

        // Faz referência ao frame layout (que suporta todos os fragments)
        // Depois, põe o frame layout no tamanho padrão, escondendo os botões de base
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        ViewGroup.LayoutParams params = contentFrame.getLayoutParams();
        params.height = params.MATCH_PARENT;
        contentFrame.setLayoutParams(params);

        AlertDialog.Builder builder;
        AlertDialog confirma, alert;

        // Escolhas, de acordo com a seleção do usuário no menu
        switch(id) {
            //ligaDesligaItensMenu(itemMenu,true);
            case R.id.item_pac_login:
                // MENU LOGIN
                fragment = new LoginFragmentPac();
                toolbar.setTitle("Login");
                frame.setVisibility(View.GONE);
                break;

            case R.id.item_pac_logoff:
                // FAZ LOGOFF
                ligaDesligaItensMenu(itemMenu,false);
                fragment = new WelcomeFragmentPac();
                toolbar.setTitle(getString(R.string.app_name));
                //EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();
                estadoAtualPac.setLogoff(this);
                frame.setVisibility(View.GONE);
                builder = new AlertDialog.Builder(this);

                builder.setMessage("Você perderá acesso às funcionalidades do app," +
                        "até que logue outra vez em alguma de suas contas.");
                builder.setPositiveButton("Entendi", null);


                confirma = builder.create();
                confirma.show();
                break;

            case R.id.item_pac_criar_conta:
                // MENU CRIAÇÃO DE USUÁRIO PACIENTE
                fragment = new CriarPacFragment();
                toolbar.setTitle("Criar usuário paciente");
                frame.setVisibility(View.GONE);
                break;

            case R.id.item_pac_grupos:
                // MENU DE GRUPOS
                fragment = new GruposPacFragment();
                toolbar.setTitle("Grupo");
                frame.setVisibility(View.GONE);
                break;

            case R.id.item_pac_remedios:
                // MENU REMEDIOS
                fragment = new RemediosPacFragment();
                toolbar.setTitle("Remédios");
                frame.setVisibility(View.GONE);
                break;

            case R.id.item_pac_notas:
                // MENU DE NOTAS
                if (estadoAtualPac.getIdGrupoAtual() == -1) {
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage("É necessário primeiro selecionar um grupo.");
                    builder.setPositiveButton("Entendi", null);
                    confirma = builder.create();
                    confirma.show();
                } else {
                    fragment = new NotasPacFragment();
                    toolbar.setTitle("Notas");
                }
                break;

            case R.id.item_pac_config:
                // IMPLEMENTAR
                // Avisa que esta funcionalidade ainda não está implementada
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Ops!");
                builder.setMessage("Esta funcionalidade ainda não está implementada!");
                builder.setPositiveButton("Entendi", null);
                alert = builder.create();
                alert.show();
                break;

            case R.id.item_pac_trocar:
                // ALTERNAR APLICATIVO
                Intent intent = new Intent(this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                frame.setVisibility(View.GONE);
                startActivity(intent);
                break;

            case R.id.item_pac_reset:
                // IMPLEMENTAR
                // Avisa que esta funcionalidade ainda não está implementada
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Ops!");
                builder.setMessage("Esta funcionalidade ainda não está implementada!");
                builder.setPositiveButton("Entendi", null);
                alert = builder.create();
                alert.show();
                break;

            case R.id.item_pac_sobre:
                // MENU COM INFORMAÇÕES SOBRE O TRABALHO
                // CONSTRUIR UM MENU COM IMAGEM DA CAPA DO RELATÓRIO DO P.I.

                // IMPLEMENTAR
                // Avisa que esta funcionalidade ainda não está implementada
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Ops!");
                builder.setMessage("Esta funcionalidade ainda não está implementada!");
                builder.setPositiveButton("Entendi", null);
                alert = builder.create();
                alert.show();
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.disallowAddToBackStack();
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    // MÉTODOS DO MENU DRAWER ----------------------------------------------------------------------
    // Mantido apenas por causa da chamada de 'logoff' no menu

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
