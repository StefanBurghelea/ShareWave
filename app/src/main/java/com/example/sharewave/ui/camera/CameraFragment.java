package com.example.sharewave.ui.camera;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.sharewave.R;


public class CameraFragment extends Fragment {

    EditText txtSegundos;
    Button btnClick;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        txtSegundos = root.findViewById(R.id.txtSegundos);
        btnClick = root.findViewById(R.id.btnMandar);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = txtSegundos.getText().toString();
                runner.execute(sleepTime);
            }
        });



        return root;

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            Toast.makeText(getContext(),"Sucesso"+txtSegundos.getText(),Toast.LENGTH_LONG).show();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(),
                    "A Processar",
                    "Esperar "+txtSegundos.getText().toString()+ " segundos");
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}