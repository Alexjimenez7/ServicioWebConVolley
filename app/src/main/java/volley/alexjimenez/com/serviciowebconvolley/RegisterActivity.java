package volley.alexjimenez.com.serviciowebconvolley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtUserName,txtPassword1,txtPassword2;
    private ProgressDialog  m_progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtUserName = (EditText) findViewById(R.id.txtUsuario_registro);
        txtPassword1 = (EditText) findViewById(R.id.txtPassword1);
        txtPassword2 = (EditText) findViewById(R.id.txtPassword2);

    }
    public void registrar(View v) {
        // Validar datos
        if (txtUserName.getText().toString().trim().equals("")) {
            txtUserName.setError("Usuario requerido!");
            return;
        }
        if (txtPassword1.getText().toString().trim().equals("")) {
            txtPassword1.setError("Password Requerido!");
            return;
        }
        if (txtPassword2.getText().toString().trim().equals("")) {
            txtPassword2.setError("Es necesario confirmar password 2");
            return;
        }
        if (txtPassword1.getText().toString().trim().equals(
                txtPassword2.getText().toString().trim() )  ) {
            // ejecutar tarea de registro
            tareaRegistro();
        } else {
            txtPassword2.setError("El password 2 no coincide con el password 1!");
            return;
        }
    }

    private void tareaRegistro() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.SERVICE_API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        int result = 0;
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            result = jsonObject.getInt("result");
                        } catch (JSONException e) {
                            Log.e("ERROR",e.getMessage());
                            m_progressDialog.dismiss();
                            return;
                        }
                        if (result == Common.RESULT_SUCCESS){
                            Toast.makeText(RegisterActivity.this,
                                    "Registro satisfactorio",Toast.LENGTH_LONG).show();
                            Intent i = new Intent();
                            i.putExtra("username",txtUserName.getText().toString());
                            i.putExtra("password",txtPassword1.getText().toString());
                            setResult(RESULT_OK,i);
                            finish();
                        } else if (result == Common.RESULT_USER_EXISTS) {
                            Toast.makeText(RegisterActivity.this,
                                    "Usuario ya existe!",Toast.LENGTH_LONG).show();
                            m_progressDialog.dismiss();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Registro fallido!",Toast.LENGTH_LONG).show();
                            m_progressDialog.dismiss();
                        }

                        m_progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("action","add");
                params.put("username",txtUserName.getText().toString());
                params.put("password",txtPassword1.getText().toString());
                return params;
            }
        };

        m_progressDialog =  ProgressDialog.show(RegisterActivity.this,
                "Por favor espere...","Procesando...",true);

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void cancelar(View view) {
        Intent i = new Intent();
        setResult(RESULT_CANCELED,i);
        finish();
    }


}
