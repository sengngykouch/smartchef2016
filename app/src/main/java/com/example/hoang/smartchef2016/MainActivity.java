package com.example.hoang.smartchef2016;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Category;
import com.microsoft.projectoxford.vision.contract.Face;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import static android.R.attr.bitmap;


public class MainActivity extends AppCompatActivity {
    private Button uploadButton;
    private ImageView targetImage;
    private TextView textTargetUri;
    private Button cameraButton;
    private VisionServiceClient client;
    private Button submitButton;
    private Bitmap bitmap;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadButton = (Button) findViewById(R.id.button);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        targetImage = (ImageView) findViewById(R.id.targetimage);
        textTargetUri = (TextView) findViewById(R.id.targeturi);
        submitButton = (Button) findViewById(R.id.submitButton);
        textView = (TextView) findViewById(R.id.textView);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
                startActivityForResult(intent, 1);
            }
        });

        if (client==null){
            client = new VisionServiceRestClient(getString(R.string.subscription_key));
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRecognizerAndAnalyze();
                //VisionServiceClient VisionServiceClient = new VisionServiceClient("85b66286720840e28dc1cc4f26b717fe");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void doRecognizerAndAnalyze() {
        try {
            new doRequest().execute();

        } catch (Exception e)
        {
            e.printStackTrace();
           // mEditText.setText("Error encountered. Exception is: " + e.toString());
        }
    }

    private String process() throws VisionServiceException, IOException, InterruptedException {
        Gson gson = new Gson();
        String[] feature = {"Tags"};
        String[] details = {};

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        AnalysisResult resultStream = this.client.analyzeImage(inputStream, feature, details);

        String result = gson.toJson(resultStream);
        //Toast.makeText(this, "Testing charAnalysis ", Toast.LENGTH_LONG).show();
        Log.d("Returning RESULTS are ", result);

        //textView.setText(resultStream.tags.toString());
        return result;
    }

    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doRequest() {
        }

        @Override
        protected String doInBackground(String... args) {

            try {
                return process();
            } catch (Exception e) {
                this.e = e;    // Store error
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

            if (e != null) {
                //mEditText.setText("Error: " + e.getMessage());
                this.e = null;
            } else {
                Gson gson = new Gson();
                AnalysisResult result = gson.fromJson(data, AnalysisResult.class);

//                for (Category category: result.categories) {
//                    if (category.name  == "tag") {
//                        Log.d(result.tags.toString().split(",")[]);
//                        StringBuilder resultBuild = new StringBuilder();
//                        resultBuild.append(result.tags.toString());
//                        for (int j = 0; j < resultBuild.length(); j++) {
//                            if (resultBuild.charAt(j) == '{' ||
//                                    resultBuild.charAt(j) == '}' ||
//                                    resultBuild.charAt(j) == '"' ||
//                                    resultBuild.charAt(j) == ',' ||
//                                    resultBuild.charAt(j) == ':') {
//                                resultBuild.deleteCharAt(j);
//                                j--;
//                            }
//                        }
//                        String finalResult = resultBuild.toString();
//                    }
                  //Log.d("Testing here ", "Category: " + category.name + ", score: " + category.score + "\n");
                }
                //Toast.makeText(MainActivity.this, result.tags.toString(), Toast.LENGTH_LONG).show();
                //mEditText.setSelection(0);

                //Log.d("Test results are", result.toString());

                //Toast.makeText(MainActivity.this, result.tags.toString(), Toast.LENGTH_LONG).show();
            //}
            }
    }
}
