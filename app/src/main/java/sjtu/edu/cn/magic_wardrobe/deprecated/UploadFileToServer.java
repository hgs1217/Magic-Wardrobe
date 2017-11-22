//package sjtu.edu.cn.magic_wardrobe.deprecated;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.View;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//
//import java.io.File;
//
//import sjtu.edu.cn.clothes_image_processing.utils.Config;
//
//import static android.content.ContentValues.TAG;
//import static sjtu.edu.cn.clothes_image_processing.R.id.progressBar;
//import static sjtu.edu.cn.clothes_image_processing.R.id.txtPercentage;
//
///**
// * Created by HgS_1217_ on 2017/11/21.
// */
//
//
//public class UploadFileToServer extends AsyncTask<Void, Integer, String> {
//    /**
//     * Uploading the file to server
//     */
//    @Override
//    protected void onPreExecute() {
//        // setting progress bar to zero
//        progressBar.setProgress(0);
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... progress) {
//        // Making progress bar visible
//        progressBar.setVisibility(View.VISIBLE);
//
//        // updating progress bar value
//        progressBar.setProgress(progress[0]);
//
//        // updating percentage value
//        txtPercentage.setText(String.valueOf(progress[0]) + "%");
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        return uploadFile();
//    }
//
//    @SuppressWarnings("deprecation")
//    private String uploadFile() {
//        String responseString, entityName;
//
//        HttpClient httpclient = new DefaultHttpClient();
//
//        HttpPost httppost;
//        if (isImage) {
//            httppost = new HttpPost(Config.IMAGE_UPLOAD_URL);
//            entityName = "image";
//        } else {
//            httppost = new HttpPost(Config.VIDEO_UPLOAD_URL);
//            entityName = "file";
//        }
//
//        try {
//            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                    new AndroidMultiPartEntity.ProgressListener() {
//                        @Override
//                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
//                        }
//                    });
//
//            File sourceFile = new File(filePath);
//
//            // Adding file data to http body
//            entity.addPart(entityName, new FileBody(sourceFile));
//
//            totalSize = entity.getContentLength();
//            httppost.setEntity(entity);
//
//            // Making server call
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity r_entity = response.getEntity();
//
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == 200) {
//                // Server response
//                responseString = EntityUtils.toString(r_entity, "utf-8");
//            } else {
//                responseString = "Error occurred! Http Status Code: " + statusCode;
//            }
//        } catch (Exception e) {
//            responseString = e.toString();
//        }
//
//        return responseString;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        Log.e(TAG, "Response from server: " + result);
//
//        // showing the server response in an alert dialog
//        showAlert(result);
//
//        super.onPostExecute(result);
//    }
//
//}
//
//    /**
//     * Method to show alert dialog
//     */
//    private void showAlert(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(message)
//                .setTitle("Response from Servers")
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // do nothing
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//}
