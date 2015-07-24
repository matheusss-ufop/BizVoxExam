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
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;


public class ListShotsApiDribbble {

    private Context context;
    private String url_param = "https://api.dribbble.com/v1/shots?access_token=c8d5cc7b12dc06f00244a5072a68c51c6b0064018a985ec5fd501ed88256b81d&per_page=30";
    private ArrayList<ShotItem> shotsList = null;

    public ListShotsApiDribbble(Context context){
        this.context = context;
        shotsList = new ArrayList<>();
    }

    public void receiveShotsList (){
        new HttpAsyncTask().execute(url_param);
    }

    public void getListShots(String url_param) {
        URL url = null;
        InputStream inputStream = null;

        try {
            url = new URL(url_param);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                convertInputStreamToShotsList(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadImagesFromListShots(){
        URL url = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

        for (ShotItem shot : shotsList){

            String imgUrl = shot.getImageURL();
            if (!imgUrl.equals("null")){
                try {
                    url = new URL(imgUrl);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    shot.setImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("ListShots", "All images downloaded");
    }

    public ArrayList<ShotItem> getShotsList() {
        return shotsList;
    }

    public int getShotId(int position){
        return shotsList.get(position).getId();
    }


    /**
     * Classe responsável por fazer as requisições e download dos Shots do Dribbble
     */
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Wait", "Receiving Dribbble List Shots...");
        }

        @Override
        protected String doInBackground(String... urls) {
            getListShots(urls[0]);
            downloadImagesFromListShots();
            return "doInBackground - OK";
        }

        @Override
        protected void onPostExecute(String result) {
            MainActivity.loadDribbbleData();
            progressDialog.dismiss();
        }
    }

    private void convertInputStreamToShotsList(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        ShotItem shot = null;

        while((line = bufferedReader.readLine()) != null) {

            line = line.trim();

            // Se encontrou o caractere "{", verificamos se a proxima linha será o "id"
            if (line.equals("{")){
                line = bufferedReader.readLine();
                line = line.trim();

                if (line.startsWith("\"id\"")) {
                    String[] lineArray = line.split(":");
                    lineArray[1] = lineArray[1].substring(0, lineArray[1].length() - 1);

                    // Se encontrou um novo ID, então adicionamos o antigo shot a lista e criamos um novo shot
                    if (shot != null) {
                        shotsList.add(shot);
                    }
                    shot = new ShotItem();
                    shot.setId(new Integer(lineArray[1].trim()));
                }
            }
            else if ((line.startsWith("\"")) && (line.contains(":"))) {
                String[] lineArray = line.split(":");
                lineArray[0] = lineArray[0].replaceAll("\"", "");
                lineArray[1] = lineArray[1].replaceAll("\"", "");
                if (lineArray[1].endsWith(",")) {
                    lineArray[1] = lineArray[1].substring(0, lineArray[1].length() - 1);
                }

                if (lineArray[0].equals("title")){
                    shot.setTitle(lineArray[1].trim());
                }
                else if (lineArray[0].equals("teaser")){
                    String imgURL = lineArray[1] + ":" + lineArray[2];
                    imgURL = imgURL.replaceAll("\"", "");
                    if (imgURL.endsWith(",")) {
                        imgURL = imgURL.substring(0, imgURL.length() - 1);
                    }
                    shot.setImageURL(imgURL.trim());
                } else if (lineArray[0].equals("description")){
                    shot.setDescription(lineArray[1].trim());
                } else if (lineArray[0].equals("views_count")){
                    shot.setViewsCount(lineArray[1].trim());
                }
                else if (lineArray[0].equals("comments_count")){
                    shot.setCommentsCount(lineArray[1].trim());
                }
                else if (lineArray[0].equals("created_at")){
                    shot.setCreatedAt(lineArray[1].trim());
                }
            }
        }

        // Adiciona o último shot encontrado a lista
        if (shot != null){
            shotsList.add(shot);
        }

        inputStream.close();
    }

}
