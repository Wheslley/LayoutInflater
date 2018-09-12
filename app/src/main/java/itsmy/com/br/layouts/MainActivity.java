package itsmy.com.br.layouts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String ESTADO_MODIFICACAO_CHECKBOX = "ESTADO_MODIFICACAO_CHECKBOX";
    private final String ESTADO_MODIFICACAO_RADIOBUTTON = "ESTADO_MODIFICACAO_RADIOBUTTON";
    private final String ESTADO_TELEFONE_ARRAY = "ESTADO_TELEFONE_ARRAY";
    private final String ESTADO_TELEFONE_SPINNER_ARRAY = "ESTADO_TELEFONE_SPINNER_ARRAY";
    private final String ESTADO_EMAIL_ARRAY = "ESTADO_EMAIL_ARRAY";

    private EditText nome;
    private CheckBox modificacoesCheckBox;
    private RadioGroup modificacoesRadioGroup;

    private LinearLayout telefoneLinearLayout;
    private ArrayList<View> telefoneArrayList;

    private LinearLayout emailLinearLayout;
    private ArrayList<View> emailArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_view_activity_main);

        this.nome = findViewById(R.id.nomeEditText);
        //this.telefone = findViewById(R.id.telefoneEditText);
        this.modificacoesCheckBox = findViewById(R.id.notificacoesCheckBox);
        this.modificacoesRadioGroup = findViewById(R.id.notificacoesRadioGroup);

        this.telefoneLinearLayout = findViewById(R.id.telefoneLinearLayout);
        this.telefoneArrayList = new ArrayList<>();

        this.emailLinearLayout = findViewById(R.id.emailLinearLayout);
        this.emailArrayList = new ArrayList<>();

        //this.modificacoesCheckBox.setOnClickListener(this);

        this.modificacoesCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) modificacoesRadioGroup.setVisibility(View.VISIBLE);
                else modificacoesRadioGroup.setVisibility(View.GONE);
            }
        });

        /*this.modificacoesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) modificacoesRadioGroup.setVisibility(View.VISIBLE);
                else modificacoesRadioGroup.setVisibility(View.GONE);
            }
        });*/
    }

    public void adicionarTelefone(View botao){
        if(botao.getId() == R.id.adicionarTelefoneButton){
            LayoutInflater li = getLayoutInflater();
            View novoTelefoneLayout = li.inflate(R.layout.novo_telefone_layout, null);
            this.telefoneArrayList.add(novoTelefoneLayout);
            this.telefoneLinearLayout.addView(novoTelefoneLayout);
        }
    }

    public void adicionarEmail(View botao){
        if(botao.getId() == R.id.adicionarEmailButton){
            LayoutInflater li = getLayoutInflater();
            View novoEmailLayout = li.inflate(R.layout.novo_email_layout, null);
            this.emailArrayList.add(novoEmailLayout);
            this.emailLinearLayout.addView(novoEmailLayout);
        }
    }

    public void limparFormulario(View botao){
        if(botao.getId() == R.id.limparButton) {
            this.nome.setText(null);
            this.modificacoesRadioGroup.clearCheck();
            this.modificacoesCheckBox.setChecked(false);
            if (this.modificacoesCheckBox.isChecked()) this.modificacoesRadioGroup.setVisibility(View.VISIBLE);
            else this.modificacoesRadioGroup.setVisibility(View.GONE);
            this.nome.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.notificacoesCheckBox) {
            if (((CheckBox) v).isChecked()) this.modificacoesRadioGroup.setVisibility(View.VISIBLE);
            else this.modificacoesRadioGroup.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putBoolean(ESTADO_MODIFICACAO_CHECKBOX, modificacoesCheckBox.isChecked());
        outState.putInt(ESTADO_MODIFICACAO_RADIOBUTTON, modificacoesRadioGroup.getCheckedRadioButtonId());
        saveTelefoneLinearLayout(outState);
        saveEmailLinearLayout(outState);

    }

    /*Salva o estado do layout TELEFONE*/
    public void saveTelefoneLinearLayout(Bundle outState){

        ArrayList<String> telefoneArraylist = new ArrayList<>();
        ArrayList<Integer> telefoneSpinnerValueArrayList = new ArrayList<>();

        for (int i = 0; i < this.telefoneLinearLayout.getChildCount(); i++) {
            View viewTel = this.telefoneLinearLayout.getChildAt(i);
            telefoneArraylist.add(((EditText)viewTel.findViewById(R.id.telefoneEditText)).getText().toString());
            telefoneSpinnerValueArrayList.add(((Spinner)viewTel.findViewById(R.id.tipoTelefoneSpinner)).getSelectedItemPosition());
        }

        outState.putStringArrayList(this.ESTADO_TELEFONE_ARRAY, telefoneArraylist);
        outState.putIntegerArrayList(this.ESTADO_TELEFONE_SPINNER_ARRAY, telefoneSpinnerValueArrayList);

    }

    /*Salva o estado do layout EMAIL*/
    public void saveEmailLinearLayout(Bundle outState){

        ArrayList<String> emailArraylist = new ArrayList<>();

        for (int i = 0; i < this.emailLinearLayout.getChildCount(); i++) {
            View viewEmail = this.emailLinearLayout.getChildAt(i);
            emailArraylist.add(((EditText)viewEmail.findViewById(R.id.emailEditText)).getText().toString());
        }

        outState.putStringArrayList(this.ESTADO_EMAIL_ARRAY, emailArraylist);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        modificacoesCheckBox.setChecked(savedInstanceState.getBoolean(ESTADO_MODIFICACAO_CHECKBOX, false));

        if ((modificacoesCheckBox).isChecked())
            modificacoesRadioGroup.setVisibility(View.VISIBLE);
        else modificacoesRadioGroup.setVisibility(View.GONE);

        int idRadioButtonSelecionado = savedInstanceState.getInt(ESTADO_MODIFICACAO_RADIOBUTTON, -1);
        if (idRadioButtonSelecionado != -1) modificacoesRadioGroup.check(idRadioButtonSelecionado);

        recoveryTelefoneLinearLayout(savedInstanceState);
        recoveryEmailLinearLayout(savedInstanceState);

    }

    /*Recupera o estado do layout TELEFONE*/
    public void recoveryTelefoneLinearLayout(Bundle savedInstanceState){
        ArrayList<String> telefoneRecoveryArraylist = savedInstanceState.getStringArrayList(this.ESTADO_TELEFONE_ARRAY);
        ArrayList<Integer> telefoneSpinnerRecoveryValueArrayList = savedInstanceState.getIntegerArrayList(this.ESTADO_TELEFONE_SPINNER_ARRAY);
        if(savedInstanceState != null){
            for(int i = 0; i < telefoneRecoveryArraylist.size(); i ++){
                LayoutInflater layoutInflater = getLayoutInflater();
                View novoTelefoneLayout = layoutInflater.inflate(R.layout.novo_telefone_layout, null);
                if(telefoneRecoveryArraylist.get(i) != null){
                    ((EditText)novoTelefoneLayout.findViewById(R.id.telefoneEditText)).setText(telefoneRecoveryArraylist.get(i));
                    if(telefoneSpinnerRecoveryValueArrayList != null) ((Spinner)novoTelefoneLayout.findViewById(R.id.tipoTelefoneSpinner)).setSelection(telefoneSpinnerRecoveryValueArrayList.get(i));
                }
                this.telefoneLinearLayout.addView(novoTelefoneLayout);
            }

        }
    }

    /*Recupera o estado do layout EMAIL*/
    public void recoveryEmailLinearLayout(Bundle savedInstanceState){
        ArrayList<String> emailRecoveryArrayList = savedInstanceState.getStringArrayList(this.ESTADO_EMAIL_ARRAY);
        if(emailRecoveryArrayList != null){
            for(int i = 0; i < emailRecoveryArrayList.size(); i ++){
                LayoutInflater layoutInflater = getLayoutInflater();
                View viewEmailLayout = layoutInflater.inflate(R.layout.novo_email_layout, null);
                if(emailRecoveryArrayList.get(i) != null) ((EditText)viewEmailLayout.findViewById(R.id.emailEditText)).setText(emailRecoveryArrayList.get(i));
                this.emailLinearLayout.addView(viewEmailLayout);
            }
        }
    }

}
