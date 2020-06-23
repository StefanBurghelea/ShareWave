package com.example.sharewave.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;

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
import com.example.sharewave.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    EditText txtusername,txtfirstname,txtlastname,txtemail,txtpassword,txtrepassword;
    Button btnRegister;
    ImageView imgLogin;
    RequestQueue requestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        declarar(root);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name,email,firstname,lastname,password,repassword;

                name= txtusername.getText().toString().trim();
                email = txtemail.getText().toString().trim();
                firstname = txtfirstname.getText().toString().trim();
                lastname = txtlastname.getText().toString().trim();
                password = txtpassword.getText().toString().trim();
                repassword = txtrepassword.getText().toString().trim();

                requestQueue= Volley.newRequestQueue(getContext());

                if (email.equals("")||email.equals(null)||
                        name.equals("")||name.equals(null)||
                        firstname.equals("")||firstname.equals(null)||
                        lastname.equals("")||lastname.equals(null)||
                        password.equals("")||password.equals(null)||
                        repassword.equals("")||repassword.equals(null)){

                    Toast.makeText(getContext(),
                            "Insira algo no email/password",
                            Toast.LENGTH_LONG).show();

                }
                else {

                    register(name,email,firstname,lastname,password,repassword);

                }

            }
        });

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mudarparalogin();
            }
        });

        return root;

    }

    private void mudarparalogin() {
        LoginFragment newFragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contents, newFragment);
        transaction.commit();
    }

    private void register(String name, String email, String firstname, String lastname, String password, String repassword) {

        String URL = "http://checkwaves.com/api/register";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("firstname", firstname);
            jsonBody.put("lastname", lastname);
            jsonBody.put("password", password);
            final String requestBody = jsonBody.toString();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("VOLLEYNICE", response);

                    Toasty.success(getContext(), "Register completed", Toast.LENGTH_SHORT, true).show();

                    mudarparalogin();

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

        txtusername = root.findViewById(R.id.username);
        txtfirstname = root.findViewById(R.id.firstname);
        txtlastname = root.findViewById(R.id.lastname);
        txtemail = root.findViewById(R.id.email);
        txtpassword = root.findViewById(R.id.password);
        txtrepassword = root.findViewById(R.id.repassword);
        btnRegister = root.findViewById(R.id.btn_register);
        imgLogin = root.findViewById(R.id.loginurl);
    }



}
