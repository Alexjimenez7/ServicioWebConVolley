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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText txtUserName,txtPassword;
    private ProgressDialog m_ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUserName = (EditText) findViewById(R.id.txtUser);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

    }

    public void login(View v) {
        //Validar entrada
        if (txtUserName.getText().toString().trim().equals("")) {
            txtUserName.setError("Usuario requerido!!!");
            return;
        }
        if (txtPassword.getText().toString().trim().equals("")) {
            txtPassword.setError("Password requerido!!!");
            return;
        }
        tareaLogin();
    }

    public void registrar_usuario(View v) {
        Intent i = new Intent(MainActivity.this,RegisterActivity.class);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            txtUserName.setText(data.getStringExtra("username"));
            txtPassword.setText(data.getStringExtra("password"));
        }
    }

    public void salir(View v) {
        finish();
    }

    private void tareaLogin() {
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
                            m_ProgressDialog.dismiss();
                            return;
                        }
                        if (result == Common.RESULT_SUCCESS) {
                            Toast.makeText(getApplicationContext(),"Login Correcto!",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),WelcomeActivity.class);
                            i.putExtra("username",txtUserName.getText().toString());
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(),"Login Fallido!",
                                    Toast.LENGTH_LONG).show();
                        }

                        m_ProgressDialog.dismiss();
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
                        params.put("action","login");
                        params.put("username",txtUserName.getText().toString());
                        params.put("password",txtPassword.getText().toString());
                        return params;
                    }
                };

        m_ProgressDialog =  ProgressDialog.show(MainActivity.this,
                "Por favor espere...","Procesando...",true);

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


}
