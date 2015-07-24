package com.android.bizvoxexam;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class GetAShotApiDribbble {

    private Context context;

    private ShotItem shotItem = null;

    private int shotID;

    public GetAShotApiDribbble(Context context){
        this.context = context;
    }

    // Realiza a consulta a API em background, selecionando o Shot desejado.
    public void getAShot(int id){
        shotID = id;
        String url_param = "https://api.dribbble.com/v1/shots/"+ shotID +"?access_token=c8d5cc7b12dc06f00244a5072a68c51c6b0064018a985ec5fd501ed88256b81d";
        new HttpAsyncTask().execute(url_param);
    }

    // Realiza a requisição a API selecionando o Shot desejado
    public void getShot(String url_param) {
        URL url = null;
        InputStream inputStream = null;

        try {
            url = new URL(url_param);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                convertInputStreamToShot(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ShotItem getAShot() {
        return shotItem;
    }

    // Realiza o download da imagem do Shot selecionado
    public void downloadImageFromShot(){
        URL url = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

            String imgUrl = shotItem.getImageURL();
            if (!imgUrl.equals("null")){
                try {
                    url = new URL(imgUrl);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    shotItem.setImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        Log.i("ShotItem " + shotID, "Image downloaded with success.");
    }



    // Classe responsável por fazer a requisição e download de um Shot do Dribbble em background
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        // Enquanto é realizado o download das informações em background, é exibido um ProgressDialog.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Wait", "Receiving Dribbble Shot...");
        }

        @Override
        protected String doInBackground(String... urls) {
            // Realiza a consulta a Dribbble API e guarda as informações do Shot
            getShot(urls[0]);
            // Realiza o download da imagem correspondente ao Shot
            downloadImageFromShot();
            return "doInBackground - OK";
        }

        // Depois que tudo foi executado em background, é exibido o Progress Dialog é finalizado e o Shot desejado é exibido.
        @Override
        protected void onPostExecute(String result) {
            GetAShotActivity.loadShotData();
            progressDialog.dismiss();
        }
    }

    // Recebe o inputstream de retorno da chamada a API e cria um Shot com todos os atributos desejados.
    private void convertInputStreamToShot (InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        while((line = bufferedReader.readLine()) != null) {

            line = line.trim();

            if ((line.startsWith("\"")) && (line.contains(":"))) {
                String[] lineArray = line.split(":");
                lineArray[0] = lineArray[0].replaceAll("\"", "");
                lineArray[1] = lineArray[1].replaceAll("\"", "");
                if (lineArray[1].endsWith(",")) {
                    lineArray[1] = lineArray[1].substring(0, lineArray[1].length() - 1);
                }

                // Se encontrou o titulo, então criamos o shot
                if (lineArray[0].equals("title")){
                    shotItem = new ShotItem();
                    shotItem.setTitle(lineArray[1].trim());
                }
                // Guarda apenas a imagem de tamanho normal
                else if (lineArray[0].equals("normal")){
                    String imgURL = lineArray[1] + ":" + lineArray[2];
                    imgURL = imgURL.replaceAll("\"", "");
                    if (imgURL.endsWith(",")) {
                        imgURL = imgURL.substring(0, imgURL.length() - 1);
                    }
                    shotItem.setImageURL(imgURL.trim());
                }
                // Como pode conter caraceteres especiais e html tags na descrição, é necessário tratar esse caso sepradamente
                else if (lineArray[0].equals("description")){
                    String description = "\"description\":";
                    line = line.substring(description.length(), line.length()-1);
                    // removendo todas as tags da descrição
                    line = android.text.Html.fromHtml(line).toString();
                    line = line.replaceAll("\\\\n", "");
                    if (line.startsWith("\"")){
                        line = line.substring(1, line.length());
                    }
                    if (line.endsWith("\"")){
                        line = line.substring(0, line.length()-1);
                    }
                    shotItem.setDescription(line.trim());
                }
                else if (lineArray[0].equals("views_count")){
                    shotItem.setViewsCount(lineArray[1].trim());
                }
                else if (lineArray[0].equals("comments_count")){
                    shotItem.setCommentsCount(lineArray[1].trim());
                }
                else if (lineArray[0].equals("created_at")){
                    shotItem.setCreatedAt(lineArray[1].trim());
                }
            }
        }

        inputStream.close();
    }



}
