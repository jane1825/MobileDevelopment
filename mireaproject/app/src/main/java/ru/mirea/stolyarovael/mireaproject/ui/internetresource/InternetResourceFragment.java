package ru.mirea.stolyarovael.mireaproject.ui.internetresource;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import ru.mirea.stolyarovael.mireaproject.R;

public class InternetResourceFragment extends Fragment {

    private TextView resultText;
    private TextView userId;
    private TextView todoId;
    private TextView todoText;
    private TextView isDone;
    private Button btnFetchCountry;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_internet_resource, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultText = view.findViewById(R.id.tvCountryInfo);
        userId = view.findViewById(R.id.user);
        todoId = view.findViewById(R.id.todoid);
        todoText = view.findViewById(R.id.todo);
        isDone = view.findViewById(R.id.done);

        btnFetchCountry = view.findViewById(R.id.btnFetchCountry);
        final Random random = new Random();

        btnFetchCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using ConnectivityManager as requested
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo.isConnected()) {
                    int indx = random.nextInt(200);

                    String urlString = "https://jsonplaceholder.typicode.com/todos/" + Integer.toString(indx);
                    new FetchCountryTask().execute(urlString);

                } else {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class FetchCountryTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            resultText.setText("Загрузка...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadData(urls[0]);
            } catch (IOException e) {
                return "Error fetching data: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (isAdded()) {
                resultText.setText("Найдено");
                try {
                    JSONObject responseJson = new JSONObject(result);
                    userId.setText("ID пользователя: "+ responseJson.optString("userId", "N/A"));
                    todoId.setText("ID замтки: "+ responseJson.optString("id", "N/A"));
                    todoText.setText("Содержание заметки: "+ responseJson.optString("title", "N/A"));
                    isDone.setText("Сдеално? - "+responseJson.optString("completed", "N/A"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        }

        private String downloadData(String urlString) throws IOException {
            InputStream inputStream = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }
                    return result.toString("UTF-8");
                } else {
                    return "Error: Server returned code " + responseCode;
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
    }
}
