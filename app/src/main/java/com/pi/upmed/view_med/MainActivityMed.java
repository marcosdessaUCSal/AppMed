package com.pi.upmed.view_med;

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
import com.pi.upmed.DAO.BancoConfigHelperMed;
import com.pi.upmed.DAO.BancoGeralHelper;
import com.pi.upmed.R;
import com.pi.upmed.StartActivity;
import com.pi.upmed.persist_estado.EstadoAtualMed;

public class MainActivityMed extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_med);

        // Inicializando os bancos, caso não tenham sido criados.
        SQLiteOpenHelper helper = new BancoGeralHelper(this);
        helper.close();
        helper = new BancoConfigHelperMed(this);
        helper.close();

        // Inicializa o estado atual de configurações e associa ao contexto
        EstadoAtualMed estadoAtualMed = EstadoAtualMed.getInstance();
        estadoAtualMed.inicializar(this);

        // Isto configura a toolbar como a appbar, fazendo referência ao elemento 'toolbar'
        // configurado no arquivo 'activity_main_med'
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
        refreshMenu(estadoAtualMed.estaLogado(), navigationView);


        // Inicia o app pelo fragmento de boas vindas
        Fragment fragment = new WelcomeFragmentMed();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment);
        ft.commit();


    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;

        // Obtém o estado atual do app
        EstadoAtualMed estadoAtualMed = EstadoAtualMed.getInstance();

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
        AlertDialog confirma;
        AlertDialog alert;

        // Escolhas, de acordo com a seleção do usuário no menu
        switch(id) {
            //ligaDesligaItensMenu(itemMenu,true);
            case R.id.item_med_login:
                // MENU LOGIN
                fragment = new LoginFragmentMed();
                toolbar.setTitle("Login");
                frame.setVisibility(View.GONE);
                break;

            case R.id.item_med_logoff:
                // FAZ LOGOFF
                ligaDesligaItensMenu(itemMenu,false);
                fragment = new WelcomeFragmentMed();
                toolbar.setTitle(getString(R.string.app_name));
                estadoAtualMed.setLogoff(this);
                frame.setVisibility(View.GONE);
                builder = new AlertDialog.Builder(this);

                builder.setMessage("Você perderá acesso às funcionalidades do app," +
                        "até que logue outra vez em alguma de suas contas.");
                builder.setPositiveButton("Entendi", null);


                confirma = builder.create();
                confirma.show();
                break;

            case R.id.item_med_criar_conta:
                // MENU CRIAÇÃO DE USUÁRIO PACIENTE
                /*
                fragment = new CriarPacFragment();
                toolbar.setTitle("Criar usuário paciente");
                frame.setVisibility(View.GONE);

                 */
                break;

            case R.id.item_med_grupos:
                // MENU DE GRUPOS

                fragment = new GruposMedFragment();
                toolbar.setTitle("Grupo");
                frame.setVisibility(View.GONE);


                break;

            case R.id.item_med_remedios:
                // IMPLEMENTAR
                // Avisa que esta funcionalidade ainda não está implementada
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Ops!");
                builder.setMessage("Esta funcionalidade ainda não está implementada!");
                builder.setPositiveButton("Entendi", null);
                alert = builder.create();
                alert.show();
                break;

            case R.id.item_med_notas_medicos:
                if (estadoAtualMed.getIdGrupoAtual() == -1) {
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage("É necessário primeiro selecionar um grupo.");
                    builder.setPositiveButton("Entendi", null);
                    confirma = builder.create();
                    confirma.show();
                } else {
                    fragment = new NotasMedFragmentMed();
                    toolbar.setTitle("Notas de médicos");
                }
                break;

            case R.id.item_med_notas:
                // MENU DE NOTAS

                if (estadoAtualMed.getIdGrupoAtual() == -1) {
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage("É necessário primeiro selecionar um grupo.");
                    builder.setPositiveButton("Entendi", null);
                    confirma = builder.create();
                    confirma.show();
                } else {
                    fragment = new NotasMedFragmentPac();
                    toolbar.setTitle("Notas de pacientes");
                }
                break;

            case R.id.item_med_config:
                // IMPLEMENTAR
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Ops!");
                builder.setMessage("Esta funcionalidade ainda não está implementada!");
                builder.setPositiveButton("Entendi", null);
                alert = builder.create();
                alert.show();
                break;

            case R.id.item_med_trocar:
                // ALTERNAR APLICATIVO
                Intent intent = new Intent(this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                frame.setVisibility(View.GONE);
                startActivity(intent);
                break;

            case R.id.item_med_reset:
                // IMPLEMENTAR
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Ops!");
                builder.setMessage("Esta funcionalidade ainda não está implementada!");
                builder.setPositiveButton("Entendi", null);
                alert = builder.create();
                alert.show();
                break;

            case R.id.item_med_sobre:
                // IMPLEMENTAR
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
        navigationView.getMenu().findItem(R.id.item_med_login).setVisible(!on);
        navigationView.getMenu().findItem(R.id.item_med_logoff).setVisible(on);
        navigationView.getMenu().findItem(R.id.item_med_criar_conta).setVisible(!on);
        navigationView.getMenu().findItem(R.id.item_med_grupos).setVisible(on);
        navigationView.getMenu().findItem(R.id.item_med_remedios).setVisible(on);
        navigationView.getMenu().findItem(R.id.item_med_notas).setVisible(on);
    }

}
