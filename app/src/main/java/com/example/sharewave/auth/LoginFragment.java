package com.example.sharewave.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharewave.MainActivity;
import com.example.sharewave.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    EditText txtEmail,txtPassword;
    Button btnLogin;
    ImageView btnRegister;
    RequestQueue requestQueue;
    private DatabaseHelper db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        if (container != null) {
            container.removeAllViews();
        }

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        declarar(root);

        db = new DatabaseHelper(getContext());

        int login=0;

        login = db.usersGuardados();

        if (login==0){

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String email,pass;

                    email= txtEmail.getText().toString().trim();
                    pass = txtPassword.getText().toString().trim();

                    requestQueue=Volley.newRequestQueue(getContext());

                    if (email.equals("")||email.equals(" ")||email.equals(null)||pass.equals("")||pass.equals(" ")||pass.equals(null)){

                        Toast.makeText(getContext(),
                                "Insira algo no email/password",
                                Toast.LENGTH_LONG).show();

                    }
                    else {

                        login(email,pass);

                    }



                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RegisterFragment newFragment = new RegisterFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contents, newFragment);
                    transaction.commit();
                }
            });
        }else {

            mainactitvity();

        }


        return root;
    }

    private void mainactitvity() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

    }

    private void login(final String email,final String pass) {

        String URL = "http://192.168.1.156:8000/api/login";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", pass);
            final String requestBody = jsonBody.toString();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("VOLLEYNICE", response);


                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject userJson = obj.getJSONObject("user");
                        String id = userJson.getString("id");
                        String name = userJson.getString("name");
                        String email = userJson.getString("email");
                        String firstname = userJson.getString("firstname");
                        String lastname = userJson.getString("lastname");

                        db.inserirUser(id,name,email,firstname,lastname);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mainactitvity();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VOLLEYERROR", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void declarar(View root) {

        txtEmail = root.findViewById(R.id.et_email);
        txtPassword = root.findViewById(R.id.et_password);
        btnLogin = root.findViewById(R.id.btn_login);
        btnRegister = root.findViewById(R.id.registerurl);

    }



}
