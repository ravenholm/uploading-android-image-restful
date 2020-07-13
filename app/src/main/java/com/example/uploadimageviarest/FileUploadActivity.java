package com.example.uploadimageviarest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Interpolator;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uploadimageviarest.model.FileInfo;
import com.example.uploadimageviarest.remote.APIUtils;
import com.example.uploadimageviarest.remote.FileService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadActivity extends AppCompatActivity {

    FileService fileService;
    Button btnChooseFile;
    Button btnUpload;
    String imagePath;
    String imagePath1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        btnChooseFile = (Button)findViewById(R.id.btnChooseFile);
        btnUpload = (Button)findViewById(R.id.btnUpload);
        fileService = APIUtils.getFileService();
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                File file = new File(imagePath);
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                Call<FileInfo> call = fileService.upload(body);
                call.enqueue(new Callback<FileInfo>() {
                    @Override
                    public void onResponse(Call<FileInfo> call, Response<FileInfo> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(FileUploadActivity.this,
                                    "Image Uploaded Successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(FileUploadActivity.this,
                                    "Error: " + response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FileInfo> call, Throwable t) {
                        Toast.makeText(FileUploadActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this, "Unable to choose image", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri imageUri = data.getData();
            imagePath = getRealPathFromUri(imageUri);
            imagePath1 = ImageFilePath.getPath(getApplicationContext(), data.getData());
            int test = 9;
        }
    }

    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }
}